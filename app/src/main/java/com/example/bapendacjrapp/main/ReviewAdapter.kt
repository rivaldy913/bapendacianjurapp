// ReviewAdapter.kt
package com.example.bapendacjrapp.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bapendacjrapp.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ReviewAdapter(private val items: List<ReviewItem>) :
    RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val reviewerIdView: TextView = view.findViewById(R.id.tvReviewerId)
        val reviewContentView: TextView = view.findViewById(R.id.tvReviewContent)
        val reviewDateView: TextView = view.findViewById(R.id.tvReviewDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_review, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        // Anda mungkin ingin menampilkan nama pengguna daripada ID di masa depan
        holder.reviewerIdView.text = "Pengguna (ID: ${item.userId.substring(0, 4)}...)" // Menampilkan sebagian ID
        holder.reviewContentView.text = item.reviewText

        // Format timestamp menjadi tanggal yang mudah dibaca
        val dateFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("id", "ID"))
        holder.reviewDateView.text = dateFormat.format(Date(item.timestamp))
    }

    override fun getItemCount() = items.size
}