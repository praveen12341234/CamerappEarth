package com.example.cameraapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photos")
data class Photo(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val albumId: Int,
    val uri: String,
    val date: Long
)
