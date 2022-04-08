package com.hk.ijournal.domain

import com.hk.ijournal.dayentry.models.Page
import com.hk.ijournal.dayentry.repo.DayEntryRepo
import java.time.LocalDate

class PageUseCaseImpl internal constructor(private val dayEntryRepo: DayEntryRepo): PageUseCase {

    override suspend fun insertPage(page: Page): Long? {
        return dayEntryRepo.insertPage(page)
    }

    override suspend fun updatePage(page: Page) {
        return dayEntryRepo.updatePage(page)
    }

    override suspend fun getPageforDate(selectedDate: LocalDate, uid: Long): Page? {
        return dayEntryRepo.getPageforDate(selectedDate, uid)
    }

    override suspend fun getPageForId(pid: Long): Page? {
        return dayEntryRepo.getPageForId(pid)
    }

    override suspend fun getPageIdForDate(selectedDate: LocalDate, uid: Long): Long? {
        return dayEntryRepo.getPageIdForDate(selectedDate, uid)
    }
}