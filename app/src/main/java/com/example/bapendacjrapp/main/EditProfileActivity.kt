package com.example.bapendacjrapp.main

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bapendacjrapp.MainActivity
import com.example.bapendacjrapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class EditProfileActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var tvCurrentEmail: TextView
    private lateinit var etNewPassword: EditText
    private lateinit var etConfirmNewPassword: EditText
    private lateinit var btnChangePassword: Button
    private lateinit var ivEditProfileBack: ImageView
    private lateinit var btnLogout: Button // Deklarasi tombol logout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        auth = Firebase.auth

        tvCurrentEmail = findViewById(R.id.tvCurrentEmail)
        etNewPassword = findViewById(R.id.etNewPassword)
        etConfirmNewPassword = findViewById(R.id.etConfirmNewPassword)
        btnChangePassword = findViewById(R.id.btnChangePassword)
        ivEditProfileBack = findViewById(R.id.ivEditProfileBack)
        btnLogout = findViewById(R.id.btnLogout) // Inisialisasi tombol logout

        // Tampilkan email pengguna saat ini
        val user = auth.currentUser
        if (user != null) {
            tvCurrentEmail.text = "Email Saat Ini: ${user.email}"
        } else {
            tvCurrentEmail.text = "Email Saat Ini: Tidak Ditemukan"
            Toast.makeText(this, "Tidak ada pengguna yang masuk.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        btnChangePassword.setOnClickListener {
            changePassword()
        }

        ivEditProfileBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Listener untuk tombol Logout
        btnLogout.setOnClickListener {
            performLogout()
        }
    }

    private fun changePassword() {
        val newPassword = etNewPassword.text.toString().trim()
        val confirmNewPassword = etConfirmNewPassword.text.toString().trim()

        if (newPassword.isEmpty() || confirmNewPassword.isEmpty()) {
            Toast.makeText(this, "Mohon isi semua kolom kata sandi.", Toast.LENGTH_SHORT).show()
            return
        }

        if (newPassword != confirmNewPassword) {
            Toast.makeText(this, "Kata sandi baru tidak cocok.", Toast.LENGTH_SHORT).show()
            return
        }

        if (newPassword.length < 6) {
            Toast.makeText(this, "Kata sandi minimal 6 karakter.", Toast.LENGTH_SHORT).show()
            return
        }

        val user = auth.currentUser
        if (user != null) {
            user.updatePassword(newPassword)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Kata sandi berhasil diperbarui!", Toast.LENGTH_LONG).show()
                        etNewPassword.text.clear()
                        etConfirmNewPassword.text.clear()
                        finish() // Kembali ke halaman sebelumnya setelah sukses
                    } else {
                        if (task.exception?.message?.contains("requires recent authentication") == true) {
                            Toast.makeText(this, "Sesi login Anda sudah usang. Mohon login ulang untuk mengubah kata sandi.", Toast.LENGTH_LONG).show()
                            // Arahkan ke halaman login dan bersihkan back stack
                            auth.signOut()
                            val intent = Intent(this, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, "Gagal memperbarui kata sandi: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                }
        }
    }

    private fun performLogout() {
        auth.signOut() // Melakukan logout dari Firebase
        Toast.makeText(this, "Anda telah berhasil logout.", Toast.LENGTH_SHORT).show()

        // Arahkan ke MainActivity (halaman login) dan bersihkan back stack
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish() // Menutup EditProfileActivity
    }
}