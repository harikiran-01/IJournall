package com.hk.ijournal
import androidx.room.Database
import androidx.room.RoomDatabase
import com.hk.ijournal.dao.UserDao
import com.hk.ijournal.entities.UserEntity

@Database(entities = [UserEntity::class], exportSchema = false, version = 1)
abstract class IJDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        const val DBNAME = "Journals.db"
    }
}