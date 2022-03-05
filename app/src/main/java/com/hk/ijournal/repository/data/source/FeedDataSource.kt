package com.hk.ijournal.repository.data.source

import com.hk.ijournal.repository.data.source.local.entities.DiaryPage

interface FeedDataSource {
    suspend fun getAllPages(uid: Long): List<DiaryPage>
}