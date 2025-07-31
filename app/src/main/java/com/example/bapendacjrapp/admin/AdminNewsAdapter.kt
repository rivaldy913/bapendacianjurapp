package com.example.bapendacjrapp.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bapendacjrapp.R
import com.example.bapendacjrapp.main.BeritaPengumumanItem

class AdminNewsAdapter(
    private val items: List<BeritaPengumumanItem>,
    private val onEditClick: (BeritaPengumumanItem) -> Unit,
    private val onDeleteClick: (BeritaPengumumanItem) -> Unit
) : RecyclerView.Adapter<AdminNewsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.itemImage)
        val titleView: TextView = view.findViewById(R.id.itemTitle)
        val dateCategoryView: TextView = view.findViewById(R.id.itemDateCategory)
        val descriptionView: TextView = view.findViewById(R.id.itemDescription)
        val btnEdit: Button = view.findViewById(R.id.btnEdit)
        val btnDelete: Button = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_admin_news, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        val imageResId = holder.itemView.context.resources.getIdentifier(
            item.imageUrl, "drawable", holder.itemView.context.packageName
        )
        if (imageResId != 0) {
            holder.imageView.setImageResource(imageResId)
        } else {
            holder.imageView.setImageResource(R.drawable.placeholder_news_image)
        }

        holder.titleView.text = item.title
        holder.dateCategoryView.text = "${item.date} | ${item.category}"
        holder.descriptionView.text = item.description

        holder.btnEdit.setOnClickListener { onEditClick(item) }
        holder.btnDelete.setOnClickListener { onDeleteClick(item) }
    }

    override fun getItemCount() = items.size
}