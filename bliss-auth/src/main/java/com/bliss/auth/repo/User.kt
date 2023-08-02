package com.bliss.auth.repo

import com.bliss.auth.AuthApiImpl.uid
import java.io.Serializable
import java.time.LocalDate

data class User constructor(var username: String, var passcode: String, var dob: LocalDate?):
    Serializable {

    constructor(username: String, passcode: String) : this(username, passcode, null)

    constructor() : this("", "", null)

    @Override
    override fun toString(): String {
        return "uid: $uid, username: $username"
    }

}