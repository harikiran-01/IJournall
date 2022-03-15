package com.hk.ijournal.dayentry.models

import com.hk.ijournal.common.base.BaseAdapterViewType
import com.hk.ijournal.common.base.ViewType
import java.io.Serializable

interface PageContentModel : BaseAdapterViewType, Serializable {
    @get:ViewType
    val parentViewType: Int
}