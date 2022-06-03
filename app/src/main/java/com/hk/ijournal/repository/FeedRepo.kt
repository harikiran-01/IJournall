package com.hk.ijournal.repository

import com.hk.ijournal.features.dayentry.models.Page

interface FeedRepo {
    suspend fun getAllPages(uid: Long): List<Page>
}