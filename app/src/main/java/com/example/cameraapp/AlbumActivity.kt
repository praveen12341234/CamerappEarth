package com.example.cameraapp

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AlbumActivity : AppCompatActivity() {

    private lateinit var photoViewModel: PhotoViewModel
    private var albumId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album)

        albumId = intent.getIntExtra("albumId", 0)
        Log.d("AlbumActivity", "Album ID: $albumId")

        val textView = findViewById<TextView>(R.id.debug_text_view)
        textView.text = "Loading photos for album ID: $albumId"

        val photoRecyclerView = findViewById<RecyclerView>(R.id.photo_recycler_view)
        val adapter = PhotoAdapter(emptyList()) { uri ->
            // Handle photo click here
        }
        photoRecyclerView.adapter = adapter
        photoRecyclerView.layoutManager = LinearLayoutManager(this)

        photoViewModel = ViewModelProvider(this).get(PhotoViewModel::class.java)
        photoViewModel.getPhotosForAlbum(albumId).observe(this, Observer { photos ->
            Log.d("AlbumActivity", "Photos: $photos")
            if (photos != null) {
                textView.text = "Found ${photos.size} photos for album ID: $albumId"
                adapter.updatePhotos(photos)
            } else {
                textView.text = "No photos found for album ID: $albumId"
            }
        })
    }
}
