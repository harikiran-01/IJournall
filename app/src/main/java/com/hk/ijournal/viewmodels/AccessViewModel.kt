package com.hk.ijournal.viewmodels

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.hk.ijournal.common.CommonLib.LOGTAG
import com.hk.ijournal.repository.AccessRepository
import com.hk.ijournal.repository.AccessRepository.LoginStatus
import com.hk.ijournal.repository.local.IJDatabase
import java.time.LocalDate

class AccessViewModel(application: Application) : AndroidViewModel(application) {
    private val ijDatabase: IJDatabase
    val accessRepository: AccessRepository

    val loginStatus: LiveData<LoginStatus>
        get() = accessRepository.loginStatus

    val registerStatus: LiveData<AccessRepository.RegisterStatus>
        get() = accessRepository.registerStatus

    init {
        ijDatabase = IJDatabase.getDatabase(application.applicationContext)
        accessRepository = AccessRepository(ijDatabase.userDao(), viewModelScope)
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    fun onDateSelected(year: Int, month: Int, dayOfMonth: Int) {
        accessRepository.setDobLiveData(LocalDate.of(year, month + 1, dayOfMonth))
    }

    fun loginUser() = accessRepository.loginUserAndUpdateAccessStatus()

    fun registerUser() = accessRepository.registerUserAndUpdateAccessStatus()

    fun getUid(): Long = accessRepository.uid

    override fun onCleared() {
        super.onCleared()
        Log.d(LOGTAG, "AccessVM Cleared")
    }
}