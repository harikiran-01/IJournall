package com.hk.ijournal.repository.data.source.local.dao

import androidx.room.*
import com.hk.ijournal.repository.data.source.local.entities.Page
import com.hk.ijournal.utils.DateConverter
import java.time.LocalDate
@Dao
@TypeConverters(DateConverter::class)
interface DiaryDao : RoomDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertPage(page: Page): Long?

    @Update
    suspend fun updatePage(page: Page)

    @Query("select * from diarytable where uid=:uid order by selectedDate desc")
    suspend fun getAllPages(uid: Long): List<Page>

    @Query("select * from diarytable where selectedDate=:selectedDate and uid=:uid")
    suspend fun getPageforDate(selectedDate: LocalDate, uid: Long): Page?

    @Query("select pid from diarytable where selectedDate=:selectedDate and uid=:uid")
    suspend fun getPageIdForDate(selectedDate: LocalDate, uid: Long): Long?
}