package com.hk.ijournal.domain

import androidx.lifecycle.LiveData
import com.hk.ijournal.repository.AlbumRepository
import com.hk.ijournal.repository.DiaryRepository
import com.hk.ijournal.repository.data.source.local.entities.DayAlbum
import com.hk.ijournal.repository.data.source.local.entities.DiaryPage
import com.hk.ijournal.repository.data.source.local.entities.ImageSource
import java.io.ByteArrayOutputStream
import java.io.File
import java.time.LocalDate
import javax.inject.Inject

class AlbumUseCaseImpl internal constructor(private val albumRepository: AlbumRepository) : AlbumUseCase{

    override suspend fun getAlbum(pid: Long): List<DayAlbum>? = albumRepository.getAlbum(pid)

    override suspend fun insertAlbum(album: DayAlbum): Long {
        return albumRepository.insertAlbum(album)
    }

    override suspend fun getExternalImgUriList(pageId: Long?, imageSource: String): List<String> {
        return albumRepository.getExternalImgUriList(pageId, imageSource)
    }

    override suspend fun updateUriAndImageSource(oldUri: String, newUri: String, imageSource: String) {
        albumRepository.updateUriAndImageSource(oldUri, newUri, imageSource)
    }

    override suspend fun saveImgsToDbAsAlbum(pageId: Long, externalImgUriList: List<String>): List<DayAlbum> {
        val dbImgUriList = mutableListOf<DayAlbum>()
        // if page is not in db, then insert in db with empty content
        externalImgUriList.forEach {
            dbImgUriList.add(DayAlbum(pageId, it, ImageSource.EXTERNAL.name))
        }
        albumRepository.insertAllAlbum(dbImgUriList)
        return dbImgUriList
    }

    override suspend fun saveImageInApp(
        userId: Long,
        selectedDate: LocalDate,
        internalDirectory: File,
        byteArrayOutputStream: ByteArrayOutputStream
    ): String {
        return albumRepository.saveImageInApp(userId, selectedDate, internalDirectory, byteArrayOutputStream)
    }

    override suspend fun saveImageToInternalFile(
        userId: Long,
        selectedDate: LocalDate,
        internalDirectory: File,
        byteArrayOutputStream: ByteArrayOutputStream
    ): String {
        return albumRepository.saveImageToInternalFile(userId, selectedDate, internalDirectory, byteArrayOutputStream)
    }

    override suspend fun updateImgUriInDb(oldImgUri: String, newImgUri: String) {
        updateUriAndImageSource(oldImgUri, newImgUri, ImageSource.INTERNAL.name)
    }
}