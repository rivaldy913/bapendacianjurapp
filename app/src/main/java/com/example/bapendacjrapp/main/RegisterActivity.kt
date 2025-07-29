package com.example.bapendacjrapp.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView // Import TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bapendacjrapp.MainActivity // Import MainActivity
import com.example.bapendacjrapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import java.util.Date

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var tvLoginHere: TextView // Deklarasi TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = Firebase.auth
        db = Firebase.firestore

        val etEmail = findViewById<EditText>(R.id.etRegisterEmail)
        val etPassword = findViewById<EditText>(R.id.etRegisterPassword)
        val etConfirmPassword = findViewById<EditText>(R.id.etRegisterConfirmPassword)
        val btnRegister = findViewById<Button>(R.id.btnRegisterAccount)
        val ivBack = findViewById<ImageView>(R.id.ivRegisterBack)

        // Pastikan inisialisasi ini benar:
        tvLoginHere = findViewById(R.id.tvLoginHere) // Ini baris yang menyebabkan error jika salah nama

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

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val userId = auth.currentUser?.uid
                        if (userId != null) {
                            val user = hashMapOf(
                                "email" to email,
                                "role" to "user",
                                "createdAt" to Date()
                            )
                            db.collection("users").document(userId)
                                .set(user)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "Pendaftaran berhasil! Silakan Login.", Toast.LENGTH_LONG).show()
                                    finish()
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(this, "Pendaftaran berhasil di Auth, tapi gagal menyimpan data user di Firestore: ${e.message}", Toast.LENGTH_LONG).show()
                                    finish()
                                }
                        } else {
                            Toast.makeText(this, "Pendaftaran berhasil, tetapi user ID tidak ditemukan.", Toast.LENGTH_LONG).show()
                            finish()
                        }
                    } else {
                        Toast.makeText(this, "Pendaftaran gagal: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
        }

        ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        tvLoginHere.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}