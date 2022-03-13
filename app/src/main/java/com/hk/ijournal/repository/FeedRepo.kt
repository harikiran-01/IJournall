package com.hk.ijournal.repository

import com.hk.ijournal.repository.data.source.local.entities.Page

interface FeedRepo {
    suspend fun getAllPages(uid: Long): List<Page>
}