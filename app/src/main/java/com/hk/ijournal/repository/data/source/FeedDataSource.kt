package com.hk.ijournal.repository.data.source

import com.hk.ijournal.dayentry.models.Page

interface FeedDataSource {
    suspend fun getAllPages(uid: Long): List<Page>
}