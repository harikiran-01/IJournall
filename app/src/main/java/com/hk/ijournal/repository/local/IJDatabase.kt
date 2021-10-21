package com.hk.ijournal.repository.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hk.ijournal.repository.models.DayAlbum
import com.hk.ijournal.repository.models.DiaryPage
import com.hk.ijournal.repository.models.DiaryUser

@Database(entities = [DiaryUser::class, DiaryPage::class, DayAlbum::class], exportSchema = false, version = 2)
abstract class IJDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun diaryDao(): DiaryDao
    abstract fun albumDao(): AlbumDao

    enum class DaoMapKeys {
        Album, Page
    }

    fun getDiaryPageDao(): MutableMap<DaoMapKeys, RoomDao> {
        val diaryDaoMap = mutableMapOf<DaoMapKeys, RoomDao>()
        diaryDaoMap[DaoMapKeys.Album] = albumDao()
        diaryDaoMap[DaoMapKeys.Page] = diaryDao()
        return diaryDaoMap
    }

    companion object {
        //db schema
        private const val DBNAME = "Journals.db"

        @Volatile
        private var INSTANCE: IJDatabase? = null

        @JvmStatic
        @Synchronized
        fun getDatabase(context: Context): IJDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) return tempInstance

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