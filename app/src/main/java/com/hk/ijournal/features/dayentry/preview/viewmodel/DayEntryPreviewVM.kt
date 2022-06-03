package com.hk.ijournal.features.dayentry.preview.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.hk.ijournal.common.Constants
import com.hk.ijournal.domain.PageUseCase
import com.hk.ijournal.features.dayentry.models.Page
import com.hk.ijournal.features.dayentry.models.content.BaseEntity
import com.hk.ijournal.features.dayentry.models.content.CONTENT_TEXT
import com.hk.ijournal.features.dayentry.models.content.TextContent
import com.hk.ijournal.repository.data.source.local.entities.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class DayEntryPreviewVM @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val pageUseCase: PageUseCase) : ViewModel() {
    companion object {
        private const val NEW_PAGE = 0L
    }
    private var userId = 0L

    private val _pageIdLive = MutableLiveData<Long>()

    private val _currentPage = Transformations.map(_pageIdLive) {
        return@map if (it == NEW_PAGE) Page(LocalDate.now(),
            userId, "",
            listOf(BaseEntity(CONTENT_TEXT, TextContent("", "#A02B55"))))
        else runBlocking { return@runBlocking pageUseCase.getPageForId(it) }
    } as MutableLiveData<Page>

    val currentPage : LiveData<Page>
        get() = _currentPage

    private val _selectedDateLive = Transformations.map(_currentPage) {
         return@map it.selectedDate
    } as MutableLiveData<LocalDate>

    val selectedDateLive: LiveData<LocalDate>
        get() = _selectedDateLive

    init {
        userId = savedStateHandle.get<User>(Constants.DIARY_USER)!!.uid
    }

    fun navigateToPrevPage() {
        _selectedDateLive.value?.minusDays(1)?.let {
            navigateToSelectedPage(it)
            _currentPage.value?.selectedDate = it
        }
    }

    fun navigateToNextPage() {
        selectedDateLive.value?.plusDays(1)?.let {
            navigateToSelectedPage(it)
            _currentPage.value?.selectedDate = it
        }
    }

    fun navigateToSelectedPage(selectedDate: LocalDate) {
        _selectedDateLive.value = selectedDate
    }

    fun loadPage(pageId: Long) {
        _pageIdLive.value = pageId
    }

}