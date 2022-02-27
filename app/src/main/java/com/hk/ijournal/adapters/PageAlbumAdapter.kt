package com.hk.ijournal.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.hk.ijournal.R
import com.hk.ijournal.repository.data.source.local.entities.DayAlbum
import com.hk.ijournal.views.home.diary.PageAlbumViewHolder

class PageAlbumAdapter(private val glide: RequestManager) : RecyclerView.Adapter<PageAlbumViewHolder>() {

    private val albumUriStringList: MutableList<DayAlbum> = mutableListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageAlbumViewHolder {
        val picView = LayoutInflater.from(parent.context).inflate(R.layout.album_single_image, parent, false)
        return PageAlbumViewHolder(picView)
    }

    override fun getItemCount(): Int = albumUriStringList.size


    override fun onBindViewHolder(holder: PageAlbumViewHolder, position: Int) {
        glide.load(Uri.parse(albumUriStringList[position].imageUriString)).placeholder(R.drawable.spinnerloop).apply(RequestOptions().override(200, 200)).centerCrop().thumbnail(0.4f)
                .into(holder.albumSingleImage)
    }

    fun addAlbum(albumUriList: List<DayAlbum>) {
        albumUriStringList.clear()
        albumUriStringList.addAll(albumUriList)
        notifyDataSetChanged()
    }

    fun clearAlbum() {
        albumUriStringList.clear()
        notifyDataSetChanged()
    }

}