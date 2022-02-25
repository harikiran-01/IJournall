package com.hk.ijournal.repository.data.source

import com.hk.ijournal.repository.data.source.local.entities.DayAlbum

interface AlbumDataSource {
    suspend fun insertAlbum(album: DayAlbum): Long

    suspend fun insertAllAlbum(albumList: List<DayAlbum>)

    suspend fun getAlbum(pid: Long): List<DayAlbum>?

    suspend fun getExternalImgUriList(pageId: Long?, imageSource: String): List<String>

    suspend fun updateUriAndImageSource(oldUri: String, newUri: String, imageSource: String)
}