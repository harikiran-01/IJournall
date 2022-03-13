package com.hk.ijournal.dayentry.adapters.binders

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hk.ijournal.R
import com.hk.ijournal.common.base.ITEM_DAY_CONTENT
import com.hk.ijournal.common.base.ITEM_DAY_IMAGE
import com.hk.ijournal.common.base.ViewDataBinder
import com.hk.ijournal.databinding.ImageWithTitleBinding
import com.hk.ijournal.dayentry.models.PageContentModel
import com.hk.ijournal.repository.data.source.local.entities.ImageContent
import javax.inject.Inject

class ImageContentBinder @Inject constructor() :
    ViewDataBinder<ImageWithTitleBinding, ImageContent>(), PageContentModel {

    override val viewType: Int
        get() = ITEM_DAY_IMAGE

    override val parentViewType: Int
        get() = ITEM_DAY_CONTENT

    override fun createBinder(parent: ViewGroup): ImageWithTitleBinding {
        return ImageWithTitleBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
    }

    override fun bindData(binding: ImageWithTitleBinding, data: ImageContent, position: Int) {
        with(binding) {
            Glide.with(binding.root).load(Uri.parse(data.imageUri)).placeholder(R.drawable.spinnerloop).apply(
                RequestOptions().override(200, 200)).centerCrop().thumbnail(0.4f)
                .into(image)
            imageTitle.setText(data.description)
        }
    }
}