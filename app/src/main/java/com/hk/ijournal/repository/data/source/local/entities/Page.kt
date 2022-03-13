package com.hk.ijournal.repository.data.source.local.entities

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import com.hk.ijournal.common.base.ITEM_DAY_CONTENT
import com.hk.ijournal.dayentry.models.PageContentModel
import com.hk.ijournal.utils.DateConverter
import com.hk.ijournal.utils.DayEntryConverter
import java.time.LocalDate

@Entity(tableName = "diarytable",
        foreignKeys = [ForeignKey(
                entity = User::class, parentColumns = ["uid"],
                childColumns = ["uid"],
                onDelete = CASCADE)], indices = [Index("pid", "uid")])
@TypeConverters(DateConverter::class, DayEntryConverter::class)
data class Page(val selectedDate: LocalDate, val uid: Long, var title: String, var contentList: List<PageContentModel>) : PageContentModel {
    @PrimaryKey(autoGenerate = true)
    var pid: Long = 0

    @RequiresApi(Build.VERSION_CODES.O)
    constructor() : this(LocalDate.now(), 0, "", listOf())

    @Ignore
    constructor(selectedDate: LocalDate, uid: Long) : this(selectedDate, uid, "", listOf())

    @Ignore
    override var parentViewType: Int = ITEM_DAY_CONTENT

    @Ignore
    override var viewType: Int = ITEM_DAY_CONTENT
}
