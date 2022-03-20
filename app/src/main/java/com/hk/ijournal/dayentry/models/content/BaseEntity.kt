package com.hk.ijournal.dayentry.models.content

import com.google.gson.annotations.SerializedName
import com.hk.ijournal.dayentry.models.EntityType
import java.io.Serializable

data class BaseEntity<WD : ContentData> (

    @SerializedName("type")
    val type: String,

    @SerializedName("data")
    val data: WD,

    @get:EntityType
    val entityType: Int = 0,

) : Serializable
