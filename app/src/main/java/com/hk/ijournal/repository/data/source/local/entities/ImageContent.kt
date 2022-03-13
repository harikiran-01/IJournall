package com.hk.ijournal.repository.data.source.local.entities

import com.hk.ijournal.common.base.BaseAdapterViewType
import com.hk.ijournal.common.base.ITEM_DAY_CONTENT
import com.hk.ijournal.common.base.ITEM_DAY_IMAGE
import com.hk.ijournal.dayentry.models.PageContentModel

data class ImageContent (var imageUri: String, var description: String) :
    PageContentModel, BaseAdapterViewType {

    override var parentViewType: Int = ITEM_DAY_CONTENT

    override var viewType: Int = ITEM_DAY_IMAGE
}