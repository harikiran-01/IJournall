package com.hk.ijournal.features.dayentry.models.content

import bliss.platform.android.components.android.BaseAdapterViewType
import bliss.platform.android.components.android.ITEM_DAY_IMAGE

data class MediaContent (val mediaUri: String, val mimeType: String, var description: String) :
    ContentData(), BaseAdapterViewType {

    override var viewType: Int = ITEM_DAY_IMAGE

    constructor() : this("", "","")
}