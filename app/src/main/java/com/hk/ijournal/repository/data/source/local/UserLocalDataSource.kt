package com.hk.ijournal.repository.data.source.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.hk.ijournal.repository.data.source.UserDataSource
import com.hk.ijournal.repository.data.source.local.entities.DiaryUser
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

/**
 * Main entry point for accessing user data.
 */
class UserLocalDataSource internal constructor(
    private val userDao: UserDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : UserDataSource {
    override suspend fun insertUser(user: DiaryUser): Long {
        return userDao.insertUser(user)
    }

    override suspend fun deleteUser(user: DiaryUser) {
        userDao.deleteUser(user)
    }

    override suspend fun getUserbyName(userName: String): Result<DiaryUser> {
        return userDao.getUserbyName(userName)?.let {
            Result.success(it)
        } ?: Result.failure(Exception())
        }
    }