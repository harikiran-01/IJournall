package com.hk.ijournal.domain

import com.hk.ijournal.repository.data.source.local.entities.DiaryPage

interface FeedUseCase {
    suspend fun getAllPages(uid: Long): List<DiaryPage>
}