package com.hk.ijournal.domain

import com.hk.ijournal.repository.DiaryRepository
import com.hk.ijournal.repository.data.source.local.entities.DiaryPage
import java.time.LocalDate
import javax.inject.Inject

class PageUseCaseImpl internal constructor(private val diaryRepository: DiaryRepository): PageUseCase {

    override suspend fun insertPage(page: DiaryPage): Long? {
        return diaryRepository.insertPage(page)
    }

    override suspend fun updatePage(page: DiaryPage) {
        return diaryRepository.updatePage(page)
    }

    override suspend fun getPageforDate(selectedDate: LocalDate, uid: Long): DiaryPage? {
        return diaryRepository.getPageforDate(selectedDate, uid)
    }

    override suspend fun getPageIdForDate(selectedDate: LocalDate, uid: Long): Long? {
        return diaryRepository.getPageIdForDate(selectedDate, uid)
    }
}