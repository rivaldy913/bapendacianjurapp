package com.example.bapendacjrapp.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.bapendacjrapp.MainActivity
import com.example.bapendacjrapp.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Date

// Asumsi struktur data model yang Anda miliki
// Anda perlu memastikan kelas-kelas ini ada dan sesuai dengan data Firestore Anda
// Contoh: data class BeritaPengumumanItem(...), data class ArtikelItem(...), dll.

class HomeActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    private lateinit var vpBerita: ViewPager2
    private lateinit var btnArrowLeft: ImageButton
    private lateinit var btnArrowRight: ImageButton

    private lateinit var vpPengumuman: ViewPager2
    private lateinit var btnPengumumanArrowLeft: ImageButton
    private lateinit var btnPengumumanArrowRight: ImageButton

    private lateinit var btnLihatSelengkapnyaBerita: Button
    private lateinit var btnLihatSelengkapnyaPengumuman: Button
    private lateinit var btnLihatSelengkapnyaArtikel: Button

    private lateinit var rvArtikel: RecyclerView
    private lateinit var rvPimpinan: RecyclerView
    private lateinit var rvLayanan: RecyclerView
    private lateinit var rvReviews: RecyclerView

    private lateinit var tvVisiContent: TextView
    private lateinit var tvMisiContent: TextView
    private lateinit var ivStrukturOrganisasi: ImageView
    private lateinit var tvTujuanDanFungsiContent: TextView

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var etUserReview: EditText
    private lateinit var btnSubmitReview: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        db = Firebase.firestore
        auth = Firebase.auth

        // Inisialisasi View
        etUserReview = findViewById(R.id.etUserReview)
        btnSubmitReview = findViewById(R.id.btnSubmitReview)

        vpBerita = findViewById(R.id.vpBerita)
        btnArrowLeft = findViewById(R.id.btnArrowLeft)
        btnArrowRight = findViewById(R.id.btnArrowRight)

        vpPengumuman = findViewById(R.id.vpPengumuman)
        btnPengumumanArrowLeft = findViewById(R.id.btnPengumumanArrowLeft)
        btnPengumumanArrowRight = findViewById(R.id.btnPengumumanArrowRight)

        btnLihatSelengkapnyaBerita = findViewById(R.id.btnLihatSelengkapnyaBerita)
        btnLihatSelengkapnyaPengumuman = findViewById(R.id.btnLihatSelengkapnyaPengumuman)
        btnLihatSelengkapnyaArtikel = findViewById(R.id.btnLihatSelengkapnyaArtikel)

        rvArtikel = findViewById(R.id.rvArtikel)
        rvPimpinan = findViewById(R.id.rvPimpinan)
        rvLayanan = findViewById(R.id.rvLayanan)
        rvReviews = findViewById(R.id.rvReviews)

        tvVisiContent = findViewById(R.id.tvVisiContent)
        tvMisiContent = findViewById(R.id.tvMisiContent)
        ivStrukturOrganisasi = findViewById(R.id.ivStrukturOrganisasi)
        tvTujuanDanFungsiContent = findViewById(R.id.tvTujuanDanFungsiContent)

        bottomNavigationView = findViewById(R.id.bottom_navigation)

        // Setup RecyclerViews
        rvArtikel.layoutManager = LinearLayoutManager(this)
        rvPimpinan.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvLayanan.layoutManager = GridLayoutManager(this, 3)
        rvReviews.layoutManager = LinearLayoutManager(this)

        // Listener untuk tombol Kirim Ulasan
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
                            loadReviews() // Muat ulang ulasan setelah pengiriman
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Gagal: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                } else {
                    // Pengguna belum login, tampilkan dialog
                    showLoginRequiredDialog("Untuk mengirim ulasan, Anda perlu login terlebih dahulu.")
                }
            } else {
                Toast.makeText(this, "Ulasan tidak boleh kosong.", Toast.LENGTH_SHORT).show()
            }
        }

        // Listeners untuk "Lihat Selengkapnya"
        btnLihatSelengkapnyaBerita.setOnClickListener {
            val intent = Intent(this, AllNewsActivity::class.java)
            startActivity(intent)
            Toast.makeText(this, "Membuka halaman daftar semua berita.", Toast.LENGTH_SHORT).show()
        }

        btnLihatSelengkapnyaPengumuman.setOnClickListener {
            val intent = Intent(this, AllAnnouncementsActivity::class.java)
            startActivity(intent)
            Toast.makeText(this, "Membuka halaman daftar semua pengumuman.", Toast.LENGTH_SHORT).show()
        }

        btnLihatSelengkapnyaArtikel.setOnClickListener {
            val intent = Intent(this, AllArticlesActivity::class.java)
            startActivity(intent)
            Toast.makeText(this, "Membuka halaman daftar semua artikel.", Toast.LENGTH_SHORT).show()
        }

        // Listener untuk Bottom Navigation Bar
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    // Konten beranda sudah ditampilkan di aktivitas ini.
                    Toast.makeText(this, "Anda di halaman Beranda", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.navigation_layanan -> {
                    // Arahkan ke HomeActivity (seperti yang ada di file Anda sebelumnya, atau ke LayananActivity jika ada)
                    // Jika AllServicesActivity ada, gunakan ini:
                    // val intent = Intent(this, AllServicesActivity::class.java)
                    // startActivity(intent)
                    Toast.makeText(this, "Membuka Halaman Layanan", Toast.LENGTH_SHORT).show()
                    // Karena Layanan sudah dimuat di Home, Anda mungkin tidak perlu navigasi ke activity lain
                    // atau jika ada AllServicesActivity, navigasi ke sana
                    true
                }
                R.id.navigation_profile -> {
                    val currentUser = auth.currentUser
                    if (currentUser != null) {
                        // Pengguna sudah login, arahkan ke halaman profil
                        val intent = Intent(this, EditProfileActivity::class.java)
                        startActivity(intent)
                    } else {
                        // Pengguna belum login, tampilkan dialog
                        showLoginRequiredDialog("Untuk melihat dan mengedit profil Anda, Anda perlu login terlebih dahulu.")
                    }
                    true
                }
                else -> false
            }
        }

        // Muat data saat aktivitas dibuat
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

                updateArrowVisibility(vpBerita.currentItem, beritaList.size, "berita")

                vpBerita.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        updateArrowVisibility(position, beritaList.size, "berita")
                    }
                })

                btnArrowLeft.setOnClickListener {
                    if (vpBerita.currentItem > 0) {
                        vpBerita.currentItem = vpBerita.currentItem - 1
                    }
                }

                btnArrowRight.setOnClickListener {
                    if (vpBerita.currentItem < beritaList.size - 1) {
                        vpBerita.currentItem = vpBerita.currentItem + 1
                    }
                }

            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error getting news: ${exception.message}", Toast.LENGTH_LONG).show()
                vpBerita.adapter = NewsPagerAdapter(this, emptyList())
                updateArrowVisibility(0, 0, "berita")
            }
    }

    private fun updateArrowVisibility(currentPosition: Int, totalItems: Int, type: String) {
        val leftButton: ImageButton
        val rightButton: ImageButton

        if (type == "berita") {
            leftButton = findViewById(R.id.btnArrowLeft)
            rightButton = findViewById(R.id.btnArrowRight)
        } else if (type == "pengumuman") {
            leftButton = findViewById(R.id.btnPengumumanArrowLeft)
            rightButton = findViewById(R.id.btnPengumumanArrowRight)
        } else {
            return
        }

        if (totalItems <= 1) {
            leftButton.visibility = View.GONE
            rightButton.visibility = View.GONE
        } else {
            leftButton.visibility = if (currentPosition == 0) View.GONE else View.VISIBLE
            rightButton.visibility = if (currentPosition == totalItems - 1) View.GONE else View.VISIBLE
        }
    }

    private fun loadPengumuman() {
        val pengumumanList = mutableListOf<BeritaPengumumanItem>()
        db.collection("announcements")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                if (result.isEmpty) {
                    // Data placeholder jika Firestore kosong
                    pengumumanList.add(BeritaPengumumanItem("p1", "diskon_bphtb", "Diskon 50% BPHTB", "01 Jul 2025", "Pengumuman", "Diskon dalam rangka Hari Jadi Cianjur Periode 1 Juli s/d 31 Juli 2025."))
                    pengumumanList.add(BeritaPengumumanItem("p2", "pemutihan_2025", "Pemutihan Pajak Kendaraan Bermotor 2025", "01 Jul 2025", "Pengumuman", "Pemutihan Pajak Kendaraan Bermotor diperpanjang hingga 30 September 2025."))
                    pengumumanList.add(BeritaPengumumanItem("p3", "ultah_cianjur", "Hari Jadi Cianjur Ke-348", "25 Jul 2025", "Event", "Upacara Perayaan Hari Jadi Cianjur Ke-348."))
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
                vpPengumuman.adapter = NewsPagerAdapter(this, pengumumanList)
                updateArrowVisibility(vpPengumuman.currentItem, pengumumanList.size, "pengumuman")

                vpPengumuman.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        updateArrowVisibility(position, pengumumanList.size, "pengumuman")
                    }
                })

                btnPengumumanArrowLeft.setOnClickListener {
                    if (vpPengumuman.currentItem > 0) {
                        vpPengumuman.currentItem = vpPengumuman.currentItem - 1
                    }
                }

                btnPengumumanArrowRight.setOnClickListener {
                    if (vpPengumuman.currentItem < pengumumanList.size - 1) {
                        vpPengumuman.currentItem = vpPengumuman.currentItem + 1
                    }
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error getting announcements: ${exception.message}", Toast.LENGTH_LONG).show()
                // Tetap tampilkan placeholder jika gagal memuat
                val defaultPengumumanList = mutableListOf<BeritaPengumumanItem>()
                defaultPengumumanList.add(BeritaPengumumanItem("p1", "placeholder_announcement_image", "Gagal Memuat Pengumuman", "N/A", "Pengumuman", "Tidak dapat memuat pengumuman dari server."))
                vpPengumuman.adapter = NewsPagerAdapter(this, defaultPengumumanList)
                updateArrowVisibility(0, 0, "pengumuman")
            }
    }

    private fun loadArtikel() {
        val artikelList = mutableListOf<ArtikelItem>()
        db.collection("articles")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(3) // Batasi hingga 3 artikel
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
                // Tambahkan placeholder jika gagal
                val defaultArtikelList = mutableListOf<ArtikelItem>()
                defaultArtikelList.add(ArtikelItem("a1", "placeholder_article_image", "Gagal Memuat Artikel", "N/A", "Artikel", "Tidak dapat memuat artikel dari server."))
                rvArtikel.adapter = ArtikelAdapter(defaultArtikelList) { item ->
                    startActivity(Intent(this, DetailActivity::class.java).apply {
                        putExtra("title", item.title)
                        putExtra("content", item.description)
                        putExtra("image_res_id", R.drawable.placeholder_article_image)
                    })
                }
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
                // Tambahkan placeholder jika gagal
                val defaultLayananList = mutableListOf<LayananItem>()
                defaultLayananList.add(LayananItem("l1", R.drawable.placeholder_motorcycle_icon, "PKB (Error)", "Deskripsi default PKB jika gagal dimuat."))
                defaultLayananList.add(LayananItem("l2", R.drawable.placeholder_car_icon, "BBNKB (Error)", "Deskripsi default BBNKB jika gagal dimuat."))
                rvLayanan.adapter = LayananAdapter(defaultLayananList) { item ->
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
                // Tambahkan placeholder jika gagal
                val defaultReviewList = mutableListOf<ReviewItem>()
                defaultReviewList.add(ReviewItem("r1", "admin", "Aplikasi ini bagus sekali!", Date().time))
                rvReviews.adapter = ReviewAdapter(defaultReviewList)
            }
    }

    private fun loadBapendaProfileContent() {
        val pimpinanList = mutableListOf<PimpinanItem>()

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

                    val pimpinanData = parsePimpinanString(pimpinanListString)
                    pimpinanList.addAll(pimpinanData)
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
                    // Gunakan placeholder default jika gambar tidak ditemukan
                    pimpinanList.add(PimpinanItem(id, R.drawable.placeholder_pimpinan_1, name, position))
                }
            }
        }
        return pimpinanList
    }

    private fun showLoginRequiredDialog(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Login Diperlukan")
            .setMessage(message)
            .setPositiveButton("Login Sekarang") { dialog, _ ->
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                dialog.dismiss()
            }
            .setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}