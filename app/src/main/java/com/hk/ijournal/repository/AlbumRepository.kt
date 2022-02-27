package com.hk.ijournal.repository

import androidx.lifecycle.LiveData
import com.hk.ijournal.repository.data.source.local.entities.DayAlbum
import kotlinx.coroutines.flow.Flow
import java.io.ByteArrayOutputStream
import java.io.File
import java.time.LocalDate

interface AlbumRepository {
    suspend fun getAlbum(pid: Long): List<DayAlbum>?
    suspend fun insertAlbum(album: DayAlbum): Long
    suspend fun insertAllAlbum(albumList: List<DayAlbum>)
    suspend fun getExternalImgUriList(pageId: Long?, imageSource: String): List<String>
    suspend fun updateUriAndImageSource(oldUri: String, newUri: String, imageSource: String)
    suspend fun saveImageInApp(userId: Long, selectedDate: LocalDate, internalDirectory: File, byteArrayOutputStream: ByteArrayOutputStream): String
    suspend fun saveImageToInternalFile(userId: Long, selectedDate: LocalDate, internalDirectory: File, byteArrayOutputStream: ByteArrayOutputStream): String
}