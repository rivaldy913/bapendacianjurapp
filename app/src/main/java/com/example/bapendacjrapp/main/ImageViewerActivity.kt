package com.example.bapendacjrapp.main

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.bapendacjrapp.R

class ImageViewerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_viewer)

        val imageView = findViewById<ImageView>(R.id.fullscreenImageView)
        val ivBack = findViewById<ImageView>(R.id.ivImageViewerBack)

        // Ambil ID resource gambar dari Intent
        val imageResId = intent.getIntExtra("image_res_id", 0)

        if (imageResId != 0) {
            imageView.setImageResource(imageResId)
        } else {
            // Opsional: tampilkan placeholder atau pesan error jika gambar tidak ditemukan
            imageView.setImageResource(R.drawable.placeholder_file_icon)
        }

        // Listener tombol kembali
        ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}