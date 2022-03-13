package com.hk.ijournal.domain

import com.hk.ijournal.repository.FeedRepo
import com.hk.ijournal.repository.data.source.local.entities.Page

class FeedUseCaseImpl internal constructor(private val feedRepo: FeedRepo) : FeedUseCase {
    override suspend fun getAllPages(uid: Long): List<Page> {
        return feedRepo.getAllPages(uid)
    }
}