package com.hk.ijournal.features.dayentry.models.content

import omni.platform.android.components.android.adapters.BaseAdapterViewType
import omni.platform.android.components.android.adapters.ITEM_DAY_IMAGE

data class MediaContent (val mediaUri: String, val mimeType: String, var description: String) :
    ContentData(), BaseAdapterViewType {

    override var viewType: Int = ITEM_DAY_IMAGE

    constructor() : this("", "","")
}