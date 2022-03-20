package com.hk.ijournal.dayentry.adapters

import androidx.databinding.ViewDataBinding
import com.hk.ijournal.common.base.BaseAdapterViewType
import com.hk.ijournal.common.base.BaseRecyclerAdapter
import com.hk.ijournal.common.base.ViewDataBinder
import com.hk.ijournal.dayentry.adapters.binders.ImageContentBinder
import com.hk.ijournal.dayentry.adapters.binders.TextContentBinder
import javax.inject.Inject

class EntryContentAdapter @Inject constructor (
    private val textContentBinder: TextContentBinder,
    private val imageContentBinder: ImageContentBinder): BaseRecyclerAdapter<BaseAdapterViewType>() {

    init {
        initViewDataBinders()
    }

    override fun getSupportedViewDataBinder(): List<ViewDataBinder<ViewDataBinding, BaseAdapterViewType>> {
        val viewDataBinders = ArrayList<ViewDataBinder<*, *>>(2)
        viewDataBinders.add(textContentBinder)
        viewDataBinders.add(imageContentBinder)
        return viewDataBinders as List<ViewDataBinder<ViewDataBinding, BaseAdapterViewType>>
    }
}