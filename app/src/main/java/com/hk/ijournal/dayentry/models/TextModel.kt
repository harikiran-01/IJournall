package com.hk.ijournal.dayentry.models

import androidx.annotation.Keep
import com.hk.ijournal.common.base.BaseAdapterViewType
import com.hk.ijournal.common.base.ITEM_DAY_CONTENT
import com.hk.ijournal.common.base.ITEM_DAY_TEXT
import java.io.Serializable

@Keep
data class TextModel (
    val content : String,
    val hexCode : String,
    override val viewType: Int = ITEM_DAY_TEXT,
    override val parentViewType: Int = ITEM_DAY_CONTENT
) : Serializable, BaseAdapterViewType, PageContentModel
