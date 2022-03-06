package com.hk.ijournal.feed.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.hk.ijournal.common.Constants
import com.hk.ijournal.domain.FeedUseCase
import com.hk.ijournal.repository.data.source.local.entities.DiaryPage
import com.hk.ijournal.repository.data.source.local.entities.DiaryUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject
constructor(
    private val savedStateHandle: SavedStateHandle,
    private val feedUseCase: FeedUseCase
) : ViewModel() {
    private var userId: Long = 0

    private val _allPages = MutableLiveData<List<DiaryPage>>(mutableListOf())
    val allPages: LiveData<List<DiaryPage>>
        get() = _allPages

    init {
        userId = savedStateHandle.get<DiaryUser>(Constants.DIARY_USER)!!.uid
        getAllPages()
    }

    fun getAllPages() = runBlocking {
        Log.d("DEBDEB", "${feedUseCase.getAllPages(userId)}")
        _allPages.value = feedUseCase.getAllPages(userId)
    }

    fun resetAllPages() {
        _allPages.value = mutableListOf()
    }
}