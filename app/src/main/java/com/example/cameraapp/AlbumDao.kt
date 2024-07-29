package com.example.cameraapp

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete

@Dao
interface AlbumDao {
    @Query("SELECT * FROM album")
    fun getAllAlbums(): LiveData<List<Album>>

    @Insert
    suspend fun insert(album: Album)

    @Update
    suspend fun update(album: Album)

    @Delete
    suspend fun delete(album: Album)
}
