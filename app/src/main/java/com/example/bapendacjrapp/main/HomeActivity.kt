package com.example.bapendacjrapp

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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Date

class HomeActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        db = Firebase.firestore
        auth = Firebase.auth

        val etUserReview = findViewById<EditText>(R.id.etUserReview)
        val btnSubmitReview = findViewById<Button>(R.id.btnSubmitReview)

        btnSubmitReview.setOnClickListener {
            val reviewText = etUserReview.text.toString().trim()

            if (reviewText.isNotEmpty()) {
                val userId = auth.currentUser?.uid
                if (userId != null) {
                    val review = hashMapOf(
                        "userId" to userId,
                        "reviewText" to reviewText,
                        "timestamp" to Date()
                    )
                    db.collection("reviews")
                        .add(review)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Ulasan berhasil dikirim!", Toast.LENGTH_SHORT).show()
                            etUserReview.text.clear()
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

        // Berita
        val beritaList = listOf(
            BeritaPengumumanItem("b1", R.drawable.placeholder_news_image, "SINERGI SEGITIGA", "18 Jul 2025", "Berita", "Pusat Pengelolaan Pendapatan Daerah..."),
            BeritaPengumumanItem("b2", R.drawable.placeholder_news_image_2, "VERIFIKASI DATA", "17 Jul 2025", "Berita", "Verifikasi data statistik sektoral...")
        )
        val rvBerita = findViewById<RecyclerView>(R.id.rvBerita)
        rvBerita.layoutManager = LinearLayoutManager(this)
        rvBerita.adapter = BeritaPengumumanAdapter(beritaList) {}

        // Pengumuman
        val pengumumanList = listOf(
            BeritaPengumumanItem("p1", R.drawable.placeholder_announcement_image, "Diskon 50% BPHTB", "16 Jul 2025", "Pengumuman", "Diskon dalam rangka Hari Jadi Cianjur...")
        )
        val rvPengumuman = findViewById<RecyclerView>(R.id.rvPengumuman)
        rvPengumuman.layoutManager = LinearLayoutManager(this)
        rvPengumuman.adapter = BeritaPengumumanAdapter(pengumumanList) {}

        // Artikel
        val artikelList = listOf(
            ArtikelItem("a1", R.drawable.placeholder_article_image, "BAPENDA DAN KEJAKSAAN", "08 Jul 2025", "Artikel", "Sinergi Bapenda dan Kejaksaan...")
        )
        val rvArtikel = findViewById<RecyclerView>(R.id.rvArtikel)
        rvArtikel.layoutManager = LinearLayoutManager(this)
        rvArtikel.adapter = ArtikelAdapter(artikelList) {}

        // Pimpinan
        val pimpinanList = listOf(
            PimpinanItem("p1", R.drawable.placeholder_pimpinan_1, "Dr. H. M. Ridwan", "Kepala Bapenda")
        )
        val rvPimpinan = findViewById<RecyclerView>(R.id.rvPimpinan)
        rvPimpinan.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvPimpinan.adapter = PimpinanAdapter(pimpinanList) {}

        // Layanan
        val layananList = listOf(
            LayananItem("l1", R.drawable.placeholder_motorcycle_icon, "PKB"),
            LayananItem("l2", R.drawable.placeholder_car_icon, "BBNKB")
        )
        val rvLayanan = findViewById<RecyclerView>(R.id.rvLayanan)
        rvLayanan.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvLayanan.adapter = LayananAdapter(layananList) {}
    }
}
