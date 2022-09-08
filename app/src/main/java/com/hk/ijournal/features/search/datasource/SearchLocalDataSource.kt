package com.hk.ijournal.features.search.datasource

import com.hk.ijournal.features.dayentry.models.Page
import com.hk.ijournal.features.dayentry.repo.data.source.local.dao.DayEntryDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class SearchLocalDataSource internal constructor(
    private val diaryDao: DayEntryDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO) : SearchDataSource {

    override suspend fun getResults(uid: Long, searchQuery: String): List<Page> {
        return diaryDao.getPageForSearchQuery(uid, "%$searchQuery%", "%$searchQuery%")
    }

}