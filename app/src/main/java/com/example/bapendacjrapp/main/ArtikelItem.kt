// ArtikelItem.kt
package com.example.bapendacjrapp.main

// Tidak ada import khusus yang diperlukan.

data class ArtikelItem(
    val id: String,
    val imageUrl: Int, // Menggunakan Int untuk resource drawable lokal
    val title: String,
    val date: String,
    val category: String,
    val description: String
)