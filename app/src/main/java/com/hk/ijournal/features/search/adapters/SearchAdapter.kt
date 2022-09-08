package com.hk.ijournal.features.search.adapters

import androidx.databinding.ViewDataBinding
import bliss.platform.android.components.android.BaseAdapterViewType
import bliss.platform.android.components.android.BaseRecyclerAdapter
import bliss.platform.android.components.android.ViewDataBinder
import com.hk.ijournal.features.search.adapters.binders.SearchResultsBinder

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