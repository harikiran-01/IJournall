package com.hk.ijournal.feed.viewmodel

import androidx.lifecycle.*
import com.hk.ijournal.common.Constants
import com.hk.ijournal.domain.FeedUseCase
import com.hk.ijournal.repository.data.source.local.entities.DiaryPage
import com.hk.ijournal.repository.data.source.local.entities.DiaryUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class LandingViewModel @Inject
constructor(
    private val savedStateHandle: SavedStateHandle,
    private val feedUseCase: FeedUseCase
) : ViewModel() {
    private var userId: Long = 0

    init {
        userId = savedStateHandle.get<DiaryUser>(Constants.DIARY_USER)!!.uid
        getAllPages()
    }
    private val _allPages = MutableLiveData<List<DiaryPage>>()
    val allPages: LiveData<List<DiaryPage>>
        get() = _allPages

    private fun getAllPages() = runBlocking {
        viewModelScope.launch {
            println("DEBDEB ${feedUseCase.getAllPages(userId)}")
            _allPages.value = feedUseCase.getAllPages(userId)
            print("DEBDEB ${_allPages.value}")
        }
    }
}