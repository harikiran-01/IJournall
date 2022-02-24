package com.hk.ijournal.viewmodels

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.hk.ijournal.common.CommonLib.LOGTAG
import com.hk.ijournal.repository.AccessRepository
import com.hk.ijournal.repository.AccessRepositoryImpl
import com.hk.ijournal.repository.AccessRepositoryImpl.LoginStatus
import com.hk.ijournal.repository.data.source.local.entities.DiaryUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AccessViewModel @Inject constructor(private val accessRepositoryImpl: AccessRepository) : ViewModel() {

    var uid = 0L
    //login livedata
    val loginUsernameLive: MutableLiveData<String> = MutableLiveData()
    val loginPasscodeLive: MutableLiveData<String> = MutableLiveData()

    //register livedata
    val registerUsernameLive: MutableLiveData<String> = MutableLiveData()
    val registerPasscodeLive: MutableLiveData<String> = MutableLiveData()
    val dobLiveData: MutableLiveData<LocalDate> = MutableLiveData()

    //access livedata
    val loginStatus: MutableLiveData<LoginStatus> = MutableLiveData()
    val registerStatus: MutableLiveData<AccessRepositoryImpl.RegisterStatus> = MutableLiveData()

    fun getLoginUserValidation(): LiveData<AccessRepositoryImpl.AccessValidation> {
        //transforming based on username live data
        return Transformations.map(loginUsernameLive) {
            return@map if (accessRepositoryImpl.isUsernameInvalid(it)) AccessRepositoryImpl.AccessValidation.USERNAME_INVALID
            else
                null
        }
    }

    fun getLoginPasscodeValidation(): LiveData<AccessRepositoryImpl.AccessValidation> {
        //transforming based on passcode live data
        return Transformations.map(loginPasscodeLive) {
            return@map if (accessRepositoryImpl.isPassCodeInvalid(it)) AccessRepositoryImpl.AccessValidation.PASSCODE_INVALID
            else
                null
        }
    }

    fun getRegisterUserValidation(): LiveData<AccessRepositoryImpl.AccessValidation> {
        //transforming based on username live data
        return Transformations.map(registerUsernameLive) {
            return@map if (accessRepositoryImpl.isUsernameInvalid(it)) AccessRepositoryImpl.AccessValidation.USERNAME_INVALID
            else
                null
        }
    }

    fun getRegisterPasscodeValidation(): LiveData<AccessRepositoryImpl.AccessValidation> {
        //transforming based on passcode live data
        return Transformations.map(registerPasscodeLive) {
            return@map if (accessRepositoryImpl.isPassCodeInvalid(it)) AccessRepositoryImpl.AccessValidation.PASSCODE_INVALID
            else
                null
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    fun onDateSelected(year: Int, month: Int, dayOfMonth: Int) {
        dobLiveData.value = LocalDate.of(year, month + 1, dayOfMonth)
    }

    fun loginUser() = viewModelScope.launch {
        val diaryUser = DiaryUser(loginUsernameLive.value.toString(), loginPasscodeLive.value.toString())
        val dbUser = accessRepositoryImpl.getMatchingUserinDb(loginUsernameLive.value.toString())
        dbUser.let {
            if (it.isSuccess) {
                uid = it.getOrNull()?.uid ?: 0
                loginStatus.value = accessRepositoryImpl.processLoginAndGetAccessStatus(it.getOrNull(), diaryUser)
            }
            else
                loginStatus.value = accessRepositoryImpl.processLoginAndGetAccessStatus(null, diaryUser)
        }
    }

    fun registerUser() = viewModelScope.launch {
        var regStatus: AccessRepositoryImpl.RegisterStatus = AccessRepositoryImpl.RegisterStatus.USER_ALREADY_EXISTS
        val dbUser = accessRepositoryImpl.getMatchingUserinDb(registerUsernameLive.value.toString())
        dbUser.let {
            regStatus = if (it.isSuccess) {
                accessRepositoryImpl.processRegisterAndGetAccessStatus(it.getOrNull())
            } else
                accessRepositoryImpl.processRegisterAndGetAccessStatus(null)
        }
        println("regstatus $regStatus")
        if (regStatus == AccessRepositoryImpl.RegisterStatus.REGISTER_SUCCESSFULL)
            uid = accessRepositoryImpl.insertUserInDbAndGetRowId(DiaryUser(registerUsernameLive.value.toString(),registerPasscodeLive.value.toString(), dobLiveData.value))
            if (uid > 0L)
                registerStatus.value = regStatus
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(LOGTAG, "AccessVM Cleared")
    }
}