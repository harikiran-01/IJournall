package com.hk.ijournal.dayentry.preview.adapters

import androidx.databinding.ViewDataBinding
import com.hk.ijournal.common.base.BaseAdapterViewType
import com.hk.ijournal.common.base.BaseRecyclerAdapter
import com.hk.ijournal.common.base.ViewDataBinder
import com.hk.ijournal.dayentry.models.content.MediaContent
import com.hk.ijournal.dayentry.preview.adapters.binders.MediaContentPreviewBinder
import com.hk.ijournal.dayentry.preview.adapters.binders.TextContentPreviewBinder
import javax.inject.Inject

class EntryContentPreviewAdapter @Inject constructor (
    private val textContentPreviewBinder: TextContentPreviewBinder,
    private val mediaContentPreviewBinder: MediaContentPreviewBinder): BaseRecyclerAdapter<BaseAdapterViewType>() {

    init {
        initViewDataBinders()
    }

    fun setMediaContentClickListener(mediaContentClickListener : (MediaContent) -> Unit) {
        mediaContentPreviewBinder.setClickListener(mediaContentClickListener)
    }

    override fun getSupportedViewDataBinder(): List<ViewDataBinder<ViewDataBinding, BaseAdapterViewType>> {
        val viewDataBinders = ArrayList<ViewDataBinder<*, *>>(2)
        viewDataBinders.add(textContentPreviewBinder)
        viewDataBinders.add(mediaContentPreviewBinder)
        return viewDataBinders as List<ViewDataBinder<ViewDataBinding, BaseAdapterViewType>>
    }
}