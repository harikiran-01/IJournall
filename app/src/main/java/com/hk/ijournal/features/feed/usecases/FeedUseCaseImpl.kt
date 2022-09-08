package com.hk.ijournal.features.feed.usecases

import com.hk.ijournal.features.dayentry.models.Page
import com.hk.ijournal.features.feed.repo.FeedRepo

class FeedUseCaseImpl internal constructor(private val feedRepo: FeedRepo) : FeedUseCase {
    override suspend fun getAllPages(uid: Long): List<Page> {
        return feedRepo.getAllPages(uid)
    }
}