// LayananItem.kt
package com.example.bapendacjrapp.main

// Tidak ada import khusus yang diperlukan.

data class LayananItem(
    val id: String,
    val iconResId: Int, // Menggunakan Int untuk resource drawable lokal
    val title: String
)