package com.example.bapendacjrapp.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bapendacjrapp.R

class LayananAdapter(
    private val items: List<LayananItem>,
    private val onItemClick: (LayananItem) -> Unit
) : RecyclerView.Adapter<LayananAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val iconView: ImageView = view.findViewById(R.id.itemIcon)
        val titleView: TextView = view.findViewById(R.id.itemTitle)
        val descriptionView: TextView = view.findViewById(R.id.itemDescription) // Deklarasi TextView deskripsi
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layanan, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.iconView.setImageResource(item.iconResId)
        holder.titleView.text = item.title
        holder.descriptionView.text = item.description // Ikat data deskripsi
        holder.itemView.setOnClickListener { onItemClick(item) }
    }

    override fun getItemCount() = items.size
}