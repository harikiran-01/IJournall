package com.hk.ijournal.di

import android.content.Context
import androidx.room.Room
import com.hk.ijournal.IJDatabase
import com.hk.ijournal.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

/**
 * Module to tell Hilt how to provide instances of types that cannot be constructor-injected.
 *
 * As these types are scoped to the application lifecycle using @Singleton, they're installed
 * in Hilt's ApplicationComponent.
 */
@Module
@InstallIn(SingletonComponent::class)
object DbModule {

    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context): IJDatabase {
        synchronized(this) {
            return Room.databaseBuilder(
                context.applicationContext,
                IJDatabase::class.java,
                IJDatabase.DBNAME
            ).fallbackToDestructiveMigration().build()
        }
    }

    @Provides
    fun provideUserDao(ijDatabase: IJDatabase): UserDao {
        return ijDatabase.userDao()
    }

    @Singleton
    @Provides
    fun provideIoDispatcher() = Dispatchers.IO
}