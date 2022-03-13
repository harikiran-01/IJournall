package com.hk.ijournal.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hk.ijournal.dayentry.models.PageContentModel

object DayEntryConverter {
    @JvmStatic
    @TypeConverter
    fun fromPageContentToJson(contentList : List<PageContentModel>) : String {
        val type = object : TypeToken<List<PageContentModel>>(){}.type
        return Gson().toJson(contentList,type)
    }

    @JvmStatic
    @TypeConverter
    fun fromJsonToPageContent(pageContentJson : String): List<PageContentModel> {
        val type = object : TypeToken<List<PageContentModel?>>() {}.type
        return Gson().fromJson(pageContentJson, type)
    }
}
