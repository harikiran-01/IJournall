package com.hk.ijournal.dayentry.models

import com.hk.ijournal.common.base.BaseAdapterViewType
import com.hk.ijournal.common.base.ViewType

interface PageContentModel : BaseAdapterViewType{
    @get:ViewType
    val parentViewType: Int
}