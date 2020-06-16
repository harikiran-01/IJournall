package com.hk.ijournal.repository.local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.hk.ijournal.repository.models.DiaryUser;

@Dao
public interface UserDao {
    @Insert
    void insertUser(DiaryUser user);

    @Delete
    void deleteUser(DiaryUser user);

    @Query("select * from usertable where username = :userName")
    DiaryUser getUserbyName(String userName);
}
