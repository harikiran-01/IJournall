package com.hk.ijournal.features.search.usecases
import com.hk.ijournal.features.dayentry.models.Page

interface SearchUseCase {
    suspend fun getResults(uid: Long, searchQuery: String): List<Page>
}