package com.hk.ijournal.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.hk.ijournal.utils.DateConverter;

import java.time.LocalDate;

@Entity(tableName = "usertable")
@TypeConverters(DateConverter.class)
public class DiaryUser {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int uid = 0;
    private String username;
    private String passcode;
    private LocalDate dob;

    public DiaryUser() {
    }

    @Ignore
    public DiaryUser(String username, String passcode) {
        this(username,passcode,null);
    }

    @Ignore
    public DiaryUser(String username, String passcode, LocalDate dob) {
        this.username = username;
        this.passcode = passcode;
        this.dob = dob;
    }

    @NonNull
    public int getUid() {
        return uid;
    }

    public void setUid(@NonNull int uid) {
        this.uid = uid;
    }



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasscode() {
        return passcode;
    }

    public void setPasscode(String passcode) {
        this.passcode = passcode;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }
}
