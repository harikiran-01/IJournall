package com.hk.ijournal.feed.adapters.viewbinders

import android.view.LayoutInflater
import android.view.ViewGroup
import com.hk.ijournal.common.base.ITEM_MINI_PAGE
import com.hk.ijournal.common.base.ViewDataBinder
import com.hk.ijournal.databinding.FeedMiniPageItemBinding
import com.hk.ijournal.dayentry.models.TextModel
import com.hk.ijournal.repository.data.source.local.entities.Page
import javax.inject.Inject

class LandingFeedBinder @Inject constructor() : ViewDataBinder<FeedMiniPageItemBinding, Page>() {
    override val viewType: Int
        get() = ITEM_MINI_PAGE

    override fun createBinder(parent: ViewGroup): FeedMiniPageItemBinding {
        return FeedMiniPageItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
    }

    override fun bindData(binding: FeedMiniPageItemBinding, data: Page, position: Int) {
        binding.apply {
            dateWithMonth.text = data.selectedDate.toString()
            title.text = data.title
            //Get the substring with length of min(120, content.length - 1)
            val textModel = data.contentList.find { it is TextModel } as TextModel
            content.text = textModel.content.substring(0..if (textModel.content.length>=120) 119
            else textModel.content.length-1)
        }
    }
}