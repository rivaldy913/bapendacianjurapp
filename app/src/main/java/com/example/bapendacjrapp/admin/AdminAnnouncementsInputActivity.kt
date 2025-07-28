package com.example.bapendacjrapp.admin

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bapendacjrapp.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Date

class AdminAnnouncementsInputActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_announcements_input)

        db = Firebase.firestore

        val etAnnouncementTitle = findViewById<EditText>(R.id.etAnnouncementTitle)
        val etAnnouncementDate = findViewById<EditText>(R.id.etAnnouncementDate)
        val etAnnouncementDescription = findViewById<EditText>(R.id.etAnnouncementDescription)
        val etAnnouncementImageUrl = findViewById<EditText>(R.id.etAnnouncementImageUrl)
        val btnAddAnnouncement = findViewById<Button>(R.id.btnAddAnnouncement)
        val ivAnnouncementInputBack = findViewById<ImageView>(R.id.ivAnnouncementInputBack)

        btnAddAnnouncement.setOnClickListener {
            val title = etAnnouncementTitle.text.toString().trim()
            val date = etAnnouncementDate.text.toString().trim()
            val description = etAnnouncementDescription.text.toString().trim()
            val imageUrlString = etAnnouncementImageUrl.text.toString().trim()
            val category = "Pengumuman"

            if (title.isEmpty() || date.isEmpty() || description.isEmpty() || imageUrlString.isEmpty()) {
                Toast.makeText(this, "Mohon lengkapi semua bidang Pengumuman.", Toast.LENGTH_SHORT).show()
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

        ivAnnouncementInputBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}