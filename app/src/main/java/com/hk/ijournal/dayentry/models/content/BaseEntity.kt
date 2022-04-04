package com.hk.ijournal.dayentry.models.content

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class BaseEntity<WD : ContentData> (

    @SerializedName("type")
    val type: String,

    @SerializedName("data")
    val data: WD

) : Serializable
