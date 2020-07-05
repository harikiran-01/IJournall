package com.hk.ijournal.repository.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.hk.ijournal.repository.models.DiaryUser

@Dao
interface UserDao {
    @Insert
    fun insertUser(user: DiaryUser)

    @Delete
    fun deleteUser(user: DiaryUser)

    @Query("select * from usertable where username = :userName")
    fun getUserbyName(userName: String?): DiaryUser?
}