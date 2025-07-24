// BeritaPengumumanItem.kt
package com.example.bapendacjrapp.main

data class BeritaPengumumanItem(
    val id: String,
    val imageUrl: String, // HARUS String
    val title: String,
    val date: String,
    val category: String,
    val description: String
)