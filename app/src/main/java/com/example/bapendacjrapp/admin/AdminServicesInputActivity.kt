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

class AdminServicesInputActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var etServiceTitle: EditText
    private lateinit var etServiceIconResName: EditText
    private lateinit var etServiceDescription: EditText // Deklarasi EditText deskripsi
    private lateinit var btnAddService: Button
    private lateinit var ivServiceInputBack: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_services_input)

        db = Firebase.firestore

        etServiceTitle = findViewById(R.id.etServiceTitle)
        etServiceIconResName = findViewById(R.id.etServiceIconResName)
        etServiceDescription = findViewById(R.id.etServiceDescription) // Inisialisasi EditText deskripsi
        btnAddService = findViewById(R.id.btnAddService)
        ivServiceInputBack = findViewById(R.id.ivServiceInputBack)

        btnAddService.setOnClickListener {
            val title = etServiceTitle.text.toString().trim()
            val iconResName = etServiceIconResName.text.toString().trim()
            val description = etServiceDescription.text.toString().trim() // Ambil teks deskripsi

            if (title.isEmpty() || iconResName.isEmpty() || description.isEmpty()) { // Tambahkan validasi deskripsi
                Toast.makeText(this, "Mohon lengkapi semua bidang Layanan.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val iconResId = resources.getIdentifier(iconResName, "drawable", packageName)

            if (iconResId == 0) {
                Toast.makeText(this, "Nama drawable ikon tidak ditemukan. Pastikan sudah ada di folder drawable.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val serviceData = hashMapOf(
                "title" to title,
                "iconResId" to iconResName,
                "description" to description, // Simpan deskripsi
                "timestamp" to Date()
            )

            db.collection("services")
                .add(serviceData)
                .addOnSuccessListener { documentReference ->
                    Toast.makeText(this, "Layanan berhasil ditambahkan dengan ID: ${documentReference.id}", Toast.LENGTH_LONG).show()
                    etServiceTitle.text.clear()
                    etServiceIconResName.text.clear()
                    etServiceDescription.text.clear() // Bersihkan input deskripsi
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Gagal menambahkan layanan: ${e.message}", Toast.LENGTH_LONG).show()
                }
        }

        ivServiceInputBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}