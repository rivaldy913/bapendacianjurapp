// --- DetailActivity.kt ---
package com.example.bapendacjrapp.main

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.bapendacjrapp.R

class DetailActivity : AppCompatActivity() {

    private lateinit var ivDetailImage: ImageView
    private lateinit var tvDetailTitle: TextView // Deklarasi TextView judul di bawah gambar
    private lateinit var tvToolbarTitle: TextView // Deklarasi TextView judul di toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val title = intent.getStringExtra("title") ?: "Detail"
        tvDetailTitle = findViewById(R.id.tvDetailTitle) // Inisialisasi TextView judul di bawah gambar
        val tvContent = findViewById<TextView>(R.id.tvDetailContent)
        val ivBack = findViewById<ImageView>(R.id.ivBack)
        ivDetailImage = findViewById(R.id.ivDetailImage)
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle) // Inisialisasi TextView judul di toolbar

        tvDetailTitle.text = title // Set judul di bawah gambar
        tvToolbarTitle.text = "" // Kosongkan judul di toolbar

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
            // Konten placeholder jika tidak ada 'content' yang diteruskan
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
    }
}