package com.hk.ijournal.repository.data.source

import com.hk.ijournal.repository.data.source.local.entities.User

/**
 * Main entry point for accessing user data.
 */
interface UserDataSource {
    suspend fun insertUser(user: User): Long

    suspend fun deleteUser(user: User)

    suspend fun getUserbyName(userName: String): Result<User>
    suspend fun getUserbyId(uid: Long): Result<User>
}
