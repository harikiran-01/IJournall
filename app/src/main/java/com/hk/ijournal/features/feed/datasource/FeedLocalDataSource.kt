package com.hk.ijournal.features.feed.datasource

import com.hk.ijournal.features.dayentry.models.Page
import com.hk.ijournal.features.dayentry.repo.data.source.local.dao.DayEntryDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FeedLocalDataSource internal constructor(
    private val diaryDao: DayEntryDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO) : FeedDataSource {
    override suspend fun getAllPages(uid: Long): List<Page> = withContext(ioDispatcher) {
        return@withContext diaryDao.getAllPages(uid)
    }
}