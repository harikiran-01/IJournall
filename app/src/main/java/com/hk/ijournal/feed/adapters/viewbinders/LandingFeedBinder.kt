package com.hk.ijournal.feed.adapters.viewbinders

import android.view.LayoutInflater
import android.view.ViewGroup
import com.hk.ijournal.common.base.ITEM_MINI_PAGE
import com.hk.ijournal.common.base.ViewDataBinder
import com.hk.ijournal.databinding.FeedMiniPageItemBinding
import com.hk.ijournal.repository.data.source.local.entities.DiaryPage
import javax.inject.Inject

class LandingFeedBinder @Inject constructor() : ViewDataBinder<FeedMiniPageItemBinding, DiaryPage>() {
    override val viewType: Int
        get() = ITEM_MINI_PAGE

    override fun createBinder(parent: ViewGroup): FeedMiniPageItemBinding {
        return FeedMiniPageItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
    }

    override fun bindData(binding: FeedMiniPageItemBinding, data: DiaryPage, position: Int) {
        binding.apply {
            content.text = data.content
            dateWithMonth.text = data.selectedDate.toString()
        }
    }
}