package com.hk.ijournal.features.feed.adapters

import androidx.databinding.ViewDataBinding
import bliss.platform.android.components.android.BaseAdapterViewType
import bliss.platform.android.components.android.BaseRecyclerAdapter
import bliss.platform.android.components.android.ViewDataBinder
import com.hk.ijournal.features.feed.adapters.viewbinders.LandingFeedBinder

class FeedAdapter (
    private val landingFeedBinder: LandingFeedBinder): BaseRecyclerAdapter<BaseAdapterViewType>() {
    init {
        initViewDataBinders()
    }

    override fun getSupportedViewDataBinder(): List<ViewDataBinder<ViewDataBinding, BaseAdapterViewType>> {
        val viewDataBinders = ArrayList<ViewDataBinder<*, *>>(1)
        viewDataBinders.add(landingFeedBinder)
        return viewDataBinders as List<ViewDataBinder<ViewDataBinding, BaseAdapterViewType>>
    }
}