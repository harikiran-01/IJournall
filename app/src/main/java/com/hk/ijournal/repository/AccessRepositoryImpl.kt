package com.hk.ijournal.repository

import android.text.TextUtils
import androidx.lifecycle.LiveData
import com.hk.ijournal.repository.data.source.local.UserLocalDataSource
import com.hk.ijournal.repository.data.source.local.entities.DiaryUser
import kotlinx.coroutines.*

class AccessRepositoryImpl
    (private val userLocalDataSource: UserLocalDataSource,
     private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO): AccessRepository {

    enum class RegisterStatus {
        REGISTER_SUCCESSFULL, USER_ALREADY_EXISTS
    }


    enum class LoginStatus {
        LOGIN_SUCCESSFUL, USER_NOT_FOUND, INVALID_LOGIN
    }

    enum class AccessValidation {
        USERNAME_INVALID, PASSCODE_INVALID, DOB_INVALID
    }

    override fun isUsernameInvalid(username: String?): Boolean {
        return TextUtils.isEmpty(username)
    }

    override fun isPassCodeInvalid(passcode: String?): Boolean {
        return passcode?.let { it.length < 4 } ?: true
    }

    override fun processLoginAndGetAccessStatus(dbUser: DiaryUser?, diaryUser: DiaryUser): LoginStatus {
        return isLoginSuccessful(dbUser, diaryUser)
    }

    override fun isLoginSuccessful(dbUser: DiaryUser?, diaryUser: DiaryUser): LoginStatus {
        return when {
            dbUser == null -> LoginStatus.USER_NOT_FOUND
            dbUser.passcode == diaryUser.passcode -> LoginStatus.LOGIN_SUCCESSFUL
            else -> LoginStatus.INVALID_LOGIN
        }
    }

    override fun processRegisterAndGetAccessStatus(dbUser: DiaryUser?): RegisterStatus {
        return if (dbUser == null) {
            RegisterStatus.REGISTER_SUCCESSFULL
        } else RegisterStatus.USER_ALREADY_EXISTS
    }

    override suspend fun getMatchingUserinDb(userName: String): Result<DiaryUser> = withContext(ioDispatcher) {
        return@withContext userLocalDataSource.getUserbyName(userName)
    }

    override suspend fun insertUserInDbAndGetRowId(diaryUser: DiaryUser): Long =
            withContext(Dispatchers.IO) { userLocalDataSource.insertUser(diaryUser) }
}
