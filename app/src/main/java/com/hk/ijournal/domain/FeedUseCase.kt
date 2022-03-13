package com.hk.ijournal.domain

import com.hk.ijournal.repository.data.source.local.entities.Page

interface FeedUseCase {
    suspend fun getAllPages(uid: Long): List<Page>
}