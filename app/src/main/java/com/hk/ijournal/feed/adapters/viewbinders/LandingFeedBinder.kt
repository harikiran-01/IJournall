package com.hk.ijournal.feed.adapters.viewbinders

import android.view.LayoutInflater
import android.view.ViewGroup
import com.hk.ijournal.common.base.ITEM_MINI_PAGE
import com.hk.ijournal.common.base.ViewDataBinder
import com.hk.ijournal.databinding.FeedMiniPageItemBinding
import com.hk.ijournal.dayentry.models.CONTENT_TEXT
import com.hk.ijournal.dayentry.models.Page
import com.hk.ijournal.dayentry.models.content.TextContent

class LandingFeedBinder (private val onPageSelectedListener : (Long) -> Unit) : ViewDataBinder<FeedMiniPageItemBinding, Page>() {
    override val viewType: Int
        get() = ITEM_MINI_PAGE


    override fun createBinder(parent: ViewGroup): FeedMiniPageItemBinding {
        return FeedMiniPageItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
    }

    override fun bindData(
        binding: FeedMiniPageItemBinding,
        data: Page,
        position: Int,
        adapterPosition: Int
    ) {
        binding.apply {
            dateWithMonth.text = data.selectedDate.toString()
            title.text = data.title
            //Get the substring with length of min(120, content.length - 1)
            val textModel = data.contentList.find { it.type == CONTENT_TEXT }?.data as TextContent
            content.text = textModel.content.substring(0..if (textModel.content.length>=120) 119
            else textModel.content.length-1).split("\n")[0]

            root.setOnClickListener {
                onPageSelectedListener.invoke(data.pid)
            }
        }
    }
}