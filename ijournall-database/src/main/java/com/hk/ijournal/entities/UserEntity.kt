package com.hk.ijournal.entities

import androidx.room.*
import com.hk.ijournal.entities.typeconverters.DateConverter
import java.io.Serializable
import java.time.LocalDate

@Entity(tableName = "usertable", indices = [Index("uid")])
@TypeConverters(DateConverter::class)
data class UserEntity @Ignore constructor(var username: String, var passcode: String, var dob: LocalDate?): Serializable{
    @PrimaryKey(autoGenerate = true)
    var uid: Long = 0

    constructor() : this("", "", null)
    
    @Ignore
    constructor(username: String, passcode: String) : this(username, passcode, null)

    @Override
    override fun toString(): String {
        return "uid: $uid, username: $username"
    }

}