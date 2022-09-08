package com.hk.ijournal.features.search.repo

import com.hk.ijournal.features.dayentry.models.Page

interface SearchRepo {
    suspend fun getResults(uid: Long, searchQuery: String): List<Page>
}