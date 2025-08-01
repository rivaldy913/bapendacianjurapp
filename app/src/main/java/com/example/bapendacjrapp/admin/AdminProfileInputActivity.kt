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
import android.widget.LinearLayout // Impor LinearLayout
import android.widget.TextView // Impor TextView

class AdminProfileInputActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var etVisi: EditText
    private lateinit var etMisi: EditText
    private lateinit var etStrukturOrganisasi: EditText
    private lateinit var etTujuanDanFungsi: EditText
    private lateinit var etPimpinanList: EditText
    private lateinit var btnSaveProfile: Button
    private lateinit var ivProfileInputBack: ImageView
    private lateinit var tvToolbarTitle: TextView // Deklarasi tvToolbarTitle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_profile_input)

        db = Firebase.firestore

        etVisi = findViewById(R.id.etVisi)
        etMisi = findViewById(R.id.etMisi)
        etStrukturOrganisasi = findViewById(R.id.etStrukturOrganisasi)
        etTujuanDanFungsi = findViewById(R.id.etTujuanDanFungsi)
        etPimpinanList = findViewById(R.id.etPimpinanList)
        btnSaveProfile = findViewById(R.id.btnSaveProfile)
        ivProfileInputBack = findViewById(R.id.ivProfileInputBack)

        // Inisialisasi tvToolbarTitle dari LinearLayout toolbar, dengan cast eksplisit
        tvToolbarTitle = findViewById<LinearLayout>(R.id.profileInputToolbar).findViewById<TextView>(R.id.tvToolbarTitle)
        tvToolbarTitle.text = "Kelola Profil Bapenda" // Atur judul toolbar

        // Muat data profil yang sudah ada (jika ada)
        loadBapendaProfile()

        btnSaveProfile.setOnClickListener {
            saveBapendaProfile()
        }

        ivProfileInputBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun loadBapendaProfile() {
        db.collection("bapenda_profile").document("currentProfile")
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    etVisi.setText(document.getString("visi"))
                    etMisi.setText(document.getString("misi"))
                    etStrukturOrganisasi.setText(document.getString("strukturOrganisasi"))
                    etTujuanDanFungsi.setText(document.getString("tujuanDanFungsi"))
                    etPimpinanList.setText(document.getString("pimpinanList"))
                } else {
                    Toast.makeText(this, "Dokumen profil belum ada. Silakan isi.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Gagal memuat profil: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun saveBapendaProfile() {
        val visi = etVisi.text.toString().trim()
        val misi = etMisi.text.toString().trim()
        val strukturOrganisasi = etStrukturOrganisasi.text.toString().trim()
        val tujuanDanFungsi = etTujuanDanFungsi.text.toString().trim()
        val pimpinanListString = etPimpinanList.text.toString().trim()

        if (visi.isEmpty() || misi.isEmpty() || strukturOrganisasi.isEmpty() || tujuanDanFungsi.isEmpty() || pimpinanListString.isEmpty()) {
            Toast.makeText(this, "Mohon lengkapi semua bidang profil.", Toast.LENGTH_SHORT).show()
            return
        }

        val profileData = hashMapOf(
            "visi" to visi,
            "misi" to misi,
            "strukturOrganisasi" to strukturOrganisasi,
            "tujuanDanFungsi" to tujuanDanFungsi,
            "pimpinanList" to pimpinanListString
        )

        db.collection("bapenda_profile").document("currentProfile")
            .set(profileData)
            .addOnSuccessListener {
                Toast.makeText(this, "Profil Bapenda berhasil diperbarui!", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Gagal memperbarui profil: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}