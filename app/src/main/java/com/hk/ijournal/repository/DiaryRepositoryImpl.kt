package com.hk.ijournal.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.hk.ijournal.repository.data.source.local.datasource.DiaryLocalDataSource
import com.hk.ijournal.repository.data.source.local.entities.DiaryPage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
class DiaryRepositoryImpl(private val diaryLocalDataSource: DiaryLocalDataSource, private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO): DiaryRepository {

    override suspend fun getPageforDate(selectedDate: LocalDate, userId: Long): DiaryPage? {
        return diaryLocalDataSource.getPageforDate(selectedDate, userId)
    }

    override suspend fun updatePage(diaryPage: DiaryPage) {
        diaryLocalDataSource.updatePage(diaryPage)
    }

    override suspend fun insertPage(diaryPage: DiaryPage): Long? {
        return diaryLocalDataSource.insertPage(diaryPage)
    }

    override suspend fun getPageIdForDate(selectedDate: LocalDate, uid: Long): Long? {
        return diaryLocalDataSource.getPageIdForDate(selectedDate, uid)
    }
}