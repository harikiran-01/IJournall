package com.hk.ijournal.feed.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.hk.ijournal.common.Constants
import com.hk.ijournal.domain.FeedUseCase
import com.hk.ijournal.repository.data.source.local.entities.Page
import com.hk.ijournal.repository.data.source.local.entities.User
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

    private val _allPages = MutableLiveData<List<Page>>(mutableListOf())
    val allPages: LiveData<List<Page>>
        get() = _allPages

    init {
        userId = savedStateHandle.get<User>(Constants.DIARY_USER)!!.uid
        getAllPages()
    }

    fun getAllPages() = runBlocking {
        _allPages.value = feedUseCase.getAllPages(userId)
    }

    fun resetAllPages() {
        _allPages.value = mutableListOf()
    }
}