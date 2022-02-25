package com.hk.ijournal.di

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Room
import com.hk.ijournal.domain.AlbumUseCase
import com.hk.ijournal.domain.AlbumUseCaseImpl
import com.hk.ijournal.domain.PageUseCase
import com.hk.ijournal.domain.PageUseCaseImpl
import com.hk.ijournal.repository.*
import com.hk.ijournal.repository.data.source.local.AlbumLocalDataSource
import com.hk.ijournal.repository.data.source.local.DiaryLocalDataSource
import com.hk.ijournal.repository.data.source.local.IJDatabase
import com.hk.ijournal.repository.data.source.local.UserLocalDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
    annotation class LocalUserDataSource

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class LocalDiaryDataSource

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class LocalAlbumDataSource

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class DiaryRepo

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class AlbumRepo

    @Singleton
    @LocalUserDataSource
    @Provides
    fun provideUserLocalDataSource(
        database: IJDatabase,
        ioDispatcher: CoroutineDispatcher
    ): UserLocalDataSource {
        return UserLocalDataSource(
            database.userDao(), ioDispatcher
        )
    }

    @Singleton
    @LocalDiaryDataSource
    @Provides
    fun provideDiaryLocalDataSource(
        database: IJDatabase,
        ioDispatcher: CoroutineDispatcher
    ): DiaryLocalDataSource {
        return DiaryLocalDataSource(
            database.diaryDao(), ioDispatcher
        )
    }

    @Singleton
    @LocalAlbumDataSource
    @Provides
    fun provideAlbumLocalDataSource(
        database: IJDatabase,
        ioDispatcher: CoroutineDispatcher
    ): AlbumLocalDataSource {
        return AlbumLocalDataSource(
            database.albumDao(), ioDispatcher
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Singleton
    @DiaryRepo
    @Provides
    fun provideDiaryRepository(
        @AppModule.LocalDiaryDataSource diaryLocalDataSource: DiaryLocalDataSource,
        ioDispatcher: CoroutineDispatcher
    ): DiaryRepository {
        return DiaryRepositoryImpl(diaryLocalDataSource, ioDispatcher)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Singleton
    @AlbumRepo
    @Provides
    fun provideAlbumRepository(
        @AppModule.LocalAlbumDataSource albumLocalDataSource: AlbumLocalDataSource,
        ioDispatcher: CoroutineDispatcher
    ): AlbumRepository {
        return AlbumRepoImpl(albumLocalDataSource, ioDispatcher)
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
        @AppModule.LocalUserDataSource userLocalDataSource: UserLocalDataSource,
        ioDispatcher: CoroutineDispatcher
    ): AccessRepository {
        return AccessRepositoryImpl(userLocalDataSource, ioDispatcher)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object HomeRepositoryModule {

    @Singleton
    @Provides
    fun providePageUseCase(
        @AppModule.DiaryRepo diaryRepository: DiaryRepository
    ): PageUseCase {
        return PageUseCaseImpl(diaryRepository)
    }

    @Singleton
    @Provides
    fun provideAlbumUseCase(
        @AppModule.DiaryRepo diaryRepository: DiaryRepository,
        @AppModule.AlbumRepo albumRepository: AlbumRepository
    ): AlbumUseCase {
        return AlbumUseCaseImpl(diaryRepository, albumRepository)
    }

}