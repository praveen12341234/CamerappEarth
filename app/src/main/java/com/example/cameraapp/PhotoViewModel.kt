package com.example.cameraapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class PhotoViewModel(application: Application) : AndroidViewModel(application) {

    private val photoRepository: PhotoRepository
    val allPhotos: LiveData<List<Photo>>

    init {
        val photoDao = AppDatabase.getDatabase(application).photoDao()
        photoRepository = PhotoRepository(photoDao)
        allPhotos = photoRepository.allPhotos
    }

    fun getPhotosForAlbum(albumId: Int): LiveData<List<Photo>> {
        return photoRepository.getPhotosForAlbum(albumId)
    }

    fun insert(photo: Photo) = viewModelScope.launch {
        photoRepository.insert(photo)
    }
}
