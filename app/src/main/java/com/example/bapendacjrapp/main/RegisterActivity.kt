// File ini akan menjadi halaman Pendaftaran Akun Baru.
// Menggunakan XML layout (activity_register.xml) untuk tampilan UI.
// Mengintegrasikan Firebase Authentication untuk pendaftaran email/password.

package com.example.bapendacjrapp.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bapendacjrapp.R // Perbarui: Import R dari package root
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Inisialisasi Firebase Auth
        auth = Firebase.auth

        // Menginisialisasi komponen UI
        val etEmail = findViewById<EditText>(R.id.etRegisterEmail)
        val etPassword = findViewById<EditText>(R.id.etRegisterPassword)
        val etConfirmPassword = findViewById<EditText>(R.id.etRegisterConfirmPassword)
        val btnRegister = findViewById<Button>(R.id.btnRegisterAccount)
        val ivBack = findViewById<ImageView>(R.id.ivRegisterBack)

        // Listener untuk tombol Daftar Akun
        btnRegister.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val confirmPassword = etConfirmPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Mohon isi semua kolom.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Password tidak cocok.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.length < 6) {
                Toast.makeText(this, "Password minimal 6 karakter.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Lakukan pendaftaran dengan Firebase Authentication
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Pendaftaran berhasil
                        Toast.makeText(this, "Pendaftaran berhasil! Silakan Login.", Toast.LENGTH_LONG).show()
                        finish() // Kembali ke halaman Login (MainActivity)
                    } else {
                        // Pendaftaran gagal
                        Toast.makeText(this, "Pendaftaran gagal: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
        }

        // Listener untuk tombol kembali
        ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed() // Kembali ke Activity sebelumnya
        }
    }
}