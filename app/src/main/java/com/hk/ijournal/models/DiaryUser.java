package com.hk.ijournal.models;

import java.time.LocalDate;

public class DiaryUser {
    String username;
    String passcode;
    LocalDate dob;

    public DiaryUser() {
    }

    public DiaryUser(String username, String passcode, LocalDate dob) {
        this.username = username;
        this.passcode = passcode;
        this.dob = dob;
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
