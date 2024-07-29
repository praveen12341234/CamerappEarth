package com.example.cameraapp

import androidx.lifecycle.LiveData

class PhotoRepository(private val photoDao: PhotoDao) {

    val allPhotos: LiveData<List<Photo>> = photoDao.getAllPhotos()

    fun getPhotosForAlbum(albumId: Int): LiveData<List<Photo>> {
        return photoDao.getPhotosForAlbum(albumId)
    }

    suspend fun insert(photo: Photo) {
        photoDao.insert(photo)
    }
}
