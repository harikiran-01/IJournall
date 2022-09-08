package com.hk.ijournal.features.dayentry.edit.adapters.binders

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import bliss.platform.android.components.android.ITEM_DAY_IMAGE
import bliss.platform.android.components.android.ViewDataBinder
import bliss.platform.android.extensions.toPx
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hk.ijournal.R
import com.hk.ijournal.common.gone
import com.hk.ijournal.common.visible
import com.hk.ijournal.databinding.ImageWithTitleBinding
import com.hk.ijournal.features.dayentry.models.content.MediaContent
import javax.inject.Inject

class MediaContentBinder @Inject constructor() :
    ViewDataBinder<ImageWithTitleBinding, MediaContent>() {

    override val viewType: Int
        get() = ITEM_DAY_IMAGE

    override fun createBinder(parent: ViewGroup): ImageWithTitleBinding {
        return ImageWithTitleBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
    }

    override fun bindData(
        binding: ImageWithTitleBinding,
        data: MediaContent,
        position: Int,
        adapterPosition: Int
    ) {
        with(binding) {
            // two way data binding for description
            mediaContent = data
            when (data.mimeType) {
                "image" -> {
                    Glide.with(binding.root).load(Uri.parse(data.mediaUri)).placeholder(R.drawable.spinnerloop).apply(
                        RequestOptions().override(250.toPx, 250.toPx))
                        .into(image)
                    image.visible()
                    video.gone()
                }
                "video" -> {
                    image.gone()
                    video.visible()
                    video.setVideoURI(Uri.parse(data.mediaUri))
                    video.seekTo(1)
                    video.setOnClickListener {
                        if (video.isPlaying)
                            video.pause()
                        else
                            video.start()
                    }
                }
                else -> {
                    //
                }
            }

        }
    }
}