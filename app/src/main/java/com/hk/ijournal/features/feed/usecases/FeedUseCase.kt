package com.hk.ijournal.features.feed.usecases

import com.hk.ijournal.features.dayentry.models.Page

interface FeedUseCase {
    suspend fun getAllPages(uid: Long): List<Page>
}