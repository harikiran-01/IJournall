package com.hk.ijournal.repository.data.source.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.hk.ijournal.repository.data.source.local.entities.DiaryUser

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: DiaryUser): Long

    @Delete
    suspend fun deleteUser(user: DiaryUser)

    @Query("select * from usertable where username = :userName")
    suspend fun getUserbyName(userName: String?): DiaryUser?

    @Query("select * from usertable where uid = :uid")
    suspend fun getUserbyId(uid: Long): DiaryUser?
}