package com.example.bapendacjrapp.admin

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bapendacjrapp.R
import com.example.bapendacjrapp.main.LayananItem // Menggunakan LayananItem
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AdminManageServicesActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var rvAdminServices: RecyclerView
    private lateinit var btnAddService: Button
    private lateinit var ivBack: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_manage_services)

        db = Firebase.firestore

        rvAdminServices = findViewById(R.id.rvAdminServices)
        btnAddService = findViewById(R.id.btnAddService)
        ivBack = findViewById(R.id.ivBack)

        rvAdminServices.layoutManager = LinearLayoutManager(this)

        ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        btnAddService.setOnClickListener {
            val intent = Intent(this, AdminServicesInputActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        loadServices()
    }

    private fun loadServices() {
        val serviceList = mutableListOf<LayananItem>()
        db.collection("services")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val id = document.id
                    val title = document.getString("title") ?: ""
                    val iconResName = document.getString("iconResId") ?: "" // Nama drawable icon
                    val description = document.getString("description") ?: ""

                    // Konversi nama drawable menjadi resource ID
                    val iconResId = resources.getIdentifier(iconResName, "drawable", packageName)

                    if (iconResId != 0) {
                        serviceList.add(LayananItem(id, iconResId, title, description))
                    } else {
                        // Gunakan placeholder jika resource icon tidak ditemukan
                        serviceList.add(LayananItem(id, R.drawable.placeholder_file_icon, title, description))
                    }
                }
                rvAdminServices.adapter = AdminServiceAdapter(serviceList,
                    onEditClick = { serviceItem ->
                        val intent = Intent(this, AdminServicesInputActivity::class.java).apply {
                            putExtra("SERVICE_ID", serviceItem.id)
                            putExtra("SERVICE_TITLE", serviceItem.title)
                            // Karena iconResId adalah Int, kita perlu mengirim nama stringnya
                            putExtra("SERVICE_ICON_RES_NAME", resources.getResourceEntryName(serviceItem.iconResId))
                            putExtra("SERVICE_DESCRIPTION", serviceItem.description)
                        }
                        startActivity(intent)
                    },
                    onDeleteClick = { serviceItem ->
                        showDeleteConfirmationDialog(serviceItem)
                    }
                )
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error mendapatkan layanan: ${exception.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun showDeleteConfirmationDialog(serviceItem: LayananItem) {
        AlertDialog.Builder(this)
            .setTitle("Hapus Layanan")
            .setMessage("Apakah Anda yakin ingin menghapus layanan '${serviceItem.title}'?")
            .setPositiveButton("Hapus") { dialog, _ ->
                deleteService(serviceItem.id)
                dialog.dismiss()
            }
            .setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun deleteService(serviceId: String) {
        db.collection("services").document(serviceId)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(this, "Layanan berhasil dihapus.", Toast.LENGTH_SHORT).show()
                loadServices() // Muat ulang daftar
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error menghapus layanan: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}