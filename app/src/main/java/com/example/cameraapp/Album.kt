package com.example.cameraapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "album")
data class Album(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val date: Long
)
