package com.hk.ijournal.features.feed.adapters

import androidx.databinding.ViewDataBinding
import com.hk.ijournal.features.feed.adapters.viewbinders.LandingFeedBinder
import omni.platform.android.components.android.adapters.BaseAdapterViewType
import omni.platform.android.components.android.adapters.BaseRecyclerAdapter
import omni.platform.android.components.android.adapters.ViewDataBinder

class FeedAdapter (
    private val landingFeedBinder: LandingFeedBinder
): BaseRecyclerAdapter<BaseAdapterViewType>() {
    init {
        initViewDataBinders()
    }

    override fun getSupportedViewDataBinder(): List<ViewDataBinder<ViewDataBinding, BaseAdapterViewType>> {
        val viewDataBinders = ArrayList<ViewDataBinder<*, *>>(1)
        viewDataBinders.add(landingFeedBinder)
        return viewDataBinders as List<ViewDataBinder<ViewDataBinding, BaseAdapterViewType>>
    }
}