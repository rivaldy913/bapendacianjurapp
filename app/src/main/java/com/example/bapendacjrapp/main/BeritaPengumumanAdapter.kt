package com.example.bapendacjrapp.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bapendacjrapp.R
import com.example.bapendacjrapp.main.ArtikelAdapter.ViewHolder

class BeritaPengumumanAdapter(
    private val items: List<BeritaPengumumanItem>,
    private val onItemClick: (BeritaPengumumanItem) -> Unit
) : RecyclerView.Adapter<BeritaPengumumanAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.itemImage)
        val titleView: TextView = view.findViewById(R.id.itemTitle)
        val dateCategoryView: TextView = view.findViewById(R.id.itemDateCategory)
        val descriptionView: TextView = view.findViewById(R.id.itemDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_berita_pengumuman, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.imageView.setImageResource(item.imageUrl)
        holder.titleView.text = item.title
        holder.dateCategoryView.text = "${item.date} | ${item.category}"
        holder.descriptionView.text = item.description
        holder.itemView.setOnClickListener { onItemClick(item) }
    }

    override fun getItemCount() = items.size
}