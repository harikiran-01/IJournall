package com.hk.ijournal.common.base

import android.os.Bundle
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.hk.ijournal.common.base.BaseAdapterViewType

abstract class ViewDataBinder<V : ViewDataBinding, T : BaseAdapterViewType> {

    @get:ViewType
    abstract val viewType: Int

    abstract fun createBinder(parent: ViewGroup): V

    abstract fun bindData(binding: V, data: T, position: Int)

    fun bindData(binding: V, data: Bundle?, position: Int) {}
}