package com.hk.ijournal.viewmodels

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hk.ijournal.repository.AccessRepository
import com.hk.ijournal.repository.data.source.local.entities.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class RelayViewModel @Inject constructor(private val accessRepository: AccessRepository) : ViewModel() {

    enum class RequestCode {
        IMAGEADDED
    }

    val imagePickerClicked = ObservableBoolean(false)
    val onUserAuthorized: LiveData<User>
        get() = _onUserAuthorized
    var onSessionEnd = ObservableBoolean(false)

    val imageUriList: LiveData<List<Pair<String, String>>>
        get() = _imageUriList

    private val _onUserAuthorized = MutableLiveData<User>()
    private val _imageUriList = MutableLiveData<List<Pair<String, String>>>()

    fun onImagesPicked(imagesUriList: List<Pair<String, String>>) {
        _imageUriList.value = imagesUriList
    }

    fun onUserAuthorized(diaryUser: User) {
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

    fun resetImagesPickedLiveData() {
        _imageUriList.value = listOf()
    }

}