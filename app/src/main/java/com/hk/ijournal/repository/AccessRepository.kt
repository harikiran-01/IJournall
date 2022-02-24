package com.hk.ijournal.repository

import androidx.lifecycle.LiveData
import com.hk.ijournal.repository.data.source.local.entities.DiaryUser
import java.time.LocalDate

interface AccessRepository {
    fun isUsernameInvalid(username: String?): Boolean
    fun isPassCodeInvalid(passcode: String?): Boolean
    fun processLoginAndGetAccessStatus(dbUser: DiaryUser?, diaryUser: DiaryUser): AccessRepositoryImpl.LoginStatus
    fun isLoginSuccessful(dbUser: DiaryUser?, diaryUser: DiaryUser): AccessRepositoryImpl.LoginStatus
    fun processRegisterAndGetAccessStatus(dbUser: DiaryUser?): AccessRepositoryImpl.RegisterStatus
    suspend fun getMatchingUserinDb(username: String): Result<DiaryUser>
    suspend fun insertUserInDbAndGetRowId(diaryUser: DiaryUser): Long
}
