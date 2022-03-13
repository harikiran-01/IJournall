package com.hk.ijournal.repository

import com.hk.ijournal.repository.data.source.local.entities.Page
import java.time.LocalDate

interface DiaryRepository {
    suspend fun getPageforDate(selectedDate: LocalDate, userId: Long): Page?
    suspend fun updatePage(diaryPage: Page)
    suspend fun insertPage(diaryPage: Page): Long?
    suspend fun getPageIdForDate(selectedDate: LocalDate, uid: Long): Long?
}