package com.example.bapendacjrapp.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
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
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.firestore.Query
import android.widget.ImageButton // Import ImageButton

class HomeActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    private lateinit var vpBerita: ViewPager2
    // private lateinit var tabLayoutBeritaIndicator: TabLayout // Dihapus
    private lateinit var btnArrowLeft: ImageButton // Deklarasi panah kiri
    private lateinit var btnArrowRight: ImageButton // Deklarasi panah kanan
    private lateinit var rvPengumuman: RecyclerView
    private lateinit var rvArtikel: RecyclerView
    private lateinit var rvPimpinan: RecyclerView
    private lateinit var rvLayanan: RecyclerView
    private lateinit var rvReviews: RecyclerView

    private lateinit var tvVisiContent: TextView
    private lateinit var tvMisiContent: TextView
    private lateinit var ivStrukturOrganisasi: ImageView
    private lateinit var tvTujuanDanFungsiContent: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        db = Firebase.firestore
        auth = Firebase.auth

        val etUserReview = findViewById<EditText>(R.id.etUserReview)
        val btnSubmitReview = findViewById<Button>(R.id.btnSubmitReview)

        vpBerita = findViewById(R.id.vpBerita)
        // tabLayoutBeritaIndicator = findViewById(R.id.tabLayoutBeritaIndicator) // Dihapus
        btnArrowLeft = findViewById(R.id.btnArrowLeft) // Inisialisasi panah kiri
        btnArrowRight = findViewById(R.id.btnArrowRight) // Inisialisasi panah kanan
        rvPengumuman = findViewById(R.id.rvPengumuman)
        rvArtikel = findViewById(R.id.rvArtikel)
        rvPimpinan = findViewById(R.id.rvPimpinan)
        rvLayanan = findViewById(R.id.rvLayanan)
        rvReviews = findViewById(R.id.rvReviews)

        tvVisiContent = findViewById(R.id.tvVisiContent)
        tvMisiContent = findViewById(R.id.tvMisiContent)
        ivStrukturOrganisasi = findViewById(R.id.ivStrukturOrganisasi)
        tvTujuanDanFungsiContent = findViewById(R.id.tvTujuanDanFungsiContent)


        rvPengumuman.layoutManager = LinearLayoutManager(this)
        rvArtikel.layoutManager = LinearLayoutManager(this)
        rvPimpinan.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvLayanan.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvReviews.layoutManager = LinearLayoutManager(this)

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
                            loadReviews()
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

        // Tidak ada lagi tombol btnLihatSelengkapnya

        loadBerita()
        loadPengumuman()
        loadArtikel()
        loadLayanan()
        loadReviews()
        loadBapendaProfileContent()
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
                    val imageUrl = document.getString("imageUrl") ?: ""

                    beritaList.add(BeritaPengumumanItem(id, imageUrl, title, date, category, description))
                }
                vpBerita.adapter = NewsPagerAdapter(this, beritaList)

                // Hubungkan TabLayout dengan ViewPager2 untuk indikator - Dihapus
                /*
                TabLayoutMediator(tabLayoutBeritaIndicator, vpBerita) { tab, position ->
                    // Tidak perlu teks untuk tab karena kita pakai drawable sebagai indikator
                }.attach()
                */

                // Atur logika panah navigasi
                updateArrowVisibility(vpBerita.currentItem, beritaList.size)

                vpBerita.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        updateArrowVisibility(position, beritaList.size)
                    }
                })

                btnArrowLeft.setOnClickListener {
                    vpBerita.currentItem = vpBerita.currentItem - 1
                }

                btnArrowRight.setOnClickListener {
                    vpBerita.currentItem = vpBerita.currentItem + 1
                }

            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error getting news: ${exception.message}", Toast.LENGTH_LONG).show()
                vpBerita.adapter = NewsPagerAdapter(this, emptyList())
                updateArrowVisibility(0, 0) // Sembunyikan panah jika tidak ada berita
            }
    }

    // Fungsi untuk memperbarui visibilitas panah
    private fun updateArrowVisibility(currentPosition: Int, totalItems: Int) {
        if (totalItems <= 1) { // Jika hanya ada 0 atau 1 item, sembunyikan kedua panah
            btnArrowLeft.visibility = View.GONE
            btnArrowRight.visibility = View.GONE
        } else {
            btnArrowLeft.visibility = if (currentPosition == 0) View.GONE else View.VISIBLE
            btnArrowRight.visibility = if (currentPosition == totalItems - 1) View.GONE else View.VISIBLE
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
                    val imageResId = resources.getIdentifier(item.imageUrl, "drawable", packageName)
                    startActivity(Intent(this, DetailActivity::class.java).apply {
                        putExtra("title", item.title)
                        putExtra("content", item.description)
                        putExtra("image_res_id", if (imageResId != 0) imageResId else R.drawable.placeholder_announcement_image)
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
                        putExtra("image_res_id", R.drawable.placeholder_announcement_image)
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
                    val imageUrl = document.getString("imageUrl") ?: ""

                    artikelList.add(ArtikelItem(id, imageUrl, title, date, category, description))
                }
                rvArtikel.adapter = ArtikelAdapter(artikelList) { item ->
                    val imageResId = resources.getIdentifier(item.imageUrl, "drawable", packageName)
                    startActivity(Intent(this, DetailActivity::class.java).apply {
                        putExtra("title", item.title)
                        putExtra("content", item.description)
                        putExtra("image_res_id", if (imageResId != 0) imageResId else R.drawable.placeholder_article_image)
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
                    val iconResName = document.getString("iconResId") ?: ""
                    val description = document.getString("description") ?: ""

                    val iconResId = resources.getIdentifier(iconResName, "drawable", packageName)

                    if (iconResId != 0) {
                        layananList.add(LayananItem(id, iconResId, title, description))
                    } else {
                        layananList.add(LayananItem(id, R.drawable.placeholder_file_icon, title, description))
                    }
                }
                rvLayanan.adapter = LayananAdapter(layananList) { item ->
                    startActivity(Intent(this, DetailActivity::class.java).apply {
                        putExtra("title", item.title)
                        putExtra("content", item.description)
                        putExtra("image_res_id", item.iconResId)
                    })
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error getting services: ${exception.message}", Toast.LENGTH_LONG).show()
                layananList.clear()
                layananList.add(LayananItem("l1", R.drawable.placeholder_motorcycle_icon, "PKB (Default)", "Deskripsi default PKB."))
                layananList.add(LayananItem("l2", R.drawable.placeholder_car_icon, "BBNKB (Default)", "Deskripsi default BBNKB."))
                rvLayanan.adapter = LayananAdapter(layananList) { item ->
                    startActivity(Intent(this, DetailActivity::class.java).apply {
                        putExtra("title", item.title)
                        putExtra("content", item.description)
                        putExtra("image_res_id", item.iconResId)
                    })
                }
            }
    }

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

    private fun loadBapendaProfileContent() {
        db.collection("bapenda_profile").document("currentProfile")
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val visi = document.getString("visi") ?: "Visi belum diatur."
                    val misi = document.getString("misi") ?: "Misi belum diatur."
                    val strukturOrganisasiDrawableName = document.getString("strukturOrganisasi") ?: ""
                    val tujuanDanFungsi = document.getString("tujuanDanFungsi") ?: "Tujuan dan Fungsi belum diatur."
                    val pimpinanListString = document.getString("pimpinanList") ?: ""

                    tvVisiContent.text = visi
                    tvMisiContent.text = misi
                    tvTujuanDanFungsiContent.text = tujuanDanFungsi

                    val strukturOrganisasiResId = resources.getIdentifier(strukturOrganisasiDrawableName, "drawable", packageName)
                    if (strukturOrganisasiResId != 0) {
                        ivStrukturOrganisasi.setImageResource(strukturOrganisasiResId)
                        ivStrukturOrganisasi.setOnClickListener {
                            val intent = Intent(this, ImageViewerActivity::class.java).apply {
                                putExtra("image_res_id", strukturOrganisasiResId)
                            }
                            startActivity(intent)
                        }
                    } else {
                        ivStrukturOrganisasi.setImageResource(R.drawable.placeholder_file_icon)
                        ivStrukturOrganisasi.setOnClickListener(null)
                    }

                    val pimpinanList = parsePimpinanString(pimpinanListString)
                    rvPimpinan.adapter = PimpinanAdapter(pimpinanList) { item ->
                        startActivity(Intent(this, DetailActivity::class.java).apply {
                            putExtra("title", item.name)
                            putExtra("content", "Nama: ${item.name}\nJabatan: ${item.position}")
                            putExtra("image_res_id", item.imageUrl)
                        })
                    }

                } else {
                    tvVisiContent.text = "Visi belum tersedia. Silakan hubungi admin."
                    tvMisiContent.text = "Misi belum tersedia. Silakan hubungi admin."
                    tvTujuanDanFungsiContent.text = "Tujuan & Fungsi belum tersedia. Silakan hubungi admin."
                    ivStrukturOrganisasi.setImageResource(R.drawable.placeholder_file_icon)
                    ivStrukturOrganisasi.setOnClickListener(null)
                    rvPimpinan.adapter = PimpinanAdapter(emptyList()) {}
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Gagal memuat profil Bapenda: ${e.message}", Toast.LENGTH_LONG).show()
                tvVisiContent.text = "Gagal memuat visi."
                tvMisiContent.text = "Gagal memuat misi."
                tvTujuanDanFungsiContent.text = "Gagal memuat tujuan & fungsi."
                ivStrukturOrganisasi.setImageResource(R.drawable.placeholder_file_icon)
                ivStrukturOrganisasi.setOnClickListener(null)
                rvPimpinan.adapter = PimpinanAdapter(emptyList()) {}
            }
    }

    private fun parsePimpinanString(pimpinanString: String): List<PimpinanItem> {
        val pimpinanList = mutableListOf<PimpinanItem>()
        pimpinanString.lines().forEachIndexed { index, line ->
            val parts = line.split("|").map { it.trim() }
            if (parts.size == 3) {
                val id = "p${index + 1}"
                val name = parts[0]
                val position = parts[1]
                val drawableName = parts[2]

                val imageResId = resources.getIdentifier(drawableName, "drawable", packageName)

                if (imageResId != 0) {
                    pimpinanList.add(PimpinanItem(id, imageResId, name, position))
                } else {
                    pimpinanList.add(PimpinanItem(id, R.drawable.placeholder_pimpinan_1, name, position))
                }
            }
        }
        return pimpinanList
    }
}