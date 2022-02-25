package com.hk.ijournal.common

import androidx.lifecycle.MutableLiveData

fun <T> MutableLiveData<T>.notifyListChanged() {
    this.value = this.value
}