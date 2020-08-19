package com.hk.ijournal.repository.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

enum class ImageSource {
    EXTERNAL, INTERNAL
}

@Entity(tableName = "albumtable",
        foreignKeys = [ForeignKey(
                entity = DiaryPage::class, parentColumns = ["pid"],
                childColumns = ["pid"],
                onDelete = ForeignKey.CASCADE)], indices = [Index("aid", "pid")])
data class DayAlbum(var pid: Long, var imageUriString: String, var imageSource: String) {
    @PrimaryKey(autoGenerate = true)
    var aid: Long = 0

    constructor() : this(0, "", "")
}