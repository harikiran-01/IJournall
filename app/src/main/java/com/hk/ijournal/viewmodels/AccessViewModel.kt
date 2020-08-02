package com.hk.ijournal.viewmodels

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hk.ijournal.repository.AccessRepository
import com.hk.ijournal.repository.AccessRepository.AccessValidation
import com.hk.ijournal.repository.AccessRepository.LoginStatus
import com.hk.ijournal.repository.local.IJDatabase
import java.time.LocalDate

class AccessViewModel(application: Application) : AndroidViewModel(application) {
    private val ijDatabase: IJDatabase
    private val accessRepository: AccessRepository

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

    val loginStatus: LiveData<LoginStatus>
        get() = _loginStatus

    private val _loginStatus: MutableLiveData<LoginStatus>

    val registerStatus: LiveData<AccessRepository.RegisterStatus>
        get() = _registerStatus

    private val _registerStatus: MutableLiveData<AccessRepository.RegisterStatus>

    val accessStatus: LiveData<AccessRepository.AccessStatus>
        get() = _accessStatus

    private val _accessStatus: MutableLiveData<AccessRepository.AccessStatus>

    init {
        Log.d("lifecycle", "accessVM constructor")
        ijDatabase = IJDatabase.getDatabase(application.applicationContext)
        accessRepository = AccessRepository(ijDatabase.userDao(), viewModelScope)
        //login binding
        loginUsernameLive = accessRepository.loginUsernameLive
        loginPasscodeLive = accessRepository.loginPasscodeLive
        _loginUserValidation = accessRepository.getLoginUserValidation()
        _loginUserValidation.value = AccessValidation.USERNAME_INVALID
        _loginPasscodeValidation = accessRepository.getLoginPasscodeValidation()
        _loginPasscodeValidation.value = AccessValidation.PASSCODE_INVALID
        //register binding
        registerUsernameLive = accessRepository.registerUsernameLive
        registerPasscodeLive = accessRepository.registerPasscodeLive
        _registerUserValidation = accessRepository.getRegisterUserValidation()
        _registerUserValidation.value = AccessValidation.USERNAME_INVALID
        _registerPasscodeValidation = accessRepository.getRegisterPasscodeValidation()
        _registerPasscodeValidation.value = AccessValidation.PASSCODE_INVALID
        dobLiveData = accessRepository.dobLiveData
        _loginStatus = accessRepository.loginStatus
        _registerStatus = accessRepository.registerStatus
        _accessStatus = accessRepository.accessStatus
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    fun onDateSelected(year: Int, month: Int, dayOfMonth: Int) {
        accessRepository.setDobLiveData(LocalDate.of(year, month, dayOfMonth))
    }

    fun loginUser() = accessRepository.loginUserAndUpdateAccessStatus()

    fun registerUser() = accessRepository.registerUserAndUpdateAccessStatus()

    fun getUid(): Long = accessRepository.uid

    override fun onCleared() {
        super.onCleared()
        closeDB()
        Log.d("lifecycle", "accessVM cleared")
    }

    private fun closeDB() {
        ijDatabase.close()
    }
}