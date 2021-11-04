package com.hk.ijournal.repository

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.hk.ijournal.repository.local.AlbumDao
import com.hk.ijournal.repository.local.DiaryDao
import com.hk.ijournal.repository.local.IJDatabase
import com.hk.ijournal.repository.local.RoomDao
import com.hk.ijournal.repository.models.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDate
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class DiaryRepository(daoMap: MutableMap<IJDatabase.DaoMapKeys, RoomDao>, private val currentUserId: Long, private val coroutineScope: CoroutineScope) {
    val saveStatus: MutableLiveData<String> = MutableLiveData()

    private var saveFileAndUpdateDbTask: Job? = null
    private var insertAlbumInDbAndDispatchSaveFileTask: Job? = null
    private var pageId: Long? = null
    val currentExternalImgList = MutableLiveData<List<String>>()
    private val diaryDao = daoMap[IJDatabase.DaoMapKeys.Page] as DiaryDao
    private val albumDao = daoMap[IJDatabase.DaoMapKeys.Album] as AlbumDao
    val selectedDateLive = MutableLiveData<LocalDate>()

    private var diaryPageLive: MutableLiveData<DiaryPage> = Transformations.map(selectedDateLive) { selectedDate ->
        runBlocking {
            val page = diaryDao.getPageforDate(selectedDate, currentUserId)
            pageId = page?.pid
            page
        } ?: DiaryPage(selectedDate, currentUserId)
    } as MutableLiveData<DiaryPage>

    var albumLive: MutableLiveData<MutableList<DayAlbum>> = Transformations.map(diaryPageLive) { page ->
        runBlocking {
            val album = albumDao.getAlbum(page.pid)
            album
        } ?: mutableListOf()
    } as MutableLiveData<MutableList<DayAlbum>>

    val pageContentLive: MutableLiveData<Content> = Transformations.map(diaryPageLive) {
        it.run { Content(content, ContentType.LOADED) }
    } as MutableLiveData<Content>

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            loadNewPage(LocalDate.now())
        }
    }

    fun loadNewPage(selectedDate: LocalDate) {
        insertAlbumInDbAndDispatchSaveFileTask?.let {
            if (it.isActive) {
                println("cordeb ins isactive")
                it.cancel()
            }

        }
        saveFileAndUpdateDbTask?.let {
            if (it.isActive) {
                println("cordeb ins isactive")
                it.cancel()
            }
        }

        pageId = null
        currentExternalImgList.value = mutableListOf()
        saveStatus.value = ""
        selectedDateLive.value = selectedDate
        persistExternalImageOnLoad()
    }

    fun loadPrevPage() {
        selectedDateLive.value?.minusDays(1)?.let {
            loadNewPage(it)
        }
    }

    fun loadNextPage() {
        selectedDateLive.value?.plusDays(1)?.let {
            loadNewPage(it)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun saveContent(typedContent: String, stoppedTyping: Boolean) {
        if (stoppedTyping) {
            coroutineScope.launch {
                pageContentLive.value = Content(typedContent, ContentType.TYPED)
                diaryPageLive.value?.run {
                    content = pageContentLive.value!!.text
                    pageId?.let {
                        diaryDao.updatePage(this)
                    } ?: run {
                        pageId = diaryDao.insertPage(this)
                        pid = pageId!!
                    }
                }
            }
            saveStatus.value = "Page Saved"
        } else updateSaveStatustoTyping()
    }

    fun saveImgsData(externalImgUriList: List<String>) {
        insertAlbumInDbAndDispatchSaveFileTask = coroutineScope.launch {
            saveExternalImgsToDb(externalImgUriList)
            displayImgsFromExternal(externalImgUriList)
            currentExternalImgList.value = externalImgUriList
        }
    }

    private suspend fun saveExternalImgsToDb(externalImgUriList: List<String>) {
        val dbImgUriList = mutableListOf<DayAlbum>()
        pageId ?: let {
            diaryPageLive.value?.run {
                pageId = diaryDao.insertPage(this)
                pid = pageId!!
            }
        }
        pageId?.let { pageId ->
            externalImgUriList.forEach {
                dbImgUriList.add(DayAlbum(pageId, it, ImageSource.EXTERNAL.name))
            }
        }
        albumDao.insertAllAlbum(dbImgUriList)
    }

    private fun persistExternalImageOnLoad() {
        saveFileAndUpdateDbTask = coroutineScope.launch {
            val externalAlbumList = albumDao.getExternalImgUriList(pageId, ImageSource.EXTERNAL.name)
            currentExternalImgList.value = externalAlbumList
        }
    }


    private fun displayImgsFromExternal(externalImgUriList: List<String>) {
        externalImgUriList.forEach {
            albumLive.value?.add(0, DayAlbum(pageId!!, it, ImageSource.EXTERNAL.name))
        }
        albumLive.notifyListChanged()
    }

    fun persistImgAndUpdateUI(internalDirectory: File, imgFlow: Flow<ByteArrayOutputStream>) {
        try {
            var count = 0
            saveFileAndUpdateDbTask = coroutineScope.launch {
                ensureActive()
                imgFlow.collect {
                    val newImgUri = saveImageInApp(internalDirectory, it)
                    updateImgUriInDb(currentExternalImgList.value!![count++], newImgUri)
                }
            }

        } catch (ex: CancellationException) {
            println("cordeb savefile cancel")
        }

    }

    private suspend fun updateImgUriInDb(oldImgUri: String, newImgUri: String) {
        pageId?.let {
            albumDao.updateUriAndSource(oldImgUri, newImgUri, ImageSource.INTERNAL.name)
        }
    }

    private fun <T> MutableLiveData<T>.notifyListChanged() {
        this.value = this.value
    }

    private suspend fun saveImageInApp(internalDirectory: File, byteArrayOutputStream: ByteArrayOutputStream) = withContext(Dispatchers.IO) {
        val savedImagePath = saveImageToInternalFile(internalDirectory, byteArrayOutputStream)
        savedImagePath
    }


    private fun saveImageToInternalFile(internalDirectory: File, byteArrayOutputStream: ByteArrayOutputStream): String {
        val imageName = getUniqueFileName()
        val filePathDirectory = internalDirectory.absolutePath + File.separator +
                currentUserId + selectedDateLive.value + File.separator
        val f = File(filePathDirectory)
        f.mkdirs()
        val file = File(f, imageName)
        val outputStream = FileOutputStream(file)
        byteArrayOutputStream.writeTo(outputStream)
        outputStream.close()
        byteArrayOutputStream.flush()
        return file.toUri().toString()
    }

    private fun getUniqueFileName() = UUID.randomUUID().toString() + ".jpg"

    private fun updateSaveStatustoTyping() {
        saveStatus.value = "Typing ..."
    }

}