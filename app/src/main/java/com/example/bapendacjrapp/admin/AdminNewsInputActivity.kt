package com.example.bapendacjrapp.admin

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView // Import TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bapendacjrapp.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Date

class AdminNewsInputActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var etBeritaTitle: EditText
    private lateinit var etBeritaDate: EditText
    private lateinit var etBeritaDescription: EditText
    private lateinit var etBeritaImageUrl: EditText
    private lateinit var btnAddBerita: Button
    private lateinit var ivNewsInputBack: ImageView
    private lateinit var tvToolbarTitle: TextView // Added for dynamic toolbar title

    private var newsIdToEdit: String? = null // To store the ID if we are editing

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_news_input)

        db = Firebase.firestore

        etBeritaTitle = findViewById(R.id.etBeritaTitle)
        etBeritaDate = findViewById(R.id.etBeritaDate)
        etBeritaDescription = findViewById(R.id.etBeritaDescription)
        etBeritaImageUrl = findViewById(R.id.etBeritaImageUrl)
        btnAddBerita = findViewById(R.id.btnAddBerita)
        ivNewsInputBack = findViewById(R.id.ivNewsInputBack)
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle) // Initialize the toolbar title TextView

        // Check if we are in edit mode
        newsIdToEdit = intent.getStringExtra("NEWS_ID")
        if (newsIdToEdit != null) {
            // Pre-fill fields for editing
            tvToolbarTitle.text = "Edit Berita" // Set toolbar title for edit mode
            btnAddBerita.text = "Simpan Perubahan" // Change button text
            etBeritaTitle.setText(intent.getStringExtra("NEWS_TITLE"))
            etBeritaDate.setText(intent.getStringExtra("NEWS_DATE"))
            etBeritaDescription.setText(intent.getStringExtra("NEWS_DESCRIPTION"))
            etBeritaImageUrl.setText(intent.getStringExtra("NEWS_IMAGE_URL"))
        } else {
            tvToolbarTitle.text = "Input Berita Baru" // Set toolbar title for add mode
            btnAddBerita.text = "Tambah Berita" // Set button text for add mode
        }

        btnAddBerita.setOnClickListener {
            val title = etBeritaTitle.text.toString().trim()
            val date = etBeritaDate.text.toString().trim()
            val description = etBeritaDescription.text.toString().trim()
            val imageUrlString = etBeritaImageUrl.text.toString().trim()
            val category = "Berita"

            if (title.isEmpty() || date.isEmpty() || description.isEmpty() || imageUrlString.isEmpty()) {
                Toast.makeText(this, "Mohon lengkapi semua bidang Berita.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Verify if the drawable resource exists
            val imageResId = resources.getIdentifier(imageUrlString, "drawable", packageName)
            if (imageResId == 0) {
                Toast.makeText(this, "Nama drawable gambar tidak ditemukan. Pastikan sudah ada di folder drawable.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val beritaData = hashMapOf(
                "title" to title,
                "date" to date,
                "category" to category,
                "description" to description,
                "imageUrl" to imageUrlString,
                "timestamp" to Date()
            )

            if (newsIdToEdit != null) {
                // Update existing news
                db.collection("news").document(newsIdToEdit!!)
                    .set(beritaData) // Use set to overwrite
                    .addOnSuccessListener {
                        Toast.makeText(this, "Berita berhasil diperbarui!", Toast.LENGTH_LONG).show()
                        finish() // Go back to AdminManageNewsActivity
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Gagal memperbarui berita: ${e.message}", Toast.LENGTH_LONG).show()
                    }
            } else {
                // Add new news
                db.collection("news")
                    .add(beritaData)
                    .addOnSuccessListener { documentReference ->
                        Toast.makeText(this, "Berita berhasil ditambahkan dengan ID: ${documentReference.id}", Toast.LENGTH_LONG).show()
                        etBeritaTitle.text.clear()
                        etBeritaDate.text.clear()
                        etBeritaDescription.text.clear()
                        etBeritaImageUrl.text.clear()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Gagal menambahkan berita: ${e.message}", Toast.LENGTH_LONG).show()
                    }
            }
        }

        ivNewsInputBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}