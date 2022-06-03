package com.hk.ijournal.features.dayentry.preview.adapters.binders

import android.annotation.SuppressLint
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.hk.ijournal.common.base.ITEM_DAY_IMAGE
import com.hk.ijournal.common.base.ViewDataBinder
import com.hk.ijournal.common.toPx
import com.hk.ijournal.databinding.ImageWithTitlePreviewBinding
import com.hk.ijournal.features.dayentry.models.content.MediaContent
import javax.inject.Inject

class MediaContentPreviewBinder @Inject constructor() :
    ViewDataBinder<ImageWithTitlePreviewBinding, MediaContent>() {

    private var onItemClickListener: ((MediaContent) -> Unit)? = null

    override val viewType: Int
        get() = ITEM_DAY_IMAGE

    override fun createBinder(parent: ViewGroup): ImageWithTitlePreviewBinding {
        return ImageWithTitlePreviewBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    }

    fun setClickListener(mediaContentClickListener : (MediaContent) -> Unit) {
        onItemClickListener = mediaContentClickListener
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun bindData(
        binding: ImageWithTitlePreviewBinding,
        data: MediaContent,
        position: Int,
        adapterPosition: Int
    ) {
        with(binding) {
            // two way data binding for description
            mediaContent = data
            Glide.with(root.context).load(Uri.parse(data.mediaUri)).apply(
                RequestOptions().override(250.toPx.toInt(), 250.toPx.toInt()).diskCacheStrategy(
                    DiskCacheStrategy.AUTOMATIC)).into(image)
            image.setOnClickListener {
                onItemClickListener?.invoke(data)
            }
        }
    }
}