package com.hk.ijournal.dayentry.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.hk.ijournal.common.Constants
import com.hk.ijournal.common.base.BaseAdapterViewType
import com.hk.ijournal.common.base.ITEM_DAY_IMAGE
import com.hk.ijournal.common.base.ITEM_DAY_TEXT
import com.hk.ijournal.dayentry.models.CONTENT_IMAGE
import com.hk.ijournal.dayentry.models.CONTENT_TEXT
import com.hk.ijournal.dayentry.models.Page
import com.hk.ijournal.dayentry.models.content.BaseEntity
import com.hk.ijournal.dayentry.models.content.ContentData
import com.hk.ijournal.dayentry.models.content.ImageContent
import com.hk.ijournal.dayentry.models.content.TextContent
import com.hk.ijournal.domain.PageUseCase
import com.hk.ijournal.repository.data.source.local.entities.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class DayEntryViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val pageUseCase: PageUseCase) : ViewModel() {
    private var userId = 0L
    private var previewPageId = 0L

    private val _pageIdLive = MutableLiveData<Long>()

    private val _selectedDateLive = MutableLiveData<LocalDate>()
    val selectedDateLive: LiveData<LocalDate>
        get() = _selectedDateLive

    private val _currentPage = Transformations.map(_pageIdLive) {
        return@map if (it == 0L) Page(LocalDate.now(),
            userId, "",
            listOf(BaseEntity(CONTENT_TEXT, TextContent("", "#A02B55"))))
        else runBlocking { return@runBlocking pageUseCase.getPageForId(it) }
    } as MutableLiveData<Page>

    init {
        userId = savedStateHandle.get<User>(Constants.DIARY_USER)!!.uid
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            navigateToSelectedPage(LocalDate.now())
        }
        previewPageId = savedStateHandle.get<Long>(Constants.PAGE_ID)!!
        _pageIdLive.value = previewPageId
    }

    val currentPage : LiveData<Page>
        get() = _currentPage

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

    fun savePage(pageTitle: String, contentList: List<BaseAdapterViewType>) {
        val contentListWithType = contentList.map { when(it.viewType){
            ITEM_DAY_TEXT -> BaseEntity<ContentData>(CONTENT_TEXT, it as TextContent)
            ITEM_DAY_IMAGE -> BaseEntity<ContentData>(CONTENT_IMAGE, it as ImageContent)
            else -> BaseEntity<ContentData>(CONTENT_TEXT, it as TextContent)
        } }
        viewModelScope.launch {
            _currentPage.value?.apply {
                title = pageTitle
                this.contentList = contentListWithType
            }
            _currentPage.value?.let {
                pageUseCase.insertPage(it)
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
        _selectedDateLive.value = selectedDate
    }

}