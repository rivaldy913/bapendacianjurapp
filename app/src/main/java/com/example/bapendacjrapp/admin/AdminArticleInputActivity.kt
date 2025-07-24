package com.example.bapendacjrapp.admin

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bapendacjrapp.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Date

class AdminArticleInputActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    // FirebaseStorage dan selectedImageUri dihapus

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_article_input)

        db = Firebase.firestore
        // storage dihapus

        val etArtikelTitle = findViewById<EditText>(R.id.etArtikelTitle)
        val etArtikelDate = findViewById<EditText>(R.id.etArtikelDate)
        val etArtikelCategory = findViewById<EditText>(R.id.etArtikelCategory)
        val etArtikelDescription = findViewById<EditText>(R.id.etArtikelDescription)
        val etArtikelImageUrl = findViewById<EditText>(R.id.etArtikelImageUrl) // Dikembalikan
        val btnAddArtikel = findViewById<Button>(R.id.btnAddArtikel)
        val ivArticleInputBack = findViewById<ImageView>(R.id.ivArticleInputBack)

        // Listener untuk tombol pilih gambar dihapus

        btnAddArtikel.setOnClickListener {
            val title = etArtikelTitle.text.toString().trim()
            val date = etArtikelDate.text.toString().trim()
            val category = etArtikelCategory.text.toString().trim()
            val description = etArtikelDescription.text.toString().trim()
            val imageUrlString = etArtikelImageUrl.text.toString().trim() // Ambil dari EditText

            if (title.isEmpty() || date.isEmpty() || category.isEmpty() || description.isEmpty() || imageUrlString.isEmpty()) {
                Toast.makeText(this, "Mohon lengkapi semua bidang.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val artikelData = hashMapOf(
                "title" to title,
                "date" to date,
                "category" to category,
                "description" to description,
                "imageUrl" to imageUrlString, // Simpan sebagai String nama drawable
                "timestamp" to Date()
            )

            db.collection("articles")
                .add(artikelData)
                .addOnSuccessListener { documentReference ->
                    Toast.makeText(this, "Artikel berhasil ditambahkan dengan ID: ${documentReference.id}", Toast.LENGTH_LONG).show()
                    etArtikelTitle.text.clear()
                    etArtikelDate.text.clear()
                    etArtikelCategory.text.clear()
                    etArtikelDescription.text.clear()
                    etArtikelImageUrl.text.clear() // Bersihkan input
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Gagal menambahkan artikel: ${e.message}", Toast.LENGTH_LONG).show()
                }
        }

        ivArticleInputBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
    // openImageChooser dan uploadImageToFirebase dihapus
}