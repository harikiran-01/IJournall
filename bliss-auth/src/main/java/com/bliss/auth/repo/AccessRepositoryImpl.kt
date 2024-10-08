package com.bliss.auth.repo

import android.text.TextUtils
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AccessRepositoryImpl
    (private val userLocalDataSource: UserDataSource,
     private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO): AccessRepository {

    enum class AccessStatus {
        LOGIN_SUCCESSFUL, USER_NOT_FOUND, INVALID_LOGIN, REGISTER_SUCCESSFULL, USER_ALREADY_EXISTS
    }

    data class AccessUser(val accessStatus: AccessStatus, val diaryUser: User?)

    enum class AccessValidation {
        USERNAME_INVALID, PASSCODE_INVALID, DOB_INVALID
    }

    override fun isUsernameInvalid(username: String?): Boolean {
        return TextUtils.isEmpty(username)
    }

    override fun isPassCodeInvalid(passcode: String?): Boolean {
        return passcode?.let { it.length < 4 } ?: true
    }

    override fun processLoginAndGetAccessStatus(dbUser: User?, diaryUser: User): AccessUser {
        return isLoginSuccessful(dbUser, diaryUser)
    }

    override fun isLoginSuccessful(dbUser: User?, diaryUser: User): AccessUser {
        return when {
            dbUser == null -> AccessUser(AccessStatus.USER_NOT_FOUND, null)
            dbUser.passcode == diaryUser.passcode -> AccessUser(AccessStatus.LOGIN_SUCCESSFUL, dbUser)
            else -> AccessUser(AccessStatus.INVALID_LOGIN, null)
        }
    }

    override suspend fun processRegisterAndGetAccessStatus(dbUser: User?, diaryUser: User): AccessUser {
        return if (dbUser == null) {
            //diaryUser.uid = insertUserInDbAndGetRowId(diaryUser)
            AccessUser(AccessStatus.REGISTER_SUCCESSFULL, diaryUser)
        } else AccessUser(AccessStatus.USER_ALREADY_EXISTS, null)
    }

    override suspend fun getMatchingUserinDb(username: String): Result<User> = withContext(ioDispatcher) {
        return@withContext userLocalDataSource.getUserbyName(username)
    }

    override suspend fun getUser(uid: Long): Result<User> = userLocalDataSource.getUserbyId(uid)

    override suspend fun insertUserInDbAndGetRowId(diaryUser: User) = userLocalDataSource.insertUser(diaryUser)
}
