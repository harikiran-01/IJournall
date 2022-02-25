package com.hk.ijournal.repository

import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.hk.ijournal.repository.data.source.local.entities.DayAlbum
import com.hk.ijournal.repository.data.source.local.entities.DiaryPage
import kotlinx.coroutines.flow.Flow
import java.io.ByteArrayOutputStream
import java.io.File
import java.time.LocalDate

interface DiaryRepository {
    suspend fun getPageforDate(selectedDate: LocalDate, userId: Long): DiaryPage?
    suspend fun updatePage(diaryPage: DiaryPage)
    suspend fun insertPage(diaryPage: DiaryPage): Long?
    suspend fun getPageIdForDate(selectedDate: LocalDate, uid: Long): Long?
}