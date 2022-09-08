package com.hk.ijournal.features.dayentry.edit.adapters

import androidx.databinding.ViewDataBinding
import bliss.platform.android.components.android.BaseAdapterViewType
import bliss.platform.android.components.android.BaseRecyclerAdapter
import bliss.platform.android.components.android.ViewDataBinder
import com.hk.ijournal.features.dayentry.edit.adapters.binders.MediaContentBinder
import com.hk.ijournal.features.dayentry.edit.adapters.binders.TextContentBinder
import javax.inject.Inject

class EntryContentAdapter @Inject constructor (
    private val textContentBinder: TextContentBinder,
    private val mediaContentBinder: MediaContentBinder): BaseRecyclerAdapter<BaseAdapterViewType>() {

    init {
        initViewDataBinders()
    }

    override fun getSupportedViewDataBinder(): List<ViewDataBinder<ViewDataBinding, BaseAdapterViewType>> {
        val viewDataBinders = ArrayList<ViewDataBinder<*, *>>(2)
        viewDataBinders.add(textContentBinder)
        viewDataBinders.add(mediaContentBinder)
        return viewDataBinders as List<ViewDataBinder<ViewDataBinding, BaseAdapterViewType>>
    }
}