package com.hk.ijournal.repository.data.source.local.entities

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import com.hk.ijournal.utils.DateConverter
import java.time.LocalDate

@Entity(tableName = "diarytable",
        foreignKeys = [ForeignKey(
                entity = DiaryUser::class, parentColumns = ["uid"],
                childColumns = ["uid"],
                onDelete = CASCADE)], indices = [Index("pid", "uid")])
@TypeConverters(DateConverter::class)
data class DiaryPage(var selectedDate: LocalDate, var uid: Long, var content: String, var rating: Int) {
    @PrimaryKey(autoGenerate = true)
    var pid: Long = 0

    @RequiresApi(Build.VERSION_CODES.O)
    constructor() : this(LocalDate.now(), 0, "", 0)

    @Ignore
    constructor(selectedDate: LocalDate, uid: Long) : this(selectedDate, uid, "", 0)

}
