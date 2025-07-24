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

class AdminNewsInputActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    // FirebaseStorage dan selectedImageUri dihapus

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_news_input)

        db = Firebase.firestore
        // storage dihapus

        val etBeritaTitle = findViewById<EditText>(R.id.etBeritaTitle)
        val etBeritaDate = findViewById<EditText>(R.id.etBeritaDate)
        val etBeritaDescription = findViewById<EditText>(R.id.etBeritaDescription)
        val etBeritaImageUrl = findViewById<EditText>(R.id.etBeritaImageUrl) // Dikembalikan
        val btnAddBerita = findViewById<Button>(R.id.btnAddBerita)
        val ivNewsInputBack = findViewById<ImageView>(R.id.ivNewsInputBack)

        // Listener untuk tombol pilih gambar dihapus

        btnAddBerita.setOnClickListener {
            val title = etBeritaTitle.text.toString().trim()
            val date = etBeritaDate.text.toString().trim()
            val description = etBeritaDescription.text.toString().trim()
            val imageUrlString = etBeritaImageUrl.text.toString().trim() // Ambil dari EditText
            val category = "Berita"

            if (title.isEmpty() || date.isEmpty() || description.isEmpty() || imageUrlString.isEmpty()) {
                Toast.makeText(this, "Mohon lengkapi semua bidang Berita.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val beritaData = hashMapOf(
                "title" to title,
                "date" to date,
                "category" to category,
                "description" to description,
                "imageUrl" to imageUrlString, // Simpan sebagai String nama drawable
                "timestamp" to Date()
            )

            db.collection("news")
                .add(beritaData)
                .addOnSuccessListener { documentReference ->
                    Toast.makeText(this, "Berita berhasil ditambahkan dengan ID: ${documentReference.id}", Toast.LENGTH_LONG).show()
                    etBeritaTitle.text.clear()
                    etBeritaDate.text.clear()
                    etBeritaDescription.text.clear()
                    etBeritaImageUrl.text.clear() // Bersihkan input
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Gagal menambahkan berita: ${e.message}", Toast.LENGTH_LONG).show()
                }
        }

        ivNewsInputBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
    // openImageChooser dan uploadImageToFirebase dihapus
}