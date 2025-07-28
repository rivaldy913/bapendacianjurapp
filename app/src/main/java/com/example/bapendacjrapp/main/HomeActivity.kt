package com.example.bapendacjrapp.main

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bapendacjrapp.main.*
import com.example.bapendacjrapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Date
import com.google.firebase.firestore.Query

class HomeActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    private lateinit var rvBerita: RecyclerView
    private lateinit var rvPengumuman: RecyclerView
    private lateinit var rvArtikel: RecyclerView
    private lateinit var rvPimpinan: RecyclerView
    private lateinit var rvLayanan: RecyclerView
    private lateinit var rvReviews: RecyclerView // Deklarasi untuk RecyclerView ulasan

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        db = Firebase.firestore
        auth = Firebase.auth

        val etUserReview = findViewById<EditText>(R.id.etUserReview)
        val btnSubmitReview = findViewById<Button>(R.id.btnSubmitReview)

        rvBerita = findViewById(R.id.rvBerita)
        rvPengumuman = findViewById(R.id.rvPengumuman)
        rvArtikel = findViewById(R.id.rvArtikel)
        rvPimpinan = findViewById(R.id.rvPimpinan)
        rvLayanan = findViewById(R.id.rvLayanan)
        rvReviews = findViewById(R.id.rvReviews) // Inisialisasi RecyclerView ulasan

        rvBerita.layoutManager = LinearLayoutManager(this)
        rvPengumuman.layoutManager = LinearLayoutManager(this)
        rvArtikel.layoutManager = LinearLayoutManager(this)
        rvPimpinan.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvLayanan.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvReviews.layoutManager = LinearLayoutManager(this) // Atur layout manager untuk ulasan

        btnSubmitReview.setOnClickListener {
            val reviewText = etUserReview.text.toString().trim()

            if (reviewText.isNotEmpty()) {
                val userId = auth.currentUser?.uid
                if (userId != null) {
                    val review = hashMapOf(
                        "userId" to userId,
                        "reviewText" to reviewText,
                        "timestamp" to Date().time
                    )
                    db.collection("reviews")
                        .add(review)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Ulasan berhasil dikirim!", Toast.LENGTH_SHORT).show()
                            etUserReview.text.clear()
                            loadReviews() // Muat ulang ulasan setelah ulasan baru dikirim
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Gagal: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                } else {
                    Toast.makeText(this, "Login dulu untuk kirim ulasan.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Ulasan tidak boleh kosong.", Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<Button>(R.id.btnLihatSelengkapnya).setOnClickListener {
            startActivity(Intent(this, DetailActivity::class.java).apply {
                putExtra("title", "Profil Bapenda")
            })
        }

        loadBerita()
        loadPengumuman()
        loadArtikel()
        loadLayanan()
        loadReviews() // Panggil fungsi ini untuk memuat dan menampilkan ulasan

        val pimpinanList = listOf(
            PimpinanItem("p1", R.drawable.placeholder_pimpinan_1, "Dr. H. M. Ridwan", "Kepala Bapenda")
        )
        rvPimpinan.adapter = PimpinanAdapter(pimpinanList) {}

        // Bagian layanan yang hardcode sudah dihapus karena diganti dengan loadLayanan()
    }

    private fun loadBerita() {
        val beritaList = mutableListOf<BeritaPengumumanItem>()
        db.collection("news")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val id = document.id
                    val title = document.getString("title") ?: ""
                    val date = document.getString("date") ?: ""
                    val category = document.getString("category") ?: "Berita"
                    val description = document.getString("description") ?: ""
                    val imageUrl = document.getString("imageUrl") ?: "" // Nama drawable

                    beritaList.add(BeritaPengumumanItem(id, imageUrl, title, date, category, description))
                }
                rvBerita.adapter = BeritaPengumumanAdapter(beritaList) { item ->
                    startActivity(Intent(this, DetailActivity::class.java).apply {
                        putExtra("title", item.title)
                        putExtra("content", item.description)
                    })
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error getting news: ${exception.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun loadPengumuman() {
        val pengumumanList = mutableListOf<BeritaPengumumanItem>()
        db.collection("announcements")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                if (result.isEmpty) {
                    pengumumanList.add(BeritaPengumumanItem("p1", "placeholder_announcement_image", "Diskon 50% BPHTB", "16 Jul 2025", "Pengumuman", "Diskon dalam rangka Hari Jadi Cianjur..."))
                } else {
                    for (document in result) {
                        val id = document.id
                        val title = document.getString("title") ?: ""
                        val date = document.getString("date") ?: ""
                        val category = document.getString("category") ?: "Pengumuman"
                        val description = document.getString("description") ?: ""
                        val imageUrl = document.getString("imageUrl") ?: ""
                        pengumumanList.add(BeritaPengumumanItem(id, imageUrl, title, date, category, description))
                    }
                }
                rvPengumuman.adapter = BeritaPengumumanAdapter(pengumumanList) { item ->
                    startActivity(Intent(this, DetailActivity::class.java).apply {
                        putExtra("title", item.title)
                        putExtra("content", item.description)
                    })
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error getting announcements: ${exception.message}", Toast.LENGTH_LONG).show()
                pengumumanList.add(BeritaPengumumanItem("p1", "placeholder_announcement_image", "Diskon 50% BPHTB", "16 Jul 2025", "Pengumuman", "Diskon dalam rangka Hari Jadi Cianjur..."))
                rvPengumuman.adapter = BeritaPengumumanAdapter(pengumumanList) { item ->
                    startActivity(Intent(this, DetailActivity::class.java).apply {
                        putExtra("title", item.title)
                        putExtra("content", item.description)
                    })
                }
            }
    }

    private fun loadArtikel() {
        val artikelList = mutableListOf<ArtikelItem>()
        db.collection("articles")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val id = document.id
                    val title = document.getString("title") ?: ""
                    val date = document.getString("date") ?: ""
                    val category = document.getString("category") ?: "Artikel"
                    val description = document.getString("description") ?: ""
                    val imageUrl = document.getString("imageUrl") ?: "" // Nama drawable

                    artikelList.add(ArtikelItem(id, imageUrl, title, date, category, description))
                }
                rvArtikel.adapter = ArtikelAdapter(artikelList) { item ->
                    startActivity(Intent(this, DetailActivity::class.java).apply {
                        putExtra("title", item.title)
                        putExtra("content", item.description)
                    })
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error getting articles: ${exception.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun loadLayanan() {
        val layananList = mutableListOf<LayananItem>()
        db.collection("services")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val id = document.id
                    val title = document.getString("title") ?: ""
                    val iconResName = document.getString("iconResId") ?: "" // Ambil nama resource

                    val iconResId = resources.getIdentifier(iconResName, "drawable", packageName)

                    if (iconResId != 0) {
                        layananList.add(LayananItem(id, iconResId, title))
                    } else {
                        layananList.add(LayananItem(id, R.drawable.placeholder_file_icon, title))
                    }
                }
                rvLayanan.adapter = LayananAdapter(layananList) { item ->
                    startActivity(Intent(this, DetailActivity::class.java).apply {
                        putExtra("title", item.title)
                        putExtra("content", "Informasi detail untuk layanan: ${item.title}")
                    })
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error getting services: ${exception.message}", Toast.LENGTH_LONG).show()
                layananList.clear()
                layananList.add(LayananItem("l1", R.drawable.placeholder_motorcycle_icon, "PKB (Default)"))
                layananList.add(LayananItem("l2", R.drawable.placeholder_car_icon, "BBNKB (Default)"))
                rvLayanan.adapter = LayananAdapter(layananList) { item ->
                    startActivity(Intent(this, DetailActivity::class.java).apply {
                        putExtra("title", item.title)
                        putExtra("content", "Informasi detail untuk layanan: ${item.title} (dari placeholder)")
                    })
                }
            }
    }

    // Fungsi untuk memuat ulasan dari Firestore
    private fun loadReviews() {
        val reviewList = mutableListOf<ReviewItem>()
        db.collection("reviews")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(5)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val id = document.id
                    val userId = document.getString("userId") ?: "Anonim"
                    val reviewText = document.getString("reviewText") ?: ""

                    // Penanganan timestamp yang kompatibel dengan format Date() dan Date().time
                    val timestamp: Long = when (val rawTimestamp = document.get("timestamp")) {
                        is com.google.firebase.Timestamp -> rawTimestamp.toDate().time
                        is Long -> rawTimestamp
                        else -> 0L
                    }

                    reviewList.add(ReviewItem(id, userId, reviewText, timestamp))
                }
                rvReviews.adapter = ReviewAdapter(reviewList)
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error getting reviews: ${exception.message}", Toast.LENGTH_LONG).show()
            }
    }
}