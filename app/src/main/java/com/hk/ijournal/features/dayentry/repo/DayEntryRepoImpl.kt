package com.hk.ijournal.features.dayentry.repo

import android.os.Build
import androidx.annotation.RequiresApi
import com.hk.ijournal.features.dayentry.models.Page
import com.hk.ijournal.features.dayentry.repo.data.source.local.datasource.DayEntryLocalDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
class DayEntryRepoImpl(private val dayEntryLocalDataSource : DayEntryLocalDataSource, private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO):
    DayEntryRepo {

    override suspend fun getPageforDate(selectedDate: LocalDate, userId: Long): Page? {
        return dayEntryLocalDataSource.getPageforDate(selectedDate, userId)
    }

    override suspend fun updatePage(diaryPage: Page) {
        dayEntryLocalDataSource.updatePage(diaryPage)
    }

    override suspend fun insertPage(diaryPage: Page): Long? {
        return dayEntryLocalDataSource.insertPage(diaryPage)
    }

    override suspend fun getPageForId(pid: Long): Page? {
        return dayEntryLocalDataSource.getPageForId(pid)
    }

    override suspend fun getPageIdForDate(selectedDate: LocalDate, uid: Long): Long? {
        return dayEntryLocalDataSource.getPageIdForDate(selectedDate, uid)
    }
}