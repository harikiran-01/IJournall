package com.hk.ijournal.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {
    val userIdLive: LiveData<Long>
        get() = _userIdLive
    private val _userIdLive: MutableLiveData<Long> = MutableLiveData()

    fun setUserId(userId: Long) {
        _userIdLive.value = userId
    }

    init {
        Log.d("lifecycle", "homeVM onCreate")
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("lifecycle", "homeVM onClear")
    }

}