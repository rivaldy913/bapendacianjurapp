// --- MainActivity.kt ---
// File ini akan menjadi halaman Login/Daftar awal aplikasi Anda.
// Menggunakan XML layout (activity_main.xml) untuk tampilan UI.

package com.example.bapendacjrapp // Pastikan nama package sesuai dengan proyek Anda

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bapendacjrapp.R // Perbarui: Import R dari package root
import com.example.bapendacjrapp.main.RegisterActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth // Deklarasi variabel untuk Firebase Auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Menghubungkan Activity ini dengan layout XML: activity_main.xml
        setContentView(R.layout.activity_main)

        // Inisialisasi Firebase Auth
        auth = Firebase.auth

        // Menginisialisasi komponen UI dari layout
        val etUsername = findViewById<EditText>(R.id.etUsername) // Input untuk username/email
        val etPassword = findViewById<EditText>(R.id.etPassword) // Input untuk password
        val btnLogin = findViewById<Button>(R.id.btnLogin)       // Tombol Login
        val btnRegister = findViewById<Button>(R.id.btnRegister) // Tombol Daftar

        // Menambahkan listener untuk tombol Login
        btnLogin.setOnClickListener {
            val email = etUsername.text.toString().trim() // Ambil email dan hapus spasi
            val password = etPassword.text.toString().trim() // Ambil password dan hapus spasi

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Mohon isi email dan password.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Lakukan login dengan Firebase Authentication
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Login berhasil
                        Toast.makeText(this, "Login berhasil!", Toast.LENGTH_SHORT).show()
                        // Pindah ke HomeActivity setelah login berhasil
                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                        finish() // Menutup MainActivity agar user tidak bisa kembali ke halaman login
                    } else {
                        // Login gagal
                        Toast.makeText(this, "Login gagal: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
        }

        // Menambahkan listener untuk tombol Daftar
        btnRegister.setOnClickListener {
            // Pindah ke RegisterActivity saat tombol "Daftar Akun Baru" diklik
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    // Optional: Otomatis login jika user sudah pernah login sebelumnya
    // Saya menonaktifkan bagian ini sementara agar aplikasi selalu dimulai dari halaman login.
    // Jika Anda ingin mengaktifkan kembali fitur auto-login, hapus komentar di bawah ini.
    /*
    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // Jika ada user yang sudah login, langsung pindah ke HomeActivity
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish() // Menutup MainActivity
        }
    }
    */
}