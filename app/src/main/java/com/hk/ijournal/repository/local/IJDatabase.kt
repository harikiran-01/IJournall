package com.hk.ijournal.repository.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hk.ijournal.repository.models.DiaryUser

@Database(entities = [DiaryUser::class], exportSchema = false, version = 2)
abstract class IJDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao


    companion object {
        //db schema
        const val DBNAME = "Journals.db"

        @Volatile
        private var INSTANCE: IJDatabase? = null

        @JvmStatic
        @Synchronized
        fun getDatabase(context: Context): IJDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        IJDatabase::class.java,
                        DBNAME
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }
        }
    }
}