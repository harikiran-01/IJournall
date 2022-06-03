package com.hk.ijournal.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.hk.ijournal.features.dayentry.models.content.BaseEntity
import com.hk.ijournal.features.dayentry.models.content.ContentData

object DayEntryConverter {

    private val gson: Gson = GsonBuilder().registerTypeAdapter(BaseEntity::class.java, ContentTypeAdapter()).create()

    @JvmStatic
    @TypeConverter
    fun fromPageContentToJson(contentList : List<BaseEntity<ContentData>>) : String {
        val type = object : TypeToken<ArrayList<BaseEntity<ContentData>>>(){}.type
        return gson.toJson(contentList,type)
    }

    @JvmStatic
    @TypeConverter
    fun fromJsonToPageContent(pageContentJson : String): List<BaseEntity<ContentData>> {
        val type = object : TypeToken<ArrayList<BaseEntity<ContentData>>>(){}.type
        return gson.fromJson(pageContentJson, type)
    }
}
