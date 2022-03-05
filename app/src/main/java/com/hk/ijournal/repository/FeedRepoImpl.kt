package com.hk.ijournal.repository

import com.hk.ijournal.repository.data.source.local.datasource.FeedLocalDataSource
import com.hk.ijournal.repository.data.source.local.entities.DiaryPage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class FeedRepoImpl @Inject constructor(private val feedLocalDataSource: FeedLocalDataSource, private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO) : FeedRepo {
    override suspend fun getAllPages(uid: Long): List<DiaryPage> {
        return feedLocalDataSource.getAllPages(uid)
    }
}