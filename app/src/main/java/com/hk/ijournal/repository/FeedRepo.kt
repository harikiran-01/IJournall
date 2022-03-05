package com.hk.ijournal.repository

import com.hk.ijournal.repository.data.source.local.entities.DiaryPage

interface FeedRepo {
    suspend fun getAllPages(uid: Long): List<DiaryPage>
}