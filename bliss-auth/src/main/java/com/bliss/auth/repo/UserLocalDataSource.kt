package com.bliss.auth.repo

import com.hk.ijournal.dao.UserDao
import com.hk.ijournal.entities.UserEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Main entry point for accessing user data.
 */
class UserLocalDataSource internal constructor(
    private val userDao: UserDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : UserDataSource {
    override suspend fun insertUser(user: User) =
        withContext(ioDispatcher) { userDao.insertUser(UserEntity(user.username, user.passcode)) }

    override suspend fun deleteUser(user: User) =
        withContext(ioDispatcher) { userDao.deleteUser(UserEntity(user.username, user.passcode)) }

    override suspend fun getUserbyName(userName: String) = withContext(ioDispatcher) {
        userDao.getUserbyName(userName)?.let {
            Result.success(User(it.username, it.passcode))
        } ?: Result.failure(Exception())
    }

    override suspend fun getUserbyId(uid: Long) = withContext(ioDispatcher) {
        userDao.getUserbyId(uid)?.let {
            Result.success(User(it.username, it.passcode))
        } ?: Result.failure(Exception())
    }
}
