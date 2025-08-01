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
import android.widget.LinearLayout // Impor LinearLayout untuk cast eksplisit
import android.widget.TextView // Impor TextView

class AdminServicesInputActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var etServiceTitle: EditText
    private lateinit var etServiceIconResName: EditText
    private lateinit var etServiceDescription: EditText
    private lateinit var btnAddService: Button
    private lateinit var ivServiceInputBack: ImageView
    private lateinit var tvToolbarTitle: TextView // Ditambahkan untuk judul toolbar dinamis

    private var serviceIdToEdit: String? = null // Untuk menyimpan ID jika kita mengedit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_services_input)

        db = Firebase.firestore

        etServiceTitle = findViewById(R.id.etServiceTitle)
        etServiceIconResName = findViewById(R.id.etServiceIconResName)
        etServiceDescription = findViewById(R.id.etServiceDescription)
        btnAddService = findViewById(R.id.btnAddService)
        ivServiceInputBack = findViewById(R.id.ivServiceInputBack)
        tvToolbarTitle = findViewById<LinearLayout>(R.id.serviceInputToolbar).findViewById<TextView>(R.id.tvToolbarTitle)


        // Periksa apakah kita dalam mode edit
        serviceIdToEdit = intent.getStringExtra("SERVICE_ID")
        if (serviceIdToEdit != null) {
            // Isi bidang untuk pengeditan
            tvToolbarTitle.text = "Edit Layanan" // Atur judul toolbar untuk mode edit
            btnAddService.text = "Simpan Perubahan" // Ubah teks tombol
            etServiceTitle.setText(intent.getStringExtra("SERVICE_TITLE"))
            etServiceIconResName.setText(intent.getStringExtra("SERVICE_ICON_RES_NAME"))
            etServiceDescription.setText(intent.getStringExtra("SERVICE_DESCRIPTION"))
        } else {
            tvToolbarTitle.text = "Input Layanan Baru" // Atur judul toolbar untuk mode tambah
            btnAddService.text = "Tambah Layanan" // Atur teks tombol untuk mode tambah
        }


        btnAddService.setOnClickListener {
            val title = etServiceTitle.text.toString().trim()
            val iconResName = etServiceIconResName.text.toString().trim()
            val description = etServiceDescription.text.toString().trim()

            if (title.isEmpty() || iconResName.isEmpty() || description.isEmpty()) {
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
                "description" to description,
                "timestamp" to Date()
            )

            if (serviceIdToEdit != null) {
                // Perbarui layanan yang sudah ada
                db.collection("services").document(serviceIdToEdit!!)
                    .set(serviceData) // Gunakan set untuk menimpa dokumen yang sudah ada
                    .addOnSuccessListener {
                        Toast.makeText(this, "Layanan berhasil diperbarui!", Toast.LENGTH_LONG).show()
                        finish() // Kembali ke AdminManageServicesActivity
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Gagal memperbarui layanan: ${e.message}", Toast.LENGTH_LONG).show()
                    }
            } else {
                // Tambahkan layanan baru
                db.collection("services")
                    .add(serviceData)
                    .addOnSuccessListener { documentReference ->
                        Toast.makeText(this, "Layanan berhasil ditambahkan dengan ID: ${documentReference.id}", Toast.LENGTH_LONG).show()
                        etServiceTitle.text.clear()
                        etServiceIconResName.text.clear()
                        etServiceDescription.text.clear()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Gagal menambahkan layanan: ${e.message}", Toast.LENGTH_LONG).show()
                    }
            }
        }

        ivServiceInputBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}