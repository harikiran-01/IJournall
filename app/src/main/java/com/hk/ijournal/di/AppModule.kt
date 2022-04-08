package com.hk.ijournal.di

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.hk.ijournal.dayentry.models.content.BaseEntity
import com.hk.ijournal.dayentry.repo.DayEntryRepo
import com.hk.ijournal.dayentry.repo.DayEntryRepoImpl
import com.hk.ijournal.dayentry.repo.data.source.local.datasource.DayEntryLocalDataSource
import com.hk.ijournal.domain.FeedUseCase
import com.hk.ijournal.domain.FeedUseCaseImpl
import com.hk.ijournal.domain.PageUseCase
import com.hk.ijournal.domain.PageUseCaseImpl
import com.hk.ijournal.repository.AccessRepository
import com.hk.ijournal.repository.AccessRepositoryImpl
import com.hk.ijournal.repository.FeedRepo
import com.hk.ijournal.repository.FeedRepoImpl
import com.hk.ijournal.repository.data.source.local.IJDatabase
import com.hk.ijournal.repository.data.source.local.datasource.FeedLocalDataSource
import com.hk.ijournal.repository.data.source.local.datasource.UserLocalDataSource
import com.hk.ijournal.utils.ContentTypeAdapter
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

    @Singleton
    @Provides
    fun provideGson(): Gson {
        return GsonBuilder().registerTypeAdapter(BaseEntity::class.java, ContentTypeAdapter()).create()
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
object UserModule {

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class LocalUserDataSource

    @Singleton
    @LocalUserDataSource
    @Provides
    fun provideUserLocalDataSource(database: IJDatabase, ioDispatcher: CoroutineDispatcher): UserLocalDataSource {
        return UserLocalDataSource(database.userDao(), ioDispatcher)
    }

    @Singleton
    @Provides
    fun provideUserRepository(
        @LocalUserDataSource userLocalDataSource: UserLocalDataSource,
        ioDispatcher: CoroutineDispatcher): AccessRepository {
        return AccessRepositoryImpl(userLocalDataSource, ioDispatcher)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object PageModule {

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class LocalDiaryDataSource

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class DiaryRepo

    @Singleton
    @LocalDiaryDataSource
    @Provides
    fun provideDiaryLocalDataSource(
        database: IJDatabase,
        ioDispatcher: CoroutineDispatcher
    ): DayEntryLocalDataSource {
        return DayEntryLocalDataSource(database.dayEntryDao(), ioDispatcher)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Singleton
    @DiaryRepo
    @Provides
    fun provideDiaryRepository(
        @LocalDiaryDataSource diaryLocalDataSource: DayEntryLocalDataSource,
        ioDispatcher: CoroutineDispatcher
    ): DayEntryRepo {
        return DayEntryRepoImpl(diaryLocalDataSource, ioDispatcher)
    }

    @Singleton
    @Provides
    fun providePageUseCase(
        @DiaryRepo diaryRepository: DayEntryRepo
    ): PageUseCase {
        return PageUseCaseImpl(diaryRepository)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object FeedModule {

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class LocalFeedDataSource

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class FeedRepository

    @Singleton
    @LocalFeedDataSource
    @Provides
    fun provideFeedLocalDataSource(
        database: IJDatabase,
        ioDispatcher: CoroutineDispatcher
    ): FeedLocalDataSource {
        return FeedLocalDataSource(database.dayEntryDao(), ioDispatcher)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Singleton
    @FeedRepository
    @Provides
    fun provideFeedRepository(
        @LocalFeedDataSource feedLocalDataSource: FeedLocalDataSource,
        ioDispatcher: CoroutineDispatcher
    ): FeedRepo {
        return FeedRepoImpl(feedLocalDataSource, ioDispatcher)
    }

    @Singleton
    @Provides
    fun provideFeedUseCase(
        @FeedRepository feedRepo: FeedRepo
    ): FeedUseCase {
        return FeedUseCaseImpl(feedRepo)
    }
}