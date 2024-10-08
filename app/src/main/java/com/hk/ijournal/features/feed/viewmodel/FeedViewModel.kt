package com.hk.ijournal.features.feed.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.hk.ijournal.features.dayentry.models.Page
import com.hk.ijournal.features.feed.usecases.FeedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject
constructor(
    savedStateHandle: SavedStateHandle,
    private val feedUseCase: FeedUseCase
) : ViewModel() {
    private var userId: Long = 0

    private val _allPages = MutableLiveData<List<Page>>(mutableListOf())
    val allPages: LiveData<List<Page>>
        get() = _allPages

    init {
//        userId = savedStateHandle.get<User>(Constants.DIARY_USER)!!.uid
    }

    fun getAllPages() = runBlocking {
        _allPages.value = feedUseCase.getAllPages(userId)
    }

    fun resetAllPages() {
        _allPages.value = emptyList()
    }
}