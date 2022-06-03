package com.hk.ijournal.features.dayentry.repo.data.source.local.datasource

import com.hk.ijournal.features.dayentry.models.Page
import java.time.LocalDate

interface DayEntryDataSource {
    suspend fun insertPage(page: Page): Long?

    suspend fun updatePage(page: Page)

    suspend fun getPageforDate(selectedDate: LocalDate, uid: Long): Page?

    suspend fun getPageForId(pid: Long): Page?

    suspend fun getPageIdForDate(selectedDate: LocalDate, uid: Long): Long?
}