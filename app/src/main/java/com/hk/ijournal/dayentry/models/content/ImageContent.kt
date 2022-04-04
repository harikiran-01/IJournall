package com.hk.ijournal.dayentry.models.content

import com.hk.ijournal.common.base.BaseAdapterViewType
import com.hk.ijournal.common.base.ITEM_DAY_IMAGE

data class ImageContent (var imageUri: String, var description: String) :
    ContentData(), BaseAdapterViewType {

    override var viewType: Int = ITEM_DAY_IMAGE

    constructor() : this("", "")
}