package com.hk.ijournal.dayentry.adapters.binders

import android.view.LayoutInflater
import android.view.ViewGroup
import com.hk.ijournal.common.base.ITEM_DAY_CONTENT
import com.hk.ijournal.common.base.ITEM_DAY_TEXT
import com.hk.ijournal.common.base.ViewDataBinder
import com.hk.ijournal.databinding.DayEntryContentBinding
import com.hk.ijournal.dayentry.models.PageContentModel
import com.hk.ijournal.dayentry.models.TextModel
import javax.inject.Inject

class TextContentBinder  @Inject constructor() :
    ViewDataBinder<DayEntryContentBinding, TextModel>(), PageContentModel {

    override val viewType: Int
        get() = ITEM_DAY_TEXT

    override val parentViewType: Int
        get() = ITEM_DAY_CONTENT

    override fun createBinder(parent: ViewGroup): DayEntryContentBinding {
        return DayEntryContentBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
    }

    override fun bindData(binding: DayEntryContentBinding, data: TextModel, position: Int) {
        with(binding) {
            content.apply {
                setText(data.content)
                //TODO add and bind formatting data
            }
        }

    }
}