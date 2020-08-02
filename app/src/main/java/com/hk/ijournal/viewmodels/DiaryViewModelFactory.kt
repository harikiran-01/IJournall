package com.hk.ijournal.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class DiaryViewModelFactory(private val application: Application, private val userId: Long) : ViewModelProvider.AndroidViewModelFactory(application) {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = DiaryViewModel(application, userId) as T
}