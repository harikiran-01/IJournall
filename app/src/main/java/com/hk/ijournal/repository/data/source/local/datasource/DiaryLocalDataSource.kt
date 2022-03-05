package com.hk.ijournal.repository.data.source.local.datasource

import com.hk.ijournal.repository.data.source.DiaryDataSource
import com.hk.ijournal.repository.data.source.local.dao.DiaryDao
import com.hk.ijournal.repository.data.source.local.entities.DiaryPage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate

class DiaryLocalDataSource internal constructor(
    private val diaryDao: DiaryDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO): DiaryDataSource {

    override suspend fun insertPage(page: DiaryPage): Long? = withContext(ioDispatcher){
        return@withContext diaryDao.insertPage(page)
    }

    override suspend fun updatePage(page: DiaryPage) = withContext(ioDispatcher){
        diaryDao.updatePage(page)
    }

    override suspend fun getPageforDate(selectedDate: LocalDate, uid: Long): DiaryPage? = withContext(ioDispatcher) {
        return@withContext diaryDao.getPageforDate(selectedDate, uid)
    }

    override suspend fun getPageIdForDate(selectedDate: LocalDate, uid: Long): Long? = withContext(ioDispatcher){
        return@withContext diaryDao.getPageIdForDate(selectedDate, uid)
    }
}