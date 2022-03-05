package com.hk.ijournal.repository.data.source.local.datasource

import com.hk.ijournal.repository.data.source.UserDataSource
import com.hk.ijournal.repository.data.source.local.dao.UserDao
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
    override suspend fun insertUser(user: DiaryUser) =
        withContext(ioDispatcher) { userDao.insertUser(user) }

    override suspend fun deleteUser(user: DiaryUser) =
        withContext(ioDispatcher) { userDao.deleteUser(user) }

    override suspend fun getUserbyName(userName: String) = withContext(ioDispatcher) {
        userDao.getUserbyName(userName)?.let {
            Result.success(it)
        } ?: Result.failure(Exception())
    }

    override suspend fun getUserbyId(uid: Long) = withContext(ioDispatcher) {
        userDao.getUserbyId(uid)?.let {
            Result.success(it)
        } ?: Result.failure(Exception())
    }
}
