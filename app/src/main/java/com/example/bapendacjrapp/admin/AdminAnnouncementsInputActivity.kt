package com.example.bapendacjrapp.admin

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView // Impor TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bapendacjrapp.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Date
import android.widget.LinearLayout // Impor LinearLayout untuk cast eksplisit

class AdminAnnouncementsInputActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var etAnnouncementTitle: EditText
    private lateinit var etAnnouncementDate: EditText
    private lateinit var etAnnouncementDescription: EditText
    private lateinit var etAnnouncementImageUrl: EditText
    private lateinit var btnAddAnnouncement: Button
    private lateinit var ivAnnouncementInputBack: ImageView
    private lateinit var tvToolbarTitle: TextView // Ditambahkan untuk judul toolbar dinamis

    private var announcementIdToEdit: String? = null // Untuk menyimpan ID jika kita mengedit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_announcements_input)

        db = Firebase.firestore

        etAnnouncementTitle = findViewById(R.id.etAnnouncementTitle)
        etAnnouncementDate = findViewById(R.id.etAnnouncementDate)
        etAnnouncementDescription = findViewById(R.id.etAnnouncementDescription)
        etAnnouncementImageUrl = findViewById(R.id.etAnnouncementImageUrl)
        btnAddAnnouncement = findViewById(R.id.btnAddAnnouncement)
        ivAnnouncementInputBack = findViewById(R.id.ivAnnouncementInputBack)

        // Inisialisasi tvToolbarTitle dari LinearLayout toolbar, dengan cast eksplisit
        tvToolbarTitle = findViewById<LinearLayout>(R.id.announcementInputToolbar).findViewById<TextView>(R.id.tvToolbarTitle)

        // Pastikan TextView untuk judul toolbar di activity_admin_announcements_input.xml memiliki ID tvToolbarTitle
        // (Lihat instruksi di bawah untuk modifikasi XML jika belum ada)

        // Periksa apakah kita dalam mode edit
        announcementIdToEdit = intent.getStringExtra("ANNOUNCEMENT_ID")
        if (announcementIdToEdit != null) {
            // Isi bidang untuk pengeditan
            tvToolbarTitle.text = "Edit Pengumuman" // Atur judul toolbar untuk mode edit
            btnAddAnnouncement.text = "Simpan Perubahan" // Ubah teks tombol
            etAnnouncementTitle.setText(intent.getStringExtra("ANNOUNCEMENT_TITLE"))
            etAnnouncementDate.setText(intent.getStringExtra("ANNOUNCEMENT_DATE"))
            etAnnouncementDescription.setText(intent.getStringExtra("ANNOUNCEMENT_DESCRIPTION"))
            etAnnouncementImageUrl.setText(intent.getStringExtra("ANNOUNCEMENT_IMAGE_URL"))
        } else {
            tvToolbarTitle.text = "Input Pengumuman Baru" // Atur judul toolbar untuk mode tambah
            btnAddAnnouncement.text = "Tambah Pengumuman" // Atur teks tombol untuk mode tambah
        }

        btnAddAnnouncement.setOnClickListener {
            val title = etAnnouncementTitle.text.toString().trim()
            val date = etAnnouncementDate.text.toString().trim()
            val description = etAnnouncementDescription.text.toString().trim()
            val imageUrlString = etAnnouncementImageUrl.text.toString().trim()
            val category = "Pengumuman" // Kategori tetap "Pengumuman"

            if (title.isEmpty() || date.isEmpty() || description.isEmpty() || imageUrlString.isEmpty()) {
                Toast.makeText(this, "Mohon lengkapi semua bidang Pengumuman.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Verifikasi apakah sumber drawable ada
            val imageResId = resources.getIdentifier(imageUrlString, "drawable", packageName)
            if (imageResId == 0) {
                Toast.makeText(this, "Nama drawable gambar tidak ditemukan. Pastikan sudah ada di folder drawable.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val announcementData = hashMapOf(
                "title" to title,
                "date" to date,
                "category" to category,
                "description" to description,
                "imageUrl" to imageUrlString,
                "timestamp" to Date()
            )

            if (announcementIdToEdit != null) {
                // Perbarui pengumuman yang sudah ada
                db.collection("announcements").document(announcementIdToEdit!!)
                    .set(announcementData) // Gunakan set untuk menimpa dokumen yang sudah ada
                    .addOnSuccessListener {
                        Toast.makeText(this, "Pengumuman berhasil diperbarui!", Toast.LENGTH_LONG).show()
                        finish() // Kembali ke AdminManageAnnouncementsActivity
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Gagal memperbarui pengumuman: ${e.message}", Toast.LENGTH_LONG).show()
                    }
            } else {
                // Tambahkan pengumuman baru
                db.collection("announcements")
                    .add(announcementData)
                    .addOnSuccessListener { documentReference ->
                        Toast.makeText(this, "Pengumuman berhasil ditambahkan dengan ID: ${documentReference.id}", Toast.LENGTH_LONG).show()
                        etAnnouncementTitle.text.clear()
                        etAnnouncementDate.text.clear()
                        etAnnouncementDescription.text.clear()
                        etAnnouncementImageUrl.text.clear()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Gagal menambahkan pengumuman: ${e.message}", Toast.LENGTH_LONG).show()
                    }
            }
        }

        ivAnnouncementInputBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}