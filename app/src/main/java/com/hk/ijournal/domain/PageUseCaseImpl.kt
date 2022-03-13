package com.hk.ijournal.domain

import com.hk.ijournal.repository.DiaryRepository
import com.hk.ijournal.repository.data.source.local.entities.Page
import java.time.LocalDate

class PageUseCaseImpl internal constructor(private val diaryRepository: DiaryRepository): PageUseCase {

    override suspend fun insertPage(page: Page): Long? {
        return diaryRepository.insertPage(page)
    }

    override suspend fun updatePage(page: Page) {
        return diaryRepository.updatePage(page)
    }

    override suspend fun getPageforDate(selectedDate: LocalDate, uid: Long): Page? {
        return diaryRepository.getPageforDate(selectedDate, uid)
    }

    override suspend fun getPageIdForDate(selectedDate: LocalDate, uid: Long): Long? {
        return diaryRepository.getPageIdForDate(selectedDate, uid)
    }
}