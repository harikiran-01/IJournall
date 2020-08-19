package com.hk.ijournal.utils

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object DateConverter {
    @JvmStatic
    @RequiresApi(api = Build.VERSION_CODES.O)
    @TypeConverter
    fun toDate(dateString: String?): LocalDate? {
        return if (dateString == null) null else LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    }

    @JvmStatic
    @TypeConverter
    fun toString(date: LocalDate?): String? {
        return date?.toString()
    }
}