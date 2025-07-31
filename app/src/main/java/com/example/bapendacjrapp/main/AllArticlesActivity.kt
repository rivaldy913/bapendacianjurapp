package com.example.bapendacjrapp.main

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bapendacjrapp.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AllArticlesActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var rvAllArticles: RecyclerView
    private lateinit var ivBack: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_articles) // Menggunakan layout baru

        db = Firebase.firestore

        rvAllArticles = findViewById(R.id.rvAllArticles)
        ivBack = findViewById(R.id.ivBack)

        rvAllArticles.layoutManager = LinearLayoutManager(this)

        ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        loadAllArticles()
    }

    private fun loadAllArticles() {
        val articleList = mutableListOf<ArtikelItem>()
        db.collection("articles")
            .orderBy("timestamp", Query.Direction.DESCENDING) // Urutkan berdasarkan waktu terbaru
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val id = document.id
                    val title = document.getString("title") ?: ""
                    val date = document.getString("date") ?: ""
                    val category = document.getString("category") ?: "Artikel"
                    val description = document.getString("description") ?: ""
                    val imageUrl = document.getString("imageUrl") ?: ""

                    articleList.add(ArtikelItem(id, imageUrl, title, date, category, description))
                }
                rvAllArticles.adapter = ArtikelAdapter(articleList) { item ->
                    // Ketika item artikel diklik, arahkan ke DetailActivity
                    val imageResId = resources.getIdentifier(item.imageUrl, "drawable", packageName)
                    val intent = android.content.Intent(this, DetailActivity::class.java).apply {
                        putExtra("title", item.title)
                        putExtra("content", item.description)
                        putExtra("image_res_id", if (imageResId != 0) imageResId else R.drawable.placeholder_article_image)
                    }
                    startActivity(intent)
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error getting all articles: ${exception.message}", Toast.LENGTH_LONG).show()
                // Tampilkan pesan atau placeholder jika gagal memuat data
            }
    }
}