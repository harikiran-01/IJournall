package com.hk.ijournal.repository.data.source.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.hk.ijournal.repository.data.source.local.entities.User

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: User): Long

    @Delete
    suspend fun deleteUser(user: User)

    @Query("select * from usertable where username = :userName")
    suspend fun getUserbyName(userName: String?): User?

    @Query("select * from usertable where uid = :uid")
    suspend fun getUserbyId(uid: Long): User?
}