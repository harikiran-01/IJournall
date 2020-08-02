package com.hk.ijournal.viewmodels

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hk.ijournal.repository.DiaryRepository
import com.hk.ijournal.repository.local.IJDatabase
import java.time.LocalDate

class DiaryViewModel(application: Application, userId: Long) : AndroidViewModel(application) {
    private val ijDatabase: IJDatabase

    //page livedata
    private val _selectedDateLive: MutableLiveData<LocalDate>
    val selectedDateLive: LiveData<LocalDate>
        get() = _selectedDateLive

    val saveStatus: LiveData<String>
        get() = _saveStatus

    private val _saveStatus: MutableLiveData<String>

    val pageContent: LiveData<String>
        get() = _pageContent

    private val _pageContent: MutableLiveData<String>

    private val diaryRepository: DiaryRepository

    init {
        ijDatabase = IJDatabase.getDatabase(application.applicationContext)
        diaryRepository = DiaryRepository(ijDatabase.diaryDao(), userId, viewModelScope)
        //bind
        _selectedDateLive = diaryRepository.selectedDateLive
        _pageContent = diaryRepository.pageContentLive
        _saveStatus = diaryRepository.saveStatus
        Log.d("lifecycle", "diaryVM onCreate")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun navigateToPrevPage() {
        diaryRepository.selectPrevDate()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun navigateToNextPage() {
        diaryRepository.selectNextDate()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun postContent(typedContent: String, stoppedTyping: Boolean) {
        diaryRepository.savePage(typedContent, stoppedTyping)
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("lifecycle", "diaryVM onCleared")
    }
}