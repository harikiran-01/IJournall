package com.hk.ijournal.features.dayentry.edit.adapters

import androidx.databinding.ViewDataBinding
import com.hk.ijournal.features.dayentry.edit.adapters.binders.MediaContentBinder
import com.hk.ijournal.features.dayentry.edit.adapters.binders.TextContentBinder
import omni.platform.android.components.android.adapters.BaseAdapterViewType
import omni.platform.android.components.android.adapters.BaseRecyclerAdapter
import omni.platform.android.components.android.adapters.ViewDataBinder
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