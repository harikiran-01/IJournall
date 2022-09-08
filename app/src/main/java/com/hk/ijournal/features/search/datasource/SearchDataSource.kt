package com.hk.ijournal.features.search.datasource

import com.hk.ijournal.features.dayentry.models.Page

interface SearchDataSource {
    suspend fun getResults(uid: Long, searchQuery: String): List<Page>
}