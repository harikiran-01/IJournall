package com.hk.ijournal.features.dayentry.repo

import com.hk.ijournal.features.dayentry.models.Page
import java.time.LocalDate

interface DayEntryRepo {
    suspend fun getPageforDate(selectedDate: LocalDate, userId: Long): Page?
    suspend fun updatePage(diaryPage: Page)
    suspend fun insertPage(diaryPage: Page): Long?
    suspend fun getPageForId(pid: Long): Page?
    suspend fun getPageIdForDate(selectedDate: LocalDate, uid: Long): Long?
}