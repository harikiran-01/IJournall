package com.hk.ijournal.repository.data.source.local.datasource

import com.hk.ijournal.repository.data.source.AlbumDataSource
import com.hk.ijournal.repository.data.source.local.dao.AlbumDao
import com.hk.ijournal.repository.data.source.local.entities.DayAlbum
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AlbumLocalDataSource
internal constructor(private val albumDao: AlbumDao,
                     private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO): AlbumDataSource {

    override suspend fun insertAlbum(album: DayAlbum): Long = withContext(ioDispatcher) {
        return@withContext albumDao.insertAlbum(album)
    }

    override suspend fun insertAllAlbum(albumList: List<DayAlbum>) = withContext(ioDispatcher) {
        albumDao.insertAllAlbum(albumList)
    }

    override suspend fun getAlbum(pid: Long): List<DayAlbum>? = albumDao.getAlbum(pid)

    override suspend fun getExternalImgUriList(pageId: Long?, imageSource: String): List<String> = withContext(ioDispatcher) {
        return@withContext albumDao.getExternalImgUriList(pageId, imageSource)
    }

    override suspend fun updateUriAndImageSource(oldUri: String, newUri: String, imageSource: String) = withContext(ioDispatcher) {
        albumDao.updateUriAndImageSource(oldUri, newUri, imageSource)
    }
}