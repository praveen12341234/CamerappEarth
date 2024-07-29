package com.example.cameraapp

import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.File

class PreviewActivity : AppCompatActivity() {

    private lateinit var photoPreview: ImageView
    private lateinit var keepButton: Button
    private lateinit var deleteButton: Button
    private lateinit var photoURI: Uri
    private lateinit var currentPhotoPath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview)

        // Ensure IDs match the ones in the XML layout
        photoPreview = findViewById(R.id.photo_preview)
        keepButton = findViewById(R.id.keep_button)
        deleteButton = findViewById(R.id.delete_button)

        photoURI = Uri.parse(intent.getStringExtra("photoURI"))
        currentPhotoPath = intent.getStringExtra("currentPhotoPath")!!

        photoPreview.setImageURI(photoURI)

        keepButton.setOnClickListener {
            Toast.makeText(this, "Successfully saved", Toast.LENGTH_SHORT).show()
            finish()
        }

        deleteButton.setOnClickListener {
            File(currentPhotoPath).delete()
            Toast.makeText(this, "Photo deleted", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
