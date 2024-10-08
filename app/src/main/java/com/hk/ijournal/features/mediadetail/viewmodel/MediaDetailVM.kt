package com.hk.ijournal.features.mediadetail.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MediaDetailVM @Inject constructor(
    private val savedStateHandle: SavedStateHandle) : ViewModel() {
}