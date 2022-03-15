package com.hk.ijournal.utils

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.hk.ijournal.common.base.ITEM_DAY_IMAGE
import com.hk.ijournal.common.base.ITEM_DAY_TEXT
import com.hk.ijournal.dayentry.models.ImageContent
import com.hk.ijournal.dayentry.models.PageContentModel
import com.hk.ijournal.dayentry.models.TextModel
import java.lang.reflect.Type


class ContentTypeAdapter : JsonDeserializer<PageContentModel?> {
    private val LOG_TAG = ContentTypeAdapter::class.java.simpleName

    @Throws(JsonParseException::class)
    override fun deserialize(
        jsonElement: JsonElement,
        type: Type?,
        jsonDeserializationContext: JsonDeserializationContext
    ): PageContentModel? {
            val jsonObject = jsonElement.asJsonObject
            val viewType = jsonObject[VIEW_TYPE].asInt
            return when (viewType) {
                ITEM_DAY_TEXT -> jsonDeserializationContext.deserialize(jsonObject, TextModel::class.java)
                ITEM_DAY_IMAGE -> jsonDeserializationContext.deserialize(jsonObject, ImageContent::class.java)
                else -> null
            }


//            return when (jsonObject[VIEW_TYPE].asNumber) {
//            ITEM_DAY_TEXT -> jsonDeserializationContext.deserialize(jsonObject, TextModel::class.java)
//            ITEM_DAY_IMAGE -> jsonDeserializationContext.deserialize(jsonObject, ImageContent::class.java)
//            else -> {
//                null
//            }
//        }
    }

    companion object {
        private const val VIEW_TYPE = "viewType"
    }
}