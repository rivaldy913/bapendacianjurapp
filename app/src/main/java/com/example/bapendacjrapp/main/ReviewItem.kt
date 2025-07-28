// ReviewItem.kt
package com.example.bapendacjrapp.main

data class ReviewItem(
    val id: String,
    val userId: String, // ID pengguna yang memberikan ulasan
    val reviewText: String,
    val timestamp: Long // Menggunakan Long untuk timestamp Firebase
)