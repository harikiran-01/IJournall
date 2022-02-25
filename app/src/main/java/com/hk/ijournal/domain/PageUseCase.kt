package com.hk.ijournal.domain

import com.hk.ijournal.repository.data.source.local.entities.DiaryPage
import java.io.ByteArrayOutputStream
import java.io.File
import java.time.LocalDate

interface PageUseCase {
    suspend fun insertPage(page: DiaryPage): Long?

    suspend fun updatePage(page: DiaryPage)

    suspend fun getPageforDate(selectedDate: LocalDate, uid: Long): DiaryPage?

    suspend fun getPageIdForDate(selectedDate: LocalDate, uid: Long): Long?
}