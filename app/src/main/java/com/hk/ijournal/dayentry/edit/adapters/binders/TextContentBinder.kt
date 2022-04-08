package com.hk.ijournal.dayentry.edit.adapters.binders

import android.view.LayoutInflater
import android.view.ViewGroup
import com.hk.ijournal.common.base.BaseAdapterViewType
import com.hk.ijournal.common.base.ITEM_DAY_TEXT
import com.hk.ijournal.common.base.ViewDataBinder
import com.hk.ijournal.databinding.DayEntryContentBinding
import com.hk.ijournal.dayentry.models.content.TextContent
import javax.inject.Inject

class TextContentBinder @Inject constructor() :
    ViewDataBinder<DayEntryContentBinding, TextContent>(), BaseAdapterViewType {

    override val viewType: Int
        get() = ITEM_DAY_TEXT

    override fun createBinder(parent: ViewGroup): DayEntryContentBinding {
        val binding = DayEntryContentBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return binding
    }

    override fun bindData(binding: DayEntryContentBinding, data: TextContent, position: Int) {
        // two way data binding for text
        binding.textContent = data
    }
}