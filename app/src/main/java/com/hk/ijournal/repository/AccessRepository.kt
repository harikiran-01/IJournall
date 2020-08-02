package com.hk.ijournal.repository

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.hk.ijournal.repository.local.UserDao
import com.hk.ijournal.repository.models.DiaryUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

class AccessRepository(private val userDao: UserDao, private val coroutineScope: CoroutineScope) {
    private lateinit var diaryUser: DiaryUser
    var uid: Long = 0

    //login livedata
    val loginUsernameLive: MutableLiveData<String> = MutableLiveData()
    val loginPasscodeLive: MutableLiveData<String> = MutableLiveData()

    //register livedata
    val registerUsernameLive: MutableLiveData<String> = MutableLiveData()
    val registerPasscodeLive: MutableLiveData<String> = MutableLiveData()
    val dobLiveData: MutableLiveData<LocalDate> = MutableLiveData()

    //access livedata
    val loginStatus: MutableLiveData<LoginStatus> = MutableLiveData()
    val registerStatus: MutableLiveData<RegisterStatus> = MutableLiveData()
    val accessStatus: MutableLiveData<AccessStatus> = MutableLiveData()

    enum class RegisterStatus {
        REGISTER_SUCCESSFULL, USER_ALREADY_EXISTS
    }

    enum class AccessStatus {
        ACCESS_SUCCESS
    }

    enum class LoginStatus {
        LOGIN_SUCCESSFUL, USER_NOT_FOUND, INVALID_LOGIN
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
        //transforming based on passcode live data
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
        //transforming based on passcode live data
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

    private fun processLoginAndGetAccessStatus(dbUser: DiaryUser?): LoginStatus {
        return isLoginSuccessful(dbUser)
    }

    private fun isLoginSuccessful(dbUser: DiaryUser?): LoginStatus {
        return if (dbUser == null) LoginStatus.USER_NOT_FOUND
        else if (dbUser.passcode == diaryUser.passcode) LoginStatus.LOGIN_SUCCESSFUL
        else LoginStatus.INVALID_LOGIN
    }

    private fun processRegisterAndGetAccessStatus(dbUser: DiaryUser?): RegisterStatus {
        return if (dbUser == null) {
            RegisterStatus.REGISTER_SUCCESSFULL
        } else RegisterStatus.USER_ALREADY_EXISTS
    }

    fun setDobLiveData(dob: LocalDate) {
        dobLiveData.value = dob
    }

    fun loginUserAndUpdateAccessStatus() {
        diaryUser = DiaryUser(loginUsernameLive.value.toString(), loginPasscodeLive.value.toString())
        coroutineScope.launch {
            val dbUser = getMatchingUserinDb()
            uid = dbUser?.uid ?: 0
            loginStatus.value = processLoginAndGetAccessStatus(dbUser)
        }
    }

    fun registerUserAndUpdateAccessStatus() {
        var regStatus: RegisterStatus
        diaryUser = DiaryUser(registerUsernameLive.value.toString(), registerPasscodeLive.value.toString(), dobLiveData.value)
        coroutineScope.launch {
            regStatus = processRegisterAndGetAccessStatus(getMatchingUserinDb())
            //runJunk()
            uid = insertUserInDbAndGetRowId(diaryUser)
            if (uid > 0L)
                registerStatus.value = regStatus
        }
        Log.i("after", "after coroutine")
    }

    private suspend fun getMatchingUserinDb(): DiaryUser? {
        return withContext(Dispatchers.IO) { userDao.getUserbyName(diaryUser.username) }
    }

    private suspend fun insertUserInDbAndGetRowId(diaryUser: DiaryUser): Long =
            withContext(Dispatchers.IO) { userDao.insertUser(diaryUser) }
}
