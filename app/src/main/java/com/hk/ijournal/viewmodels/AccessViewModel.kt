package com.hk.ijournal.viewmodels

import android.app.Application
import android.os.Build
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
    val loginPasscodeValidation: LiveData<AccessValidation>


    //register livedata
    val registerUsernameLive: MutableLiveData<String>
    val registerPasscodeLive: MutableLiveData<String>
    val dobLiveData: MutableLiveData<LocalDate>

    val registerUserValidation: LiveData<AccessValidation>
    val registerPasscodeValidation: LiveData<AccessValidation>

    val loginStatus: LiveData<LoginStatus>
        get() = _loginStatus

    private val _loginStatus: MutableLiveData<LoginStatus>

    val registerStatus: LiveData<AccessRepository.RegisterStatus>
        get() = _registerStatus

    private val _registerStatus: MutableLiveData<AccessRepository.RegisterStatus>

    init {
        ijDatabase = IJDatabase.getDatabase(application.applicationContext)
        accessRepository = AccessRepository(ijDatabase.userDao(), viewModelScope)
        //login binding
        loginUsernameLive = accessRepository.loginUsernameLive
        loginPasscodeLive = accessRepository.loginPasscodeLive
        loginUserValidation = accessRepository.getLoginUserValidation()
        loginPasscodeValidation = accessRepository.getLoginPasscodeValidation()
        //register binding
        registerUsernameLive = accessRepository.registerUsernameLive
        registerPasscodeLive = accessRepository.registerPasscodeLive
        registerUserValidation = accessRepository.getRegisterUserValidation()
        registerPasscodeValidation = accessRepository.getRegisterPasscodeValidation()
        dobLiveData = accessRepository.dobLiveData
        _loginStatus = accessRepository.loginStatus
        _registerStatus = accessRepository.registerStatus
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    fun onDateSelected(year: Int, month: Int, dayOfMonth: Int) {
        accessRepository.setDobLiveData(LocalDate.of(year, month, dayOfMonth))
    }

    fun loginUser() = accessRepository.loginUserAndUpdateAccessStatus()

    fun registerUser() = accessRepository.registerUserAndUpdateAccessStatus()

    fun getUid(): Long = accessRepository.uid
}