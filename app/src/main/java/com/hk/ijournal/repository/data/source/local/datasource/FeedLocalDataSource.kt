package com.hk.ijournal.repository.data.source.local.datasource

import com.hk.ijournal.repository.data.source.FeedDataSource
import com.hk.ijournal.repository.data.source.local.dao.DiaryDao
import com.hk.ijournal.repository.data.source.local.entities.Page
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FeedLocalDataSource internal constructor(
    private val diaryDao: DiaryDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO) : FeedDataSource {
    override suspend fun getAllPages(uid: Long): List<Page> = withContext(ioDispatcher) {
        return@withContext diaryDao.getAllPages(uid)
    }
}