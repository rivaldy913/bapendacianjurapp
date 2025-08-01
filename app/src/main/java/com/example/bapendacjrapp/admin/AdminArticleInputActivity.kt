package com.example.bapendacjrapp.admin

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView // Impor TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bapendacjrapp.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Date
import android.widget.LinearLayout // Impor LinearLayout

class AdminArticleInputActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var etArtikelTitle: EditText
    private lateinit var etArtikelDate: EditText
    private lateinit var etArtikelCategory: EditText
    private lateinit var etArtikelDescription: EditText
    private lateinit var etArtikelImageUrl: EditText
    private lateinit var btnAddArtikel: Button
    private lateinit var ivArticleInputBack: ImageView
    private lateinit var tvToolbarTitle: TextView // Ditambahkan untuk judul toolbar dinamis

    private var articleIdToEdit: String? = null // Untuk menyimpan ID jika kita mengedit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_article_input)

        db = Firebase.firestore

        etArtikelTitle = findViewById(R.id.etArtikelTitle)
        etArtikelDate = findViewById(R.id.etArtikelDate)
        etArtikelCategory = findViewById(R.id.etArtikelCategory)
        etArtikelDescription = findViewById(R.id.etArtikelDescription)
        etArtikelImageUrl = findViewById(R.id.etArtikelImageUrl)
        btnAddArtikel = findViewById(R.id.btnAddArtikel)
        ivArticleInputBack = findViewById(R.id.ivArticleInputBack)

        // Perbaikan di baris ini: Tambahkan cast eksplisit
        tvToolbarTitle = findViewById<LinearLayout>(R.id.articleInputToolbar).findViewById<TextView>(R.id.tvToolbarTitle)

        // Periksa apakah kita dalam mode edit
        articleIdToEdit = intent.getStringExtra("ARTICLE_ID")
        if (articleIdToEdit != null) {
            // Isi bidang untuk pengeditan
            tvToolbarTitle.text = "Edit Artikel" // Atur judul toolbar untuk mode edit
            btnAddArtikel.text = "Simpan Perubahan" // Ubah teks tombol
            etArtikelTitle.setText(intent.getStringExtra("ARTICLE_TITLE"))
            etArtikelDate.setText(intent.getStringExtra("ARTICLE_DATE"))
            etArtikelCategory.setText(intent.getStringExtra("ARTICLE_CATEGORY"))
            etArtikelDescription.setText(intent.getStringExtra("ARTICLE_DESCRIPTION"))
            etArtikelImageUrl.setText(intent.getStringExtra("ARTICLE_IMAGE_URL"))
        } else {
            tvToolbarTitle.text = "Input Artikel Baru" // Atur judul toolbar untuk mode tambah
            btnAddArtikel.text = "Tambah Artikel" // Atur teks tombol untuk mode tambah
        }


        btnAddArtikel.setOnClickListener {
            val title = etArtikelTitle.text.toString().trim()
            val date = etArtikelDate.text.toString().trim()
            val category = etArtikelCategory.text.toString().trim()
            val description = etArtikelDescription.text.toString().trim()
            val imageUrlString = etArtikelImageUrl.text.toString().trim()

            if (title.isEmpty() || date.isEmpty() || category.isEmpty() || description.isEmpty() || imageUrlString.isEmpty()) {
                Toast.makeText(this, "Mohon lengkapi semua bidang.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Verifikasi apakah sumber drawable ada
            val imageResId = resources.getIdentifier(imageUrlString, "drawable", packageName)
            if (imageResId == 0) {
                Toast.makeText(this, "Nama drawable gambar tidak ditemukan. Pastikan sudah ada di folder drawable.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val artikelData = hashMapOf(
                "title" to title,
                "date" to date,
                "category" to category,
                "description" to description,
                "imageUrl" to imageUrlString,
                "timestamp" to Date()
            )

            if (articleIdToEdit != null) {
                // Perbarui artikel yang sudah ada
                db.collection("articles").document(articleIdToEdit!!)
                    .set(artikelData) // Gunakan set untuk menimpa
                    .addOnSuccessListener {
                        Toast.makeText(this, "Artikel berhasil diperbarui!", Toast.LENGTH_LONG).show()
                        finish() // Kembali ke AdminManageArticlesActivity
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Gagal memperbarui artikel: ${e.message}", Toast.LENGTH_LONG).show()
                    }
            } else {
                // Tambahkan artikel baru
                db.collection("articles")
                    .add(artikelData)
                    .addOnSuccessListener { documentReference ->
                        Toast.makeText(this, "Artikel berhasil ditambahkan dengan ID: ${documentReference.id}", Toast.LENGTH_LONG).show()
                        etArtikelTitle.text.clear()
                        etArtikelDate.text.clear()
                        etArtikelCategory.text.clear()
                        etArtikelDescription.text.clear()
                        etArtikelImageUrl.text.clear()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Gagal menambahkan artikel: ${e.message}", Toast.LENGTH_LONG).show()
                    }
            }
        }

        ivArticleInputBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}