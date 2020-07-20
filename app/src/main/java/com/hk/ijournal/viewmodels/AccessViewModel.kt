package com.hk.ijournal.viewmodels

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hk.ijournal.repository.AccessRepository
import com.hk.ijournal.repository.AccessRepository.AccessDataResponse
import com.hk.ijournal.repository.models.AccessModel.AccessStatus
import com.hk.ijournal.repository.models.AccessModel.AccessValidation
import com.hk.ijournal.repository.models.DiaryUser
import java.time.LocalDate

class AccessViewModel(application: Application) : AndroidViewModel(application), AccessDataResponse {
    //username and passcode are two way binding variables
    //login livedata
    val loginUsernameLive: MutableLiveData<String>
    val loginPasscodeLive: MutableLiveData<String>

    val loginUserValidation: LiveData<AccessValidation>
        get() = _loginUserValidation
    val loginPasscodeValidation: LiveData<AccessValidation>
        get() = _loginPasscodeValidation

    private val _loginUserValidation: MutableLiveData<AccessValidation>
    private val _loginPasscodeValidation: MutableLiveData<AccessValidation>

    //register livedata
    val registerUsernameLive: MutableLiveData<String>
    val registerPasscodeLive: MutableLiveData<String>
    val dobLiveData: MutableLiveData<LocalDate>

    val registerUserValidation: LiveData<AccessValidation>
        get() = _registerUserValidation

    val registerPasscodeValidation: LiveData<AccessValidation>
        get() = _registerPasscodeValidation

    private val _registerUserValidation: MutableLiveData<AccessValidation>
    private val _registerPasscodeValidation: MutableLiveData<AccessValidation>

    private val accessRepository: AccessRepository

    val accessStatus: LiveData<AccessStatus>
        get() = _accessStatus

    private val _accessStatus: MutableLiveData<AccessStatus> = MutableLiveData()

    init {
        Log.d("lifecycle", "accessVM constructor")
        accessRepository = AccessRepository(application)
        //login binding
        loginUsernameLive = accessRepository.accessModel.loginUsernameLive
        loginPasscodeLive = accessRepository.accessModel.loginPasscodeLive
        _loginUserValidation = accessRepository.accessModel.getLoginUserValidation()
        _loginUserValidation.value = AccessValidation.USERNAME_INVALID
        _loginPasscodeValidation = accessRepository.accessModel.getLoginPasscodeValidation()
        _loginPasscodeValidation.value = AccessValidation.PASSCODE_INVALID
        //register binding
        registerUsernameLive = accessRepository.accessModel.registerUsernameLive
        registerPasscodeLive = accessRepository.accessModel.registerPasscodeLive
        _registerUserValidation = accessRepository.accessModel.getRegisterUserValidation()
        _registerUserValidation.value = AccessValidation.USERNAME_INVALID
        _registerPasscodeValidation = accessRepository.accessModel.getRegisterPasscodeValidation()
        _registerPasscodeValidation.value = AccessValidation.PASSCODE_INVALID
        dobLiveData = accessRepository.accessModel.dobLiveData
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    fun onDateSelected(year: Int, month: Int, dayOfMonth: Int) {
        accessRepository.setDobLiveData(LocalDate.of(year, month, dayOfMonth))
    }

    fun loginUser() {
        accessRepository.loginUserAndSendAccessData(this)
    }

    fun registerUser() {
        accessRepository.registerUserAndSendAccessData(this)
    }

    override fun onAccessDataReceived(dbUser: DiaryUser?) {
        _accessStatus.value = accessRepository.processAccessAndGetAccessStatus(dbUser)
    }

    override fun onCleared() {
        super.onCleared()
        accessRepository.closeDB()
        Log.d("lifecycle", "accessVM cleared")
    }
}