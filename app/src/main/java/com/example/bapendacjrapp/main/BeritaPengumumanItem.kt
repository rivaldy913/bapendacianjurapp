// BeritaPengumumanItem.kt
package com.example.bapendacjrapp.main

// Tidak ada import khusus yang diperlukan untuk data class ini,
// kecuali jika Anda menggunakan tipe data yang tidak standar.

data class BeritaPengumumanItem(
    val id: String,
    val imageUrl: Int, // Menggunakan Int untuk resource drawable lokal
    val title: String,
    val date: String,
    val category: String,
    val description: String
)