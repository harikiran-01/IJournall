package com.hk.ijournal.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.hk.ijournal.dayentry.models.PageContentModel

object DayEntryConverter {

    private val gson: Gson = GsonBuilder().registerTypeAdapter(ArrayList::class.java, ContentTypeAdapter()).create()

    @JvmStatic
    @TypeConverter
    fun fromPageContentToJson(contentList : List<PageContentModel>) : String {
        val type = object : TypeToken<ArrayList<PageContentModel>>(){}.type
        return gson.toJson(contentList,type)
    }

    @JvmStatic
    @TypeConverter
    fun fromJsonToPageContent(pageContentJson : String): List<PageContentModel> {
        val type = object : TypeToken<ArrayList<PageContentModel>>(){}.type
        return gson.fromJson(pageContentJson, type)
    }
}
