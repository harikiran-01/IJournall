package com.hk.ijournal.viewmodels

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class DiaryViewModelFactory(private val application: Application, private val userId: Long) : ViewModelProvider.AndroidViewModelFactory(application) {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = DiaryViewModel(application, userId) as T
}