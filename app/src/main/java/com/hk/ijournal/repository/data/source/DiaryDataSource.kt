package com.hk.ijournal.repository.data.source

import com.hk.ijournal.repository.data.source.local.entities.DiaryPage
import java.time.LocalDate

interface DiaryDataSource {
    suspend fun insertPage(page: DiaryPage): Long?

    suspend fun updatePage(page: DiaryPage)

    suspend fun getPageforDate(selectedDate: LocalDate, uid: Long): DiaryPage?

    suspend fun getPageIdForDate(selectedDate: LocalDate, uid: Long): Long?
}