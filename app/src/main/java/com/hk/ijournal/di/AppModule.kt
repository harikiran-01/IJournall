package com.hk.ijournal.di

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.hk.ijournal.IJDatabase
import com.hk.ijournal.domain.PageUseCase
import com.hk.ijournal.domain.PageUseCaseImpl
import com.hk.ijournal.features.dayentry.models.content.BaseEntity
import com.hk.ijournal.features.dayentry.repo.DayEntryRepo
import com.hk.ijournal.features.dayentry.repo.DayEntryRepoImpl
import com.hk.ijournal.features.dayentry.repo.data.source.local.datasource.DayEntryLocalDataSource
import com.hk.ijournal.features.feed.datasource.FeedLocalDataSource
import com.hk.ijournal.features.feed.repo.FeedRepo
import com.hk.ijournal.features.feed.repo.FeedRepoImpl
import com.hk.ijournal.features.feed.usecases.FeedUseCase
import com.hk.ijournal.features.feed.usecases.FeedUseCaseImpl
import com.hk.ijournal.features.search.datasource.SearchDataSource
import com.hk.ijournal.features.search.datasource.SearchLocalDataSource
import com.hk.ijournal.features.search.repo.SearchRepo
import com.hk.ijournal.features.search.repo.SearchRepoImpl
import com.hk.ijournal.features.search.usecases.SearchUseCase
import com.hk.ijournal.features.search.usecases.SearchUseCaseImpl
import com.hk.ijournal.utils.ContentTypeAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
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

@Module
@InstallIn(SingletonComponent::class)
object SearchModule {

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class LocalSearchDataSource

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class SearchRepository

    @Singleton
    @LocalSearchDataSource
    @Provides
    fun provideSearchLocalDataSource(
        database: IJDatabase,
        ioDispatcher: CoroutineDispatcher
    ): SearchDataSource {
        return SearchLocalDataSource(database.dayEntryDao(), ioDispatcher)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Singleton
    @SearchRepository
    @Provides
    fun provideSearchRepository(
        @LocalSearchDataSource searchLocalDataSource: SearchDataSource,
        ioDispatcher: CoroutineDispatcher
    ): SearchRepo {
        return SearchRepoImpl(searchLocalDataSource, ioDispatcher)
    }

    @Singleton
    @Provides
    fun provideSearchUseCase(
        @SearchRepository searchRepo: SearchRepo
    ): SearchUseCase {
        return SearchUseCaseImpl(searchRepo)
    }
}