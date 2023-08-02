package com.hk.ijournal.features.dayentry.preview.adapters.binders

import android.view.LayoutInflater
import android.view.ViewGroup
import com.hk.ijournal.databinding.DayentryPreviewContentBinding
import com.hk.ijournal.features.dayentry.models.content.TextContent
import omni.platform.android.components.android.adapters.BaseAdapterViewType
import omni.platform.android.components.android.adapters.ITEM_DAY_TEXT
import omni.platform.android.components.android.adapters.ViewDataBinder
import javax.inject.Inject

class TextContentPreviewBinder @Inject constructor() :
    ViewDataBinder<DayentryPreviewContentBinding, TextContent>(), BaseAdapterViewType {

    override val viewType: Int
        get() = ITEM_DAY_TEXT

    override fun createBinder(parent: ViewGroup): DayentryPreviewContentBinding {
        val binding = DayentryPreviewContentBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return binding
    }

    override fun bindData(
        binding: DayentryPreviewContentBinding,
        data: TextContent,
        position: Int,
        adapterPosition: Int
    ) {
        // two way data binding for text
        binding.textContent = data
    }
}