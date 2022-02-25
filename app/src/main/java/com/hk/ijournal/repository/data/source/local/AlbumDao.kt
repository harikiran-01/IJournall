package com.hk.ijournal.repository.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hk.ijournal.repository.data.source.local.entities.DayAlbum

@Dao
interface AlbumDao : RoomDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAlbum(album: DayAlbum): Long

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAllAlbum(albumList: List<DayAlbum>)

    @Query("select * from albumtable where pid=:pid order by aid desc")
    suspend fun getAlbum(pid: Long): List<DayAlbum>?

    @Query("select imageUriString from albumtable where pid=:pageId and imageSource=:imageSource")
    suspend fun getExternalImgUriList(pageId: Long?, imageSource: String): List<String>

    @Query("update albumtable set imageUriString =:newUri , imageSource=:imageSource where imageUriString=:oldUri")
    suspend fun updateUriAndImageSource(oldUri: String, newUri: String, imageSource: String)
}