package com.hk.ijournal.features.dayentry.models.content

import androidx.annotation.Keep
import bliss.platform.android.components.android.BaseAdapterViewType
import bliss.platform.android.components.android.ITEM_DAY_TEXT

@Keep
data class TextContent constructor(
    var content : String,
    var hexCode : String,
) : ContentData(), BaseAdapterViewType {

    override val viewType: Int = ITEM_DAY_TEXT

    constructor() : this("", "")
}
