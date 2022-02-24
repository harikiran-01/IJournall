package com.hk.ijournal.viewmodels

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.hk.ijournal.repository.DiaryRepository
import com.hk.ijournal.repository.data.source.local.IJDatabase
import kotlinx.coroutines.flow.Flow
import java.io.ByteArrayOutputStream
import java.io.File
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
class DiaryViewModel(application: Application, userId: Long) : AndroidViewModel(application) {
    private val ijDatabase: IJDatabase

    val diaryRepository: DiaryRepository

    //page livedata
    val selectedDateLive: LiveData<LocalDate>
        get() = diaryRepository.selectedDateLive

    val saveStatus: LiveData<String>
        get() = diaryRepository.saveStatus

    init {
        ijDatabase = IJDatabase.getDatabase(application.applicationContext)
        diaryRepository = DiaryRepository(ijDatabase.getDiaryPageDao(), userId, viewModelScope)
        //bind
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