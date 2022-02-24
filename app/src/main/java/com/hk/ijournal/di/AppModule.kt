package com.hk.ijournal.di

import android.content.Context
import androidx.room.Room
import com.hk.ijournal.repository.AccessRepository
import com.hk.ijournal.repository.AccessRepositoryImpl
import com.hk.ijournal.repository.data.source.local.IJDatabase
import com.hk.ijournal.repository.data.source.local.UserLocalDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier
import javax.inject.Singleton

/**
 * Module to tell Hilt how to provide instances of types that cannot be constructor-injected.
 *
 * As these types are scoped to the application lifecycle using @Singleton, they're installed
 * in Hilt's ApplicationComponent.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class LocalTasksDataSource

    @Singleton
    @LocalTasksDataSource
    @Provides
    fun provideAccessLocalDataSource(
        database: IJDatabase,
        ioDispatcher: CoroutineDispatcher
    ): UserLocalDataSource {
        return UserLocalDataSource(
            database.userDao(), ioDispatcher
        )
    }

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

    @Singleton
    @Provides
    fun provideIoDispatcher() = Dispatchers.IO
}

/**
 * The binding for TasksRepository is on its own module so that we can replace it easily in tests.
 */
@Module
@InstallIn(SingletonComponent::class)
object UserRepositoryModule {

    @Singleton
    @Provides
    fun provideUserRepository(
        @AppModule.LocalTasksDataSource localTasksDataSource: UserLocalDataSource,
        ioDispatcher: CoroutineDispatcher
    ): AccessRepository {
        return AccessRepositoryImpl(localTasksDataSource, ioDispatcher)
    }

}