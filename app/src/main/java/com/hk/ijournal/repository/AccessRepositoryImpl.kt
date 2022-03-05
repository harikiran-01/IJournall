package com.hk.ijournal.repository

import android.text.TextUtils
import com.hk.ijournal.repository.data.source.local.datasource.UserLocalDataSource
import com.hk.ijournal.repository.data.source.local.entities.DiaryUser
import kotlinx.coroutines.*

class AccessRepositoryImpl
    (private val userLocalDataSource: UserLocalDataSource,
     private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO): AccessRepository {

    enum class AccessStatus {
        LOGIN_SUCCESSFUL, USER_NOT_FOUND, INVALID_LOGIN, REGISTER_SUCCESSFULL, USER_ALREADY_EXISTS
    }

    data class AccessUser(val accessStatus: AccessStatus, val diaryUser: DiaryUser?)

    enum class AccessValidation {
        USERNAME_INVALID, PASSCODE_INVALID, DOB_INVALID
    }

    override fun isUsernameInvalid(username: String?): Boolean {
        return TextUtils.isEmpty(username)
    }

    override fun isPassCodeInvalid(passcode: String?): Boolean {
        return passcode?.let { it.length < 4 } ?: true
    }

    override fun processLoginAndGetAccessStatus(dbUser: DiaryUser?, diaryUser: DiaryUser): AccessUser {
        return isLoginSuccessful(dbUser, diaryUser)
    }

    override fun isLoginSuccessful(dbUser: DiaryUser?, diaryUser: DiaryUser): AccessUser {
        return when {
            dbUser == null -> AccessUser(AccessStatus.USER_NOT_FOUND, null)
            dbUser.passcode == diaryUser.passcode -> AccessUser(AccessStatus.LOGIN_SUCCESSFUL, dbUser)
            else -> AccessUser(AccessStatus.INVALID_LOGIN, null)
        }
    }

    override suspend fun processRegisterAndGetAccessStatus(dbUser: DiaryUser?, diaryUser: DiaryUser): AccessUser {
        return if (dbUser == null) {
            diaryUser.uid = insertUserInDbAndGetRowId(diaryUser)
            AccessUser(AccessStatus.REGISTER_SUCCESSFULL, diaryUser)
        } else AccessUser(AccessStatus.USER_ALREADY_EXISTS, null)
    }

    override suspend fun getMatchingUserinDb(username: String): Result<DiaryUser> = withContext(ioDispatcher) {
        return@withContext userLocalDataSource.getUserbyName(username)
    }

    override suspend fun getUser(uid: Long): Result<DiaryUser> = userLocalDataSource.getUserbyId(uid)

    override suspend fun insertUserInDbAndGetRowId(diaryUser: DiaryUser) = userLocalDataSource.insertUser(diaryUser)
}
