package com.hk.ijournal.dayentry.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.hk.ijournal.common.Constants
import com.hk.ijournal.dayentry.models.PageContentModel
import com.hk.ijournal.domain.PageUseCase
import com.hk.ijournal.repository.data.source.local.entities.Page
import com.hk.ijournal.repository.data.source.local.entities.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class DayEntryViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val pageUseCase: PageUseCase) : ViewModel() {
    var pageId: Long? = null
    private var userId: Long = 0
    private val _selectedDateLive = MutableLiveData<LocalDate>()
    init {
        userId = savedStateHandle.get<User>(Constants.DIARY_USER)!!.uid
    }

    private var diaryPageLive: MutableLiveData<Page> = Transformations.map(selectedDateLive) { selectedDate ->
        runBlocking {
            var page = pageUseCase.getPageforDate(selectedDate, userId)
            page?.let {
                pageId = it.pid
            } ?: kotlin.run {
                page = Page(selectedDate, userId, "", listOf())
            }
            page
        }
    } as MutableLiveData<Page>

    val pageTitleLive: MutableLiveData<String> = Transformations.map(diaryPageLive) {
        it.title
    } as MutableLiveData<String>

    val pageContentLive: MutableLiveData<List<PageContentModel>> = Transformations.map(diaryPageLive) {
        it.run { it.contentList }
    } as MutableLiveData<List<PageContentModel>>

    //exposed livedata
    val selectedDateLive: LiveData<LocalDate>
        get() = _selectedDateLive

    private val currentExternalImgList = MutableLiveData<List<String>>()

    private var saveFileAndUpdateDbTask: Job? = null
    private var insertAlbumInDbAndDispatchSaveFileTask: Job? = null

    private fun cancelPendingJobs() {
        insertAlbumInDbAndDispatchSaveFileTask?.cancel()
        saveFileAndUpdateDbTask?.cancel()
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

    fun savePage(pageTitle: String, contentList: List<PageContentModel>) {
        viewModelScope.launch {
            pageTitleLive.value = pageTitle
            diaryPageLive.value?.run {
                title = pageTitleLive.value!!
                this.contentList = contentList
                pageId?.let {
                    pageUseCase.updatePage(this)
                } ?: run {
                    pageId = pageUseCase.insertPage(this)
                    pid = pageId!!
                }
            }
        }
    }

//    fun saveImagesAsAlbum(images: List<String>) {
//        insertAlbumInDbAndDispatchSaveFileTask = viewModelScope.launch {
//            currentExternalImgList.value = images
//            diaryPageLive.value?.let { page ->
//                currentExternalImgList.value?.let {
//                    pageId = pageId?: pageUseCase.insertPage(page)
//                    page.pid = pageId!!
//                    val newAlbum = albumUseCase.saveImgsToDbAsAlbum(page.pid, it).reversed().plus(albumLive.value as List<DayAlbum>)
//                    _albumLive.value = newAlbum
//                } }
//        }
//    }

//    private fun persistExternalImageOnLoad() {
//        saveFileAndUpdateDbTask = viewModelScope.launch {
//            val externalAlbumList = albumUseCase.getExternalImgUriList(pageId, ImageSource.EXTERNAL.name)
//            currentExternalImgList.value = externalAlbumList
//        }
//    }
//
//    private fun persistImgAndUpdateUI(internalDirectory: File, imgFlow: Flow<ByteArrayOutputStream>) {
//        try {
//            var count = 0
//            saveFileAndUpdateDbTask = viewModelScope.launch {
//                ensureActive()
//                imgFlow.collect {
//                    val newImgUri = selectedDateLive.value?.let { it1 ->
//                        albumUseCase.saveImageInApp(userId,
//                            it1, internalDirectory, it)
//                    }
//                    if (newImgUri != null) {
//                        albumUseCase.updateImgUriInDb(currentExternalImgList.value!![count++], newImgUri)
//                    }
//                }
//            }
//        } catch (ex: CancellationException) {
//            println("cordeb savefile cancel")
//        }
//    }

//    fun sendStreamFlow(internalDirectory: File, flow: Flow<ByteArrayOutputStream>) {
//        persistImgAndUpdateUI(internalDirectory, flow)
//    }

    fun navigateToSelectedPage(selectedDate: LocalDate) {
        cancelPendingJobs()
        pageId = null
        currentExternalImgList.value = mutableListOf()
        _selectedDateLive.value = selectedDate
    }

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            navigateToSelectedPage(LocalDate.now())
        }
    }

}