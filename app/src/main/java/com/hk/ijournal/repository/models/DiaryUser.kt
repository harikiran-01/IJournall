package com.hk.ijournal.repository.models

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.hk.ijournal.utils.DateConverter
import java.time.LocalDate

@Entity(tableName = "usertable")
@TypeConverters(DateConverter::class)
data class DiaryUser @Ignore constructor(var username: String, var passcode: String, var dob: LocalDate?) {
    @PrimaryKey(autoGenerate = true)
    var uid = 0

    constructor() : this("", "", null)

    @Ignore
    constructor(username: String, passcode: String) : this(username, passcode, null)

}