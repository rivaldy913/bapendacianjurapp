package com.example.bapendacjrapp.admin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView // Import CardView
import com.example.bapendacjrapp.R

class AdminDashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard) // Menggunakan layout dashboard yang baru

        // Inisialisasi CardView
        val cardManageNews = findViewById<CardView>(R.id.cardManageNews)
        val cardManageArticles = findViewById<CardView>(R.id.cardManageArticles)
        val cardManageAnnouncements = findViewById<CardView>(R.id.cardManageAnnouncements)
        val cardManageServices = findViewById<CardView>(R.id.cardManageServices)
        val cardManageProfile = findViewById<CardView>(R.id.cardManageProfile)
        val cardEditAdminProfile = findViewById<CardView>(R.id.cardEditAdminProfile)

        // Set Listener untuk setiap CardView
        cardManageNews.setOnClickListener {
            val intent = Intent(this, AdminNewsInputActivity::class.java)
            startActivity(intent)
        }

        cardManageArticles.setOnClickListener {
            val intent = Intent(this, AdminArticleInputActivity::class.java)
            startActivity(intent)
        }

        // --- MULAI PERBARUIAN DI SINI ---
        cardManageAnnouncements.setOnClickListener {
            val intent = Intent(this, AdminAnnouncementsInputActivity::class.java)
            startActivity(intent)
        }

        cardManageServices.setOnClickListener {
            val intent = Intent(this, AdminServicesInputActivity::class.java)
            startActivity(intent)
        }
        // --- AKHIR PERBARUIAN DI SINI ---

        cardManageProfile.setOnClickListener {
            // TODO: Buat AdminProfileInputActivity dan pindahkan logika ke sana
            Toast.makeText(this, "Kelola Profil Bapenda (belum diimplementasikan)", Toast.LENGTH_SHORT).show()
        }

        cardEditAdminProfile.setOnClickListener {
            // TODO: Buat AdminEditAdminProfileActivity dan pindahkan logika ke sana
            Toast.makeText(this, "Edit Profil Admin (belum diimplementasikan)", Toast.LENGTH_SHORT).show()
        }
    }
}