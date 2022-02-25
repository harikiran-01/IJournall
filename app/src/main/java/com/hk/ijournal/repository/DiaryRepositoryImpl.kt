package com.hk.ijournal.repository

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.hk.ijournal.repository.data.source.local.*
import com.hk.ijournal.repository.data.source.local.entities.DayAlbum
import com.hk.ijournal.repository.data.source.local.entities.DiaryPage
import com.hk.ijournal.repository.data.source.local.entities.DiaryUser
import com.hk.ijournal.repository.data.source.local.entities.ImageSource
import com.hk.ijournal.repository.models.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDate
import java.util.*

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