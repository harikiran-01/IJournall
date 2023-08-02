package com.hk.ijournal.features.search.adapters

import androidx.databinding.ViewDataBinding
import com.hk.ijournal.features.search.adapters.binders.SearchResultsBinder
import omni.platform.android.components.android.adapters.BaseAdapterViewType
import omni.platform.android.components.android.adapters.BaseRecyclerAdapter
import omni.platform.android.components.android.adapters.ViewDataBinder

class SearchAdapter(
    private val searchResultsBinder: SearchResultsBinder
) : BaseRecyclerAdapter<BaseAdapterViewType>() {
    init {
        initViewDataBinders()
    }

    override fun getSupportedViewDataBinder(): List<ViewDataBinder<ViewDataBinding, BaseAdapterViewType>> {
        val viewDataBinders = ArrayList<ViewDataBinder<*, *>>(1)
        viewDataBinders.add(searchResultsBinder)
        return viewDataBinders as List<ViewDataBinder<ViewDataBinding, BaseAdapterViewType>>
    }
}