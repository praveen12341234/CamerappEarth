package com.example.cameraapp

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

class MainActivity : AppCompatActivity() {
    private lateinit var captureButton: Button
    private lateinit var goToAlbumButton: Button
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_PERMISSION = 100
    private lateinit var photoURI: Uri
    private lateinit var currentPhotoPath: String
    private lateinit var albumViewModel: AlbumViewModel
    private lateinit var photoViewModel: PhotoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        captureButton = findViewById(R.id.capture_button)
        goToAlbumButton = findViewById(R.id.go_to_album_button)
        val albumRecyclerView = findViewById<RecyclerView>(R.id.album_recycler_view)
        val adapter = AlbumAdapter { album -> openAlbum(album.id) }
        albumRecyclerView.adapter = adapter
        albumRecyclerView.layoutManager = LinearLayoutManager(this)

        albumViewModel = ViewModelProvider(this).get(AlbumViewModel::class.java)
        albumViewModel.allAlbums.observe(this, { albums ->
            albums?.let { adapter.submitList(it) }
        })

        photoViewModel = ViewModelProvider(this).get(PhotoViewModel::class.java)

        captureButton.setOnClickListener {
            if (allPermissionsGranted()) {
                dispatchTakePictureIntent()
            } else {
                requestPermissions()
            }
        }

        goToAlbumButton.setOnClickListener {
            openGallery()
        }
    }

    private fun allPermissionsGranted(): Boolean {
        val cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        val storagePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        return cameraPermission == PackageManager.PERMISSION_GRANTED && storagePermission == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        val permissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSION)
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }
                photoFile?.also {
                    photoURI = FileProvider.getUriForFile(this, "com.example.cameraapp.fileprovider", it)
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            savePhotoToDatabase()
            Intent(this, PreviewActivity::class.java).also {
                it.putExtra("photoURI", photoURI.toString())
                it.putExtra("currentPhotoPath", currentPhotoPath)
                startActivity(it)
            }
        }
    }

    private fun savePhotoToDatabase() {
        val albumId = 1 // Replace with actual logic to get album ID
        val photo = Photo(
            id = 0,
            albumId = albumId,
            uri = photoURI.toString(),
            date = System.currentTimeMillis()
        )
        photoViewModel.insert(photo)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent()
            } else {
                if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) ||
                    shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    showPermissionRationale()
                } else {
                    showAppSettingsDialog()
                }
            }
        }
    }

    private fun showPermissionRationale() {
        AlertDialog.Builder(this)
            .setTitle("Permissions Required")
            .setMessage("This app needs camera and storage permissions to function properly.")
            .setPositiveButton("OK") { _, _ ->
                requestPermissions()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
                Toast.makeText(this, "Permissions are required to use the camera.", Toast.LENGTH_SHORT).show()
            }
            .create()
            .show()
    }

    private fun showAppSettingsDialog() {
        AlertDialog.Builder(this)
            .setTitle("Permissions Required")
            .setMessage("You have permanently denied some permissions. Please go to settings to enable them.")
            .setPositiveButton("Settings") { _, _ ->
                val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
                Toast.makeText(this, "Permissions are required to use the camera.", Toast.LENGTH_SHORT).show()
            }
            .create()
            .show()
    }

    private fun openAlbum(albumId: Int) {
        val intent = Intent(this, AlbumActivity::class.java).apply {
            putExtra("albumId", albumId)
        }
        startActivity(intent)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_VIEW, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivity(intent)
    }
}
