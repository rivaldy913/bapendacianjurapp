package com.example.bapendacjrapp.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bapendacjrapp.R

class PimpinanAdapter(
    private val items: List<PimpinanItem>,
    private val onItemClick: (PimpinanItem) -> Unit
) : RecyclerView.Adapter<PimpinanAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.itemImage)
        val nameView: TextView = view.findViewById(R.id.itemName)
        val positionView: TextView = view.findViewById(R.id.itemPosition)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pimpinan_bapenda, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.imageView.setImageResource(item.imageUrl)
        holder.nameView.text = item.name
        holder.positionView.text = item.position
        holder.itemView.setOnClickListener { onItemClick(item) }
    }

    override fun getItemCount() = items.size
}
