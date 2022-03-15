package com.hk.ijournal.dayentry.models

import androidx.annotation.Keep
import com.hk.ijournal.common.base.BaseAdapterViewType
import com.hk.ijournal.common.base.ITEM_DAY_CONTENT
import com.hk.ijournal.common.base.ITEM_DAY_TEXT

@Keep
data class TextModel constructor(
    var content : String,
    var hexCode : String,
) : BaseAdapterViewType, PageContentModel {
    override val viewType: Int = ITEM_DAY_TEXT
    override val parentViewType: Int = ITEM_DAY_CONTENT

    constructor() : this("", "")
}
