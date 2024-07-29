package com.example.cameraapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class AlbumViewModel(application: Application) : AndroidViewModel(application) {

    private val albumDao = AppDatabase.getDatabase(application).albumDao()
    val allAlbums: LiveData<List<Album>> = albumDao.getAllAlbums()

    fun insert(album: Album) = viewModelScope.launch {
        albumDao.insert(album)
    }

    fun update(album: Album) = viewModelScope.launch {
        albumDao.update(album)
    }

    fun delete(album: Album) = viewModelScope.launch {
        albumDao.delete(album)
    }
}
