// --- DetailActivity.kt ---
// Activity generik untuk menampilkan halaman detail (Profil, Jenis Pajak, Berita, Artikel, Edit Profil)
// Menggunakan XML layout (activity_detail.xml) untuk tampilan UI.

package com.example.bapendacjrapp.main

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.bapendacjrapp.R // Perbarui: Import R dari package root

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val title = intent.getStringExtra("title") ?: "Detail"
        val tvTitle = findViewById<TextView>(R.id.tvDetailTitle)
        val tvContent = findViewById<TextView>(R.id.tvDetailContent)
        val ivBack = findViewById<ImageView>(R.id.ivBack)

        tvTitle.text = title
        // Set konten sesuai judul
        tvContent.text = when (title) {
            "Profil Bapenda" -> "Ini adalah halaman profil Badan Pengelolaan dan Pendapatan Daerah Kabupaten Cianjur. Berisi informasi visi, misi, struktur organisasi, dan sejarah Bapenda."
            "Jenis Pajak" -> "Halaman ini menampilkan daftar jenis-jenis pajak daerah yang dikelola oleh Bapenda Cianjur, seperti PBB, BPHTB, Pajak Kendaraan Bermotor, dll. Dilengkapi dengan informasi tarif dan tata cara pembayaran."
            "Berita Terbaru" -> "Dapatkan informasi berita terkini seputar kebijakan pajak, kegiatan Bapenda, dan pengumuman penting lainnya untuk masyarakat Cianjur."
            "Artikel Edukasi" -> "Kumpulan artikel informatif dan edukatif mengenai perpajakan daerah, manfaat pajak, serta tips-tips terkait pengelolaan keuangan pribadi dan bisnis."
            "Edit Profil" -> "Halaman ini memungkinkan pengguna untuk memperbarui informasi profil mereka, seperti nama, alamat, nomor telepon, dan email. Fitur ini memerlukan autentikasi pengguna."
            else -> "Konten untuk halaman ini belum tersedia."
        }

        ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed() // Kembali ke Activity sebelumnya
        }
    }
}