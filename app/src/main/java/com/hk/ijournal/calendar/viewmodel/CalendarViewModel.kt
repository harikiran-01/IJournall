package com.hk.ijournal.calendar.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.hk.ijournal.domain.PageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val pageUseCase: PageUseCase
): ViewModel() {

}