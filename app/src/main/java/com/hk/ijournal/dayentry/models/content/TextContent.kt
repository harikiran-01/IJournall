package com.hk.ijournal.dayentry.models.content

import androidx.annotation.Keep
import com.hk.ijournal.common.base.BaseAdapterViewType
import com.hk.ijournal.common.base.ITEM_DAY_TEXT

@Keep
data class TextContent constructor(
    var content : String,
    var hexCode : String,
) : ContentData(), BaseAdapterViewType {

    override val viewType: Int = ITEM_DAY_TEXT

    constructor() : this("", "")
}
