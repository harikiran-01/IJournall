package com.hk.ijournal.repository.local

import androidx.room.*
import com.hk.ijournal.repository.models.DiaryPage
import com.hk.ijournal.utils.DateConverter
import java.time.LocalDate
@Dao
@TypeConverters(DateConverter::class)
interface DiaryDao : RoomDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertPage(page: DiaryPage): Long

    @Update
    suspend fun updatePage(page: DiaryPage)

    @Query("select * from diarytable where selectedDate=:selectedDate and uid=:uid")
    suspend fun getPageforDate(selectedDate: LocalDate, uid: Long): DiaryPage?

    @Query("select pid from diarytable where selectedDate=:selectedDate and uid=:uid")
    suspend fun getPageIdForDate(selectedDate: LocalDate, uid: Long): Long?

}