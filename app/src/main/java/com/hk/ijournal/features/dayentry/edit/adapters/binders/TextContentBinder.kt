package com.hk.ijournal.features.dayentry.edit.adapters.binders

import android.view.LayoutInflater
import android.view.ViewGroup
import com.hk.ijournal.databinding.DayEntryContentBinding
import com.hk.ijournal.features.dayentry.models.content.TextContent
import omni.platform.android.components.android.adapters.BaseAdapterViewType
import omni.platform.android.components.android.adapters.ITEM_DAY_TEXT
import omni.platform.android.components.android.adapters.ViewDataBinder
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

    override fun bindData(
        binding: DayEntryContentBinding,
        data: TextContent,
        position: Int,
        adapterPosition: Int
    ) {
        // two way data binding for text
        binding.textContent = data
    }
}