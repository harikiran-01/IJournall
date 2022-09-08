package com.hk.ijournal.features.search.repo

import com.hk.ijournal.features.dayentry.models.Page
import com.hk.ijournal.features.search.datasource.SearchDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class SearchRepoImpl @Inject constructor(private val searchLocalDataSource: SearchDataSource, private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO) :
    SearchRepo {

    override suspend fun getResults(uid: Long, searchQuery: String): List<Page> {
        return searchLocalDataSource.getResults(uid, searchQuery)
    }

}