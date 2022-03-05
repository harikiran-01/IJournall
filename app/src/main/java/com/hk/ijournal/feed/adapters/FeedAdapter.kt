package com.hk.ijournal.feed.adapters

import androidx.databinding.ViewDataBinding
import com.hk.ijournal.common.base.BaseAdapterViewType
import com.hk.ijournal.common.base.BaseRecyclerAdapter
import com.hk.ijournal.common.base.ViewDataBinder
import com.hk.ijournal.feed.adapters.viewbinders.MiniPageBinder
import javax.inject.Inject

class FeedAdapter @Inject constructor (
    private val miniPageBinder: MiniPageBinder): BaseRecyclerAdapter<BaseAdapterViewType>() {

    init {
        initViewDataBinders()
    }

    override fun getSupportedViewDataBinder(): List<ViewDataBinder<ViewDataBinding, BaseAdapterViewType>> {
        val viewDataBinders = ArrayList<ViewDataBinder<*, *>>(1)
        viewDataBinders.add(miniPageBinder)
        return viewDataBinders as List<ViewDataBinder<ViewDataBinding, BaseAdapterViewType>>
    }

}