package com.hk.ijournal.viewmodels

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hk.ijournal.repository.data.source.local.entities.DiaryUser

class RelayViewModel : ViewModel() {

    enum class RequestCode {
        IMAGEADDED
    }

    val imagePickerClicked = ObservableBoolean(false)
    val onUserAuthorized: LiveData<DiaryUser>
        get() = _onUserAuthorized
    var onSessionEnd = ObservableBoolean(false)

    val imageUriList: LiveData<List<String>>
        get() = _imageUriList

    private val _onUserAuthorized = MutableLiveData<DiaryUser>()
    private val _imageUriList = MutableLiveData<List<String>>()

    fun onImagesPicked(imagesUriList: List<String>) {
        _imageUriList.value = imagesUriList
    }

    fun onUserAuthorized(diaryUser: DiaryUser) {
        _onUserAuthorized.value = diaryUser
    }

}