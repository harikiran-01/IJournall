package com.hk.ijournal.features.feed.datasource

import com.hk.ijournal.features.dayentry.models.Page

interface FeedDataSource {
    suspend fun getAllPages(uid: Long): List<Page>
}