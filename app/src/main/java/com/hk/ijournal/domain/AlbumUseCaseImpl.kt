package com.hk.ijournal.domain

import com.hk.ijournal.repository.AlbumRepository
import com.hk.ijournal.repository.DiaryRepository
import com.hk.ijournal.repository.data.source.local.entities.DayAlbum
import com.hk.ijournal.repository.data.source.local.entities.DiaryPage
import com.hk.ijournal.repository.data.source.local.entities.ImageSource
import java.io.ByteArrayOutputStream
import java.io.File
import java.time.LocalDate
import javax.inject.Inject

class AlbumUseCaseImpl internal constructor(private val diaryRepository: DiaryRepository, private val albumRepository: AlbumRepository) : AlbumUseCase{

    override suspend fun getAlbum(pid: Long): List<DayAlbum>? {
        return albumRepository.getAlbum(pid)
    }

    override suspend fun insertAlbum(album: DayAlbum): Long {
        return albumRepository.insertAlbum(album)
    }

    override suspend fun getExternalImgUriList(pageId: Long?, imageSource: String): List<String> {
        return albumRepository.getExternalImgUriList(pageId, imageSource)
    }

    override suspend fun updateUriAndImageSource(oldUri: String, newUri: String, imageSource: String) {
        albumRepository.updateUriAndImageSource(oldUri, newUri, imageSource)
    }

    override suspend fun saveImgsToDbAsAlbum(diaryPage: DiaryPage, externalImgUriList: List<String>): MutableList<DayAlbum> {
        val dbImgUriList = mutableListOf<DayAlbum>()
        val pageId = diaryRepository.insertPage(diaryPage)
        externalImgUriList.forEach {
            dbImgUriList.add(DayAlbum(pageId!!, it, ImageSource.EXTERNAL.name))
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