package com.hk.ijournal.features.mediadetail.view

import android.net.Uri
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.hk.ijournal.databinding.FragMediaDetailBinding
import com.hk.ijournal.features.mediadetail.viewmodel.MediaDetailVM
import dagger.hilt.android.AndroidEntryPoint
import omni.platform.android.components.android.BaseBindingFragment

@AndroidEntryPoint
class MediaDetailFragment : BaseBindingFragment<FragMediaDetailBinding, MediaDetailVM>() {
    private val safeArgs : MediaDetailFragmentArgs by navArgs()

    override fun getViewModelClass(): Class<MediaDetailVM> {
        return MediaDetailVM::class.java
    }

    override fun getViewBinding(): FragMediaDetailBinding {
        return FragMediaDetailBinding.inflate(layoutInflater)
    }

    override fun setUpViews() {
        super.setUpViews()
        with(binding) {
            Glide.with(root.context).load(Uri.parse(safeArgs.mediaContent.mediaUri))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC)).
                centerInside().into(image)
        }

    }
}