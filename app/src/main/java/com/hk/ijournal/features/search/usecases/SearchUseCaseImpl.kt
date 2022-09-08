package com.hk.ijournal.features.search.usecases

import com.hk.ijournal.features.dayentry.models.Page
import com.hk.ijournal.features.search.repo.SearchRepo

class SearchUseCaseImpl internal constructor(private val searchRepo: SearchRepo) : SearchUseCase {

    override suspend fun getResults(uid: Long, searchQuery: String): List<Page> {
        return searchRepo.getResults(uid, searchQuery)
    }
}