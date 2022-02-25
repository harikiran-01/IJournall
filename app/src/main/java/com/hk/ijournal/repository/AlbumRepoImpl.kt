package com.hk.ijournal.repository

import androidx.core.net.toUri
import com.hk.ijournal.repository.data.source.local.AlbumLocalDataSource
import com.hk.ijournal.repository.data.source.local.entities.DayAlbum
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDate
import java.util.*

class AlbumRepoImpl(private val albumLocalDataSource: AlbumLocalDataSource, private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO): AlbumRepository {
    override suspend fun getAlbum(pid: Long): List<DayAlbum>? {
        return albumLocalDataSource.getAlbum(pid)
    }

    override suspend fun insertAlbum(album: DayAlbum): Long {
        return albumLocalDataSource.insertAlbum(album)
    }

    override suspend fun insertAllAlbum(albumList: List<DayAlbum>) {
        albumLocalDataSource.insertAllAlbum(albumList)
    }

    override suspend fun getExternalImgUriList(pageId: Long?, imageSource: String): List<String> {
        return albumLocalDataSource.getExternalImgUriList(pageId, imageSource)
    }

    override suspend fun updateUriAndImageSource(oldUri: String, newUri: String, imageSource: String) {
        albumLocalDataSource.updateUriAndImageSource(oldUri, newUri, imageSource)
    }

    override suspend fun saveImageInApp(userId: Long, selectedDate: LocalDate, internalDirectory: File, byteArrayOutputStream: ByteArrayOutputStream) = withContext(ioDispatcher) {
        val savedImagePath = saveImageToInternalFile(userId, selectedDate, internalDirectory, byteArrayOutputStream)
        savedImagePath
    }


    override suspend fun saveImageToInternalFile(userId: Long, selectedDate: LocalDate, internalDirectory: File, byteArrayOutputStream: ByteArrayOutputStream): String {
        val imageName = getUniqueFileName()
        val filePathDirectory = internalDirectory.absolutePath + File.separator + userId + selectedDate + File.separator
        val f = File(filePathDirectory)
        f.mkdirs()
        val file = File(f, imageName)
        val outputStream = FileOutputStream(file)
        byteArrayOutputStream.writeTo(outputStream)
        outputStream.close()
        byteArrayOutputStream.flush()
        return file.toUri().toString()
    }

    private fun getUniqueFileName() = UUID.randomUUID().toString() + ".jpg"
}