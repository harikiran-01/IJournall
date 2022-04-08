package com.hk.ijournal.dayentry.preview.adapters

import androidx.databinding.ViewDataBinding
import com.hk.ijournal.common.base.BaseAdapterViewType
import com.hk.ijournal.common.base.BaseRecyclerAdapter
import com.hk.ijournal.common.base.ViewDataBinder
import com.hk.ijournal.dayentry.preview.adapters.binders.ImageContentPreviewBinder
import com.hk.ijournal.dayentry.preview.adapters.binders.TextContentPreviewBinder
import javax.inject.Inject

class EntryContentPreviewAdapter @Inject constructor (
    private val textContentPreviewBinder: TextContentPreviewBinder,
    private val imageContentPreviewBinder: ImageContentPreviewBinder): BaseRecyclerAdapter<BaseAdapterViewType>() {

    init {
        initViewDataBinders()
    }

    override fun getSupportedViewDataBinder(): List<ViewDataBinder<ViewDataBinding, BaseAdapterViewType>> {
        val viewDataBinders = ArrayList<ViewDataBinder<*, *>>(2)
        viewDataBinders.add(textContentPreviewBinder)
        viewDataBinders.add(imageContentPreviewBinder)
        return viewDataBinders as List<ViewDataBinder<ViewDataBinding, BaseAdapterViewType>>
    }
}