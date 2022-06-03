package com.hk.ijournal.repository.data.source.local.datasource

import com.hk.ijournal.repository.data.source.FeedDataSource
import com.hk.ijournal.features.dayentry.repo.data.source.local.dao.DayEntryDao
import com.hk.ijournal.features.dayentry.models.Page
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