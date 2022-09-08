package com.hk.ijournal.features.dayentry.preview.adapters

import androidx.databinding.ViewDataBinding
import bliss.platform.android.components.android.BaseAdapterViewType
import bliss.platform.android.components.android.BaseRecyclerAdapter
import bliss.platform.android.components.android.ViewDataBinder
import com.hk.ijournal.features.dayentry.models.content.MediaContent
import com.hk.ijournal.features.dayentry.preview.adapters.binders.MediaContentPreviewBinder
import com.hk.ijournal.features.dayentry.preview.adapters.binders.TextContentPreviewBinder
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