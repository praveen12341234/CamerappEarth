package com.example.cameraapp

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PhotoDao {
    @Query("SELECT * FROM photos WHERE albumId = :albumId")
    fun getPhotosForAlbum(albumId: Int): LiveData<List<Photo>>

    @Query("SELECT * FROM photos")
    fun getAllPhotos(): LiveData<List<Photo>>

    @Insert
    suspend fun insert(photo: Photo)
}
