package com.hk.ijournal.utils

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.hk.ijournal.dayentry.models.*
import com.hk.ijournal.dayentry.models.content.BaseEntity
import com.hk.ijournal.dayentry.models.content.ContentData
import com.hk.ijournal.dayentry.models.content.ImageContent
import com.hk.ijournal.dayentry.models.content.TextContent
import java.lang.reflect.Type

class ContentTypeAdapter : JsonDeserializer<BaseEntity<ContentData>> {
    private val LOG_TAG = ContentTypeAdapter::class.java.simpleName

    @Throws(JsonParseException::class)
    override fun deserialize(
        jsonElement: JsonElement,
        type: Type?,
        jsonDeserializationContext: JsonDeserializationContext
    ): BaseEntity<ContentData>? {
        if (jsonElement.isJsonObject) {
                val jsonObject = jsonElement.asJsonObject
                if (jsonObject[TYPE] != null) {
                    val viewType = jsonObject[TYPE].asString
                    val objType = when (viewType) {
                        CONTENT_TEXT -> TextContent::class.java
                        CONTENT_IMAGE -> ImageContent::class.java
                        else -> null
                    }
                    val value: JsonElement = jsonObject.get("data")
                    return BaseEntity(viewType, jsonDeserializationContext.deserialize(value, objType))
                }
            }
        return null
    }

    companion object {
        private const val TYPE = "type"
    }
}