package com.hk.ijournal.repository.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hk.ijournal.dayentry.models.Page
import com.hk.ijournal.dayentry.repo.data.source.local.dao.DayEntryDao
import com.hk.ijournal.repository.data.source.local.dao.UserDao
import com.hk.ijournal.repository.data.source.local.entities.User

@Database(entities = [User::class, Page::class], exportSchema = false, version = 1)
abstract class IJDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun dayEntryDao(): DayEntryDao

    companion object {
        const val DBNAME = "Journals.db"
    }
}