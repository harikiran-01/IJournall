package com.hk.ijournal.repository.models

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import java.time.LocalDate

class AccessModel {
    var diaryUser: DiaryUser

    //login livedata
    val loginUsernameLive: MutableLiveData<String> = MutableLiveData()
    val loginPasscodeLive: MutableLiveData<String> = MutableLiveData()

    //register livedata
    val registerUsernameLive: MutableLiveData<String> = MutableLiveData()
    val registerPasscodeLive: MutableLiveData<String> = MutableLiveData()
    val dobLiveData: MutableLiveData<LocalDate> = MutableLiveData()

    init {
        diaryUser = DiaryUser()
    }

    enum class AccessStatus {
        LOGIN_SUCCESSFUL, USER_NOT_FOUND, INVALID_LOGIN, REGISTER_SUCCESSFULL, USER_ALREADY_EXISTS
    }

    enum class AccessValidation {
        USERNAME_INVALID, PASSCODE_INVALID, DOB_INVALID
    }

    fun getLoginUserValidation(): MutableLiveData<AccessValidation> {
        //transforming based on username live data
        return Transformations.map(loginUsernameLive) {
            return@map if (isUsernameInvalid(it)) AccessValidation.USERNAME_INVALID
            else
                null
        } as MutableLiveData<AccessValidation>
    }

    fun getLoginPasscodeValidation(): MutableLiveData<AccessValidation> {
        //transforming based on username live data
        return Transformations.map(loginPasscodeLive) {
            return@map if (isPassCodeInvalid(it)) AccessValidation.PASSCODE_INVALID
            else
                null
        } as MutableLiveData<AccessValidation>
    }

    fun getRegisterUserValidation(): MutableLiveData<AccessValidation> {
        //transforming based on username live data
        return Transformations.map(registerUsernameLive) {
            return@map if (isUsernameInvalid(it)) AccessValidation.USERNAME_INVALID
            else
                null
        } as MutableLiveData<AccessValidation>
    }

    fun getRegisterPasscodeValidation(): MutableLiveData<AccessValidation> {
        //transforming based on username live data
        return Transformations.map(registerPasscodeLive) {
            return@map if (isPassCodeInvalid(it)) AccessValidation.PASSCODE_INVALID
            else
                null
        } as MutableLiveData<AccessValidation>
    }

    private fun isUsernameInvalid(username: String?): Boolean {
        return TextUtils.isEmpty(username)
    }

    private fun isPassCodeInvalid(passcode: String?): Boolean {
        return passcode?.let { it.length < 4 } ?: true
    }

    fun isUserValid(userValidation: LiveData<AccessValidation>, passcodeValidation: LiveData<AccessValidation>): Boolean {
        return userValidation.value != AccessValidation.USERNAME_INVALID && passcodeValidation.value != AccessValidation.PASSCODE_INVALID
    }

    fun processLoginAndGetAccessStatus(diaryUser: DiaryUser?): AccessStatus {
        return signInWithUser(diaryUser)
    }

    fun signInWithUser(dbUser: DiaryUser?): AccessStatus {
        return if (dbUser == null) AccessStatus.USER_NOT_FOUND else if (dbUser.passcode == diaryUser.passcode) AccessStatus.LOGIN_SUCCESSFUL else AccessStatus.INVALID_LOGIN
    }

    fun processRegisterAndGetAccessStatus(dbUser: DiaryUser?): AccessStatus {
        return if (dbUser == null) {
            AccessStatus.REGISTER_SUCCESSFULL
        } else AccessStatus.USER_ALREADY_EXISTS
    }
}