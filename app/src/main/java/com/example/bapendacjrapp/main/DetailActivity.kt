package com.example.bapendacjrapp.main

import android.content.Intent // Import Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast // Import Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bapendacjrapp.R
import com.google.android.material.bottomnavigation.BottomNavigationView // Import BottomNavigationView

class DetailActivity : AppCompatActivity() {

    private lateinit var ivDetailImage: ImageView
    private lateinit var tvDetailTitle: TextView
    private lateinit var tvToolbarTitle: TextView
    private lateinit var bottomNavigationView: BottomNavigationView // Deklarasi BottomNavigationView baru

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val title = intent.getStringExtra("title") ?: "Detail"
        tvDetailTitle = findViewById(R.id.tvDetailTitle)
        val tvContent = findViewById<TextView>(R.id.tvDetailContent)
        val ivBack = findViewById<ImageView>(R.id.ivBack)
        ivDetailImage = findViewById(R.id.ivDetailImage)
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle)
        bottomNavigationView = findViewById(R.id.bottom_navigation) // Inisialisasi BottomNavigationView

        tvDetailTitle.text = title
        tvToolbarTitle.text = ""

        val imageResId = intent.getIntExtra("image_res_id", 0)
        if (imageResId != 0) {
            ivDetailImage.setImageResource(imageResId)
            ivDetailImage.visibility = View.VISIBLE
        } else {
            ivDetailImage.visibility = View.GONE
        }

        val content = intent.getStringExtra("content")
        if (content != null) {
            tvContent.text = content
        } else {
            tvContent.text = when (title) {
                "Profil Bapenda" -> "Ini adalah halaman profil Badan Pengelolaan dan Pendapatan Daerah Kabupaten Cianjur. Berisi informasi visi, misi, struktur organisasi, dan sejarah Bapenda."
                "Jenis Pajak" -> "Halaman ini menampilkan daftar jenis-jenis pajak daerah yang dikelola oleh Bapenda Cianjur, seperti PBB, BPHTB, Pajak Kendaraan Bermotor, dll. Dilengkapi dengan informasi tarif dan tata cara pembayaran."
                "Berita Terbaru" -> "Dapatkan informasi berita terkini seputar kebijakan pajak, kegiatan Bapenda, dan pengumuman penting lainnya untuk masyarakat Cianjur."
                "Artikel Edukasi" -> "Kumpulan artikel informatif dan edukatif mengenai perpajakan daerah, manfaat pajak, serta tips-tips terkait pengelolaan keuangan pribadi dan bisnis."
                "Edit Profil" -> "Halaman ini memungkinkan pengguna untuk memperbarui informasi profil mereka, seperti nama, alamat, nomor telepon, dan email. Fitur ini memerlukan autentikasi pengguna."
                else -> "Konten untuk halaman ini belum tersedia."
            }
        }

        ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Listener untuk Bottom Navigation Bar
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    val intent = Intent(this, HomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivity(intent)
                    true
                }
                R.id.navigation_layanan -> {
                    val intent = Intent(this, HomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    // Jika HomeActivity dapat langsung menampilkan bagian layanan, tambahkan extra
                    // intent.putExtra("navigateTo", "layanan")
                    startActivity(intent)
                    Toast.makeText(this, "Membuka Halaman Layanan", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.navigation_profile -> {
                    val intent = Intent(this, EditProfileActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(this, "Membuka Halaman Edit Profil", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
    }
}