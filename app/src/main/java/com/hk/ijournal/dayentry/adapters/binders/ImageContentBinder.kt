package com.hk.ijournal.dayentry.adapters.binders

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hk.ijournal.R
import com.hk.ijournal.common.base.ITEM_DAY_IMAGE
import com.hk.ijournal.common.base.ViewDataBinder
import com.hk.ijournal.common.toPx
import com.hk.ijournal.databinding.ImageWithTitleBinding
import com.hk.ijournal.dayentry.models.content.ImageContent
import javax.inject.Inject

class ImageContentBinder @Inject constructor() :
    ViewDataBinder<ImageWithTitleBinding, ImageContent>() {

    override val viewType: Int
        get() = ITEM_DAY_IMAGE

    override fun createBinder(parent: ViewGroup): ImageWithTitleBinding {
        return ImageWithTitleBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
    }

    override fun bindData(binding: ImageWithTitleBinding, data: ImageContent, position: Int) {
        with(binding) {
            // two way data binding for description
            imageContent = data
            Glide.with(binding.root).load(Uri.parse(data.imageUri)).placeholder(R.drawable.spinnerloop).apply(
                RequestOptions().override(250.toPx.toInt(), 250.toPx.toInt()))
                .into(image)
        }
    }
}