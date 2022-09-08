package com.hk.ijournal.features.feed.repo

import com.hk.ijournal.features.dayentry.models.Page
import com.hk.ijournal.features.feed.datasource.FeedLocalDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class FeedRepoImpl @Inject constructor(private val feedLocalDataSource: FeedLocalDataSource, private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO) :
    FeedRepo {
    override suspend fun getAllPages(uid: Long): List<Page> {
        return feedLocalDataSource.getAllPages(uid)
    }
}