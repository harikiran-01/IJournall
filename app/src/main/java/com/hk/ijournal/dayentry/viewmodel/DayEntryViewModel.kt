package com.hk.ijournal.dayentry.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.hk.ijournal.common.Constants
import com.hk.ijournal.dayentry.models.PageContentModel
import com.hk.ijournal.dayentry.models.TextModel
import com.hk.ijournal.domain.PageUseCase
import com.hk.ijournal.repository.data.source.local.entities.Page
import com.hk.ijournal.repository.data.source.local.entities.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class DayEntryViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val pageUseCase: PageUseCase) : ViewModel() {
    private var userId = 0L

    private val _selectedDateLive = MutableLiveData<LocalDate>()
    val selectedDateLive: LiveData<LocalDate>
        get() = _selectedDateLive

    init {
        userId = savedStateHandle.get<User>(Constants.DIARY_USER)!!.uid
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            navigateToSelectedPage(LocalDate.now())
        }
    }

    private val _currentPage = Transformations.map(selectedDateLive) {
        return@map Page(it, userId, "", arrayListOf(TextModel("", "#A02B55")))
    } as MutableLiveData<Page>

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

    fun savePage(pageTitle: String, contentList: List<PageContentModel>) {
        viewModelScope.launch {
            _currentPage.value?.apply {
                title = pageTitle
                this.contentList = contentList
            }
            Log.d("DEBDEB", "${currentPage.value}")
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