package com.hk.ijournal.repository

import com.hk.ijournal.repository.data.source.local.entities.User

interface AccessRepository {
    fun isUsernameInvalid(username: String?): Boolean
    fun isPassCodeInvalid(passcode: String?): Boolean
    fun processLoginAndGetAccessStatus(dbUser: User?, diaryUser: User): AccessRepositoryImpl.AccessUser
    fun isLoginSuccessful(dbUser: User?, diaryUser: User): AccessRepositoryImpl.AccessUser
    suspend fun processRegisterAndGetAccessStatus(dbUser: User?, diaryUser: User): AccessRepositoryImpl.AccessUser
    suspend fun getMatchingUserinDb(username: String): Result<User>
    suspend fun getUser(uid: Long): Result<User>
    suspend fun insertUserInDbAndGetRowId(diaryUser: User): Long
}
