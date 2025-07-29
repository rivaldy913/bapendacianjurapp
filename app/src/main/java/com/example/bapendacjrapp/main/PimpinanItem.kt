package com.example.bapendacjrapp.main

data class PimpinanItem(
    val id: String,
    val imageUrl: Int, // Dikembalikan menjadi Int untuk resource drawable lokal
    val name: String,
    val position: String
)