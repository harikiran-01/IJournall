package com.hk.ijournal.domain

import com.hk.ijournal.repository.data.source.local.entities.Page
import java.time.LocalDate

interface PageUseCase {
    suspend fun insertPage(page: Page): Long?

    suspend fun updatePage(page: Page)

    suspend fun getPageforDate(selectedDate: LocalDate, uid: Long): Page?

    suspend fun getPageIdForDate(selectedDate: LocalDate, uid: Long): Long?
}