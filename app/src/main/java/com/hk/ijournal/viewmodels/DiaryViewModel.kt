package com.hk.ijournal.viewmodels

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hk.ijournal.repository.DiaryRepository
import com.hk.ijournal.repository.local.IJDatabase
import com.hk.ijournal.repository.models.Content
import com.hk.ijournal.repository.models.DayAlbum
import kotlinx.coroutines.flow.Flow
import java.io.ByteArrayOutputStream
import java.io.File
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
class DiaryViewModel(application: Application, userId: Long) : AndroidViewModel(application) {
    private val ijDatabase: IJDatabase

    //page livedata
    private val _selectedDateLive: MutableLiveData<LocalDate>
    val selectedDateLive: LiveData<LocalDate>
        get() = _selectedDateLive

    val saveStatus: LiveData<String>
        get() = _saveStatus

    private val _saveStatus: MutableLiveData<String>

    val _pageContent: MutableLiveData<Content>

    val dayAlbumLive: MutableLiveData<MutableList<DayAlbum>>

    val currentExternalImgList: MutableLiveData<List<String>>

    private val diaryRepository: DiaryRepository

    init {
        ijDatabase = IJDatabase.getDatabase(application.applicationContext)
        diaryRepository = DiaryRepository(ijDatabase.getDiaryPageDao(), userId, viewModelScope)
        //bind
        _selectedDateLive = diaryRepository.selectedDateLive
        _pageContent = diaryRepository.pageContentLive
        _saveStatus = diaryRepository.saveStatus
        dayAlbumLive = diaryRepository.albumLive
        currentExternalImgList = diaryRepository.currentExternalImgList
        println("lifecycled diaryVM onCreate")
    }

    fun navigateToPrevPage() {
        diaryRepository.loadPrevPage()
    }

    fun navigateToNextPage() {
        diaryRepository.loadNextPage()
    }

    fun postContent(typedContent: String, stoppedTyping: Boolean) {
        diaryRepository.saveContent(typedContent, stoppedTyping)
    }

    override fun onCleared() {
        println("lifecycled diaryVM onCreate")
        super.onCleared()
    }

    fun saveImagesData(externalImgUriList: List<String>) {
        diaryRepository.saveImgsData(externalImgUriList)
    }

    fun sendStreamFlow(internalDirectory: File, flow: Flow<ByteArrayOutputStream>) {
        diaryRepository.persistImgAndUpdateUI(internalDirectory, flow)
    }

    fun navigateToSelectedPage(selectedDate: LocalDate) {
        diaryRepository.loadNewPage(selectedDate)
    }
}