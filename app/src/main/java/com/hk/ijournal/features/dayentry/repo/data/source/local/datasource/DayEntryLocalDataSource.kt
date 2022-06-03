package com.hk.ijournal.features.dayentry.repo.data.source.local.datasource

import com.hk.ijournal.features.dayentry.models.Page
import com.hk.ijournal.features.dayentry.repo.data.source.local.dao.DayEntryDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate

class DayEntryLocalDataSource internal constructor(
    private val dayEntryDao: DayEntryDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO): DayEntryDataSource {

    override suspend fun insertPage(page: Page): Long? = withContext(ioDispatcher) {
        return@withContext dayEntryDao.insertPage(page)
    }

    override suspend fun updatePage(page: Page) = withContext(ioDispatcher) {
        dayEntryDao.updatePage(page)
    }

    override suspend fun getPageforDate(selectedDate: LocalDate, uid: Long): Page? = withContext(ioDispatcher) {
        return@withContext dayEntryDao.getPageforDate(selectedDate, uid)
    }

    override suspend fun getPageForId(pid: Long) = withContext(ioDispatcher) {
        return@withContext dayEntryDao.getPageForId(pid)
    }

    override suspend fun getPageIdForDate(selectedDate: LocalDate, uid: Long): Long? = withContext(ioDispatcher) {
        return@withContext dayEntryDao.getPageIdForDate(selectedDate, uid)
    }
}