package com.hk.ijournal.dayentry.models

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import com.hk.ijournal.common.base.BaseAdapterViewType
import com.hk.ijournal.common.base.ITEM_MINI_PAGE
import com.hk.ijournal.dayentry.models.content.BaseEntity
import com.hk.ijournal.dayentry.models.content.ContentData
import com.hk.ijournal.repository.data.source.local.entities.User
import com.hk.ijournal.utils.DateConverter
import com.hk.ijournal.utils.DayEntryConverter
import java.time.LocalDate

@Entity(tableName = "diarytable",
        foreignKeys = [ForeignKey(
                entity = User::class, parentColumns = ["uid"],
                childColumns = ["uid"],
                onDelete = CASCADE)], indices = [Index("pid", "uid")])
@TypeConverters(DateConverter::class, DayEntryConverter::class)
data class Page(var selectedDate: LocalDate, val uid: Long, var title: String, var contentList: List<BaseEntity<ContentData>>) : BaseAdapterViewType {
    @PrimaryKey(autoGenerate = true)
    var pid = 0L

    @RequiresApi(Build.VERSION_CODES.O)
    constructor() : this(LocalDate.now(), 0, "", listOf())

    @Ignore
    constructor(selectedDate: LocalDate, uid: Long) : this(selectedDate, uid, "", listOf())

    @Ignore
    override var viewType: Int = ITEM_MINI_PAGE
}
