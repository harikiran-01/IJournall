package com.hk.ijournal.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.hk.ijournal.entities.UserEntity

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: UserEntity): Long

    @Delete
    suspend fun deleteUser(user: UserEntity)

    @Query("select * from usertable where username = :userName")
    fun getUserbyName(userName: String?): UserEntity?

    @Query("select * from usertable where uid = :uid")
    fun getUserbyId(uid: Long): UserEntity?
}