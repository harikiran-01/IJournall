package com.hk.ijournal.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.hk.ijournal.R
import com.hk.ijournal.views.diary.PageAlbumViewHolder

class PageAlbumAdapter(private val glide: RequestManager, private val albumImageUri: MutableList<Uri>) : RecyclerView.Adapter<PageAlbumViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageAlbumViewHolder {
        val picView = LayoutInflater.from(parent.context).inflate(R.layout.album_single_image, parent, false)
        return PageAlbumViewHolder(picView)
    }

    override fun getItemCount(): Int = albumImageUri.size


    override fun onBindViewHolder(holder: PageAlbumViewHolder, position: Int) {
        glide.load(albumImageUri[position]).apply(RequestOptions().override(200, 200)).centerCrop().thumbnail(0.4f).diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.albumSingleImage)
    }

}