package com.hk.ijournal.repository.data.source

import com.hk.ijournal.repository.data.source.local.entities.Page

interface FeedDataSource {
    suspend fun getAllPages(uid: Long): List<Page>
}