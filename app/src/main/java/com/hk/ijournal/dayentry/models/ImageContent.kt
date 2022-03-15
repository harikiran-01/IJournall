package com.hk.ijournal.dayentry.models

import com.hk.ijournal.common.base.ITEM_DAY_CONTENT
import com.hk.ijournal.common.base.ITEM_DAY_IMAGE

data class ImageContent (var imageUri: String, var description: String) :
    PageContentModel {

    override var parentViewType: Int = ITEM_DAY_CONTENT

    override var viewType: Int = ITEM_DAY_IMAGE

    constructor() : this("", "")
}