package com.hk.ijournal.domain

import com.hk.ijournal.repository.AlbumRepository
import com.hk.ijournal.repository.data.source.local.entities.DayAlbum
import com.hk.ijournal.repository.data.source.local.entities.DiaryPage
import kotlinx.coroutines.flow.Flow
import java.io.ByteArrayOutputStream
import java.io.File
import java.time.LocalDate

interface AlbumUseCase {
    suspend fun getAlbum(pid: Long): List<DayAlbum>?
    suspend fun insertAlbum(album: DayAlbum): Long
    suspend fun getExternalImgUriList(pageId: Long?, imageSource: String): List<String>
    suspend fun saveImgsToDbAsAlbum(diaryPage: DiaryPage, externalImgUriList: List<String>): MutableList<DayAlbum>
    suspend fun updateUriAndImageSource(oldUri: String, newUri: String, imageSource: String)
    suspend fun saveImageInApp(userId: Long, selectedDate: LocalDate, internalDirectory: File, byteArrayOutputStream: ByteArrayOutputStream): String
    suspend fun saveImageToInternalFile(userId: Long, selectedDate: LocalDate, internalDirectory: File, byteArrayOutputStream: ByteArrayOutputStream): String
    suspend fun updateImgUriInDb(oldImgUri: String, newImgUri: String)
}