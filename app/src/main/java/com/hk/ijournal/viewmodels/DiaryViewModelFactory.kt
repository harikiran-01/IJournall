package com.hk.ijournal.viewmodels

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hk.ijournal.common.Constants


class DiaryViewModelFactory(private val application: Application, private val savedStateHandle: SavedStateHandle) : ViewModelProvider.AndroidViewModelFactory(application) {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = savedStateHandle.get<Long>(Constants.USER_ID)
        ?.let { DiaryViewModel(application, it) } as T
}