package com.hk.ijournal.viewmodels

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hk.ijournal.repository.AccessRepository
import com.hk.ijournal.repository.data.source.local.entities.DiaryUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class RelayViewModel @Inject constructor(private val accessRepository: AccessRepository) : ViewModel() {

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

    fun getUser(uid: Long) = runBlocking {
        val userJob = async { accessRepository.getUser(uid) }
        val userResult = userJob.await()
        if (userResult.isSuccess)
            return@runBlocking userResult.getOrNull()
        else
            return@runBlocking null
    }

}