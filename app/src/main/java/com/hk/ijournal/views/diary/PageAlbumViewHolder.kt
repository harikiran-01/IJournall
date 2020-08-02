package com.hk.ijournal.views.diary

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.hk.ijournal.R

class PageAlbumViewHolder(picView: View) : RecyclerView.ViewHolder(picView) {
    val albumSingleImage: ImageView = picView.findViewById(R.id.album_single_image)
}