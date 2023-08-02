package com.hk.ijournal.features.dayentry.models.content

import androidx.annotation.Keep
import omni.platform.android.components.android.adapters.BaseAdapterViewType
import omni.platform.android.components.android.adapters.ITEM_DAY_TEXT

@Keep
data class TextContent constructor(
    var content : String,
    var hexCode : String,
) : ContentData(), BaseAdapterViewType {

    override val viewType: Int = ITEM_DAY_TEXT

    constructor() : this("", "")
}
