package com.hk.ijournal.viewmodels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.hk.ijournal.common.Constants
import com.hk.ijournal.domain.PageUseCase
import com.hk.ijournal.domain.AlbumUseCase
import com.hk.ijournal.repository.data.source.local.entities.DayAlbum
import com.hk.ijournal.repository.data.source.local.entities.DiaryPage
import com.hk.ijournal.repository.data.source.local.entities.DiaryUser
import com.hk.ijournal.repository.data.source.local.entities.ImageSource
import com.hk.ijournal.repository.models.Content
import com.hk.ijournal.repository.models.ContentType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import java.io.ByteArrayOutputStream
import java.io.File
import java.time.LocalDate
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class DiaryViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val pageUseCase: PageUseCase, private val albumUseCase: AlbumUseCase) : ViewModel() {
    private var pageId: Long? = null
    private var userId: Long = 0
    private val _saveStatus = MutableLiveData<String>()
    private val _selectedDateLive = MutableLiveData<LocalDate>()
    init {
        print("DEBDEB ${savedStateHandle.get<DiaryUser>(Constants.DIARY_USER)!!}")
        userId = savedStateHandle.get<DiaryUser>(Constants.DIARY_USER)!!.uid
    }

    private var diaryPageLive: MutableLiveData<DiaryPage> = Transformations.map(selectedDateLive) { selectedDate ->
        runBlocking {
            var page = pageUseCase.getPageforDate(selectedDate, userId)
            page?.let {
                pageId = it.pid
            } ?: kotlin.run {
                page = DiaryPage(selectedDate, savedStateHandle.get<DiaryUser>(Constants.DIARY_USER)!!.uid, "", 0)
            }
            page
        }
    } as MutableLiveData<DiaryPage>

    var albumLive: MutableLiveData<MutableList<DayAlbum>> = Transformations.map(diaryPageLive) { page ->
        runBlocking {
            val album = albumUseCase.getAlbum(page.pid)
            album
        } ?: mutableListOf()
    } as MutableLiveData<MutableList<DayAlbum>>

    val pageContentLive: MutableLiveData<Content> = Transformations.map(diaryPageLive) {
        it.run { Content(content, ContentType.LOADED) }
    } as MutableLiveData<Content>

    //exposed livedata
    val selectedDateLive: LiveData<LocalDate>
        get() = _selectedDateLive

    val saveStatus: LiveData<String>
        get() = _saveStatus

    private val currentExternalImgList = MutableLiveData<List<String>>()

    private var saveFileAndUpdateDbTask: Job? = null
    private var insertAlbumInDbAndDispatchSaveFileTask: Job? = null

    private fun cancelPendingJobs() {
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
        // persistExternalImageOnLoad()
    }


    fun navigateToPrevPage() {
        selectedDateLive.value?.minusDays(1)?.let {
            navigateToSelectedPage(it)
        }
    }

    fun navigateToNextPage() {
        selectedDateLive.value?.plusDays(1)?.let {
            navigateToSelectedPage(it)
        }
    }

    fun postContent(typedContent: String, stoppedTyping: Boolean) {
        if (stoppedTyping) {
            viewModelScope.launch {
                pageContentLive.value = Content(typedContent, ContentType.TYPED)
                diaryPageLive.value?.run {
                    content = pageContentLive.value!!.text
                    pageId?.let {
                        pageUseCase.updatePage(this)
                    } ?: run {
                        pageId = pageUseCase.insertPage(this)
                        pid = pageId!!
                    }
                }
            }
            _saveStatus.value = "Page Saved"
        } else _saveStatus.value = "Typing ..."
    }

    override fun onCleared() {
        println("lifecycled diaryVM onCreate")
        super.onCleared()
    }

    fun saveImagesAsAlbum(images: List<String>) {
        insertAlbumInDbAndDispatchSaveFileTask = viewModelScope.launch {
            currentExternalImgList.value = images
            diaryPageLive.value?.let { page ->
                currentExternalImgList.value?.let {
                    albumLive.value = albumUseCase.saveImgsToDbAsAlbum(page, it)
                } }
        }
    }

    private fun persistExternalImageOnLoad() {
        saveFileAndUpdateDbTask = viewModelScope.launch {
            val externalAlbumList = albumUseCase.getExternalImgUriList(pageId, ImageSource.EXTERNAL.name)
            currentExternalImgList.value = externalAlbumList
        }
    }

    private fun persistImgAndUpdateUI(internalDirectory: File, imgFlow: Flow<ByteArrayOutputStream>) {
        try {
            var count = 0
            saveFileAndUpdateDbTask = viewModelScope.launch {
                ensureActive()
                imgFlow.collect {
                    val newImgUri = selectedDateLive.value?.let { it1 ->
                        albumUseCase.saveImageInApp(savedStateHandle.get<DiaryUser>(Constants.DIARY_USER)!!.uid,
                            it1, internalDirectory, it)
                    }
                    if (newImgUri != null) {
                        albumUseCase.updateImgUriInDb(currentExternalImgList.value!![count++], newImgUri)
                    }
                }
            }

        } catch (ex: CancellationException) {
            println("cordeb savefile cancel")
        }

    }

    fun sendStreamFlow(internalDirectory: File, flow: Flow<ByteArrayOutputStream>) {
        persistImgAndUpdateUI(internalDirectory, flow)
    }

    fun navigateToSelectedPage(selectedDate: LocalDate) {
        cancelPendingJobs()
        pageId = null
        currentExternalImgList.value = mutableListOf()
        _saveStatus.value = ""
        _selectedDateLive.value = selectedDate
    }

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            navigateToSelectedPage(LocalDate.now())
        }
    }
}