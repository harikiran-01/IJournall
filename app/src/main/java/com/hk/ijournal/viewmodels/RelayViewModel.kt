package com.hk.ijournal.viewmodels

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RelayViewModel : ViewModel() {

    enum class RequestCode {
        IMAGEADDED
    }

    val imagePickerClicked = ObservableBoolean(false)
    val isAccessAuthorized = ObservableBoolean(false)
    var onSessionEnd = ObservableBoolean(false)

    val imageUriCategory: LiveData<Boolean>
        get() = _imageUriCategory

    private val _imageUriCategory = MutableLiveData<Boolean>()

    fun onImagePicked() {
        _imageUriCategory.value = true
    }

}