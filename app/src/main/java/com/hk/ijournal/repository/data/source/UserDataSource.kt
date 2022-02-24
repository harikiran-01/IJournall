package com.hk.ijournal.repository.data.source

import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.hk.ijournal.repository.data.source.local.entities.DiaryUser

/**
 * Main entry point for accessing user data.
 */
interface UserDataSource {
    suspend fun insertUser(user: DiaryUser): Long

    suspend fun deleteUser(user: DiaryUser)

    suspend fun getUserbyName(userName: String): Result<DiaryUser>
}
