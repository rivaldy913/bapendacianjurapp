package com.example.bapendacjrapp.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bapendacjrapp.R
import com.example.bapendacjrapp.main.LayananItem // Menggunakan LayananItem

class AdminServiceAdapter(
    private val items: List<LayananItem>,
    private val onEditClick: (LayananItem) -> Unit,
    private val onDeleteClick: (LayananItem) -> Unit
) : RecyclerView.Adapter<AdminServiceAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.itemImage) // Akan digunakan untuk ikon layanan
        val titleView: TextView = view.findViewById(R.id.itemTitle)
        val dateCategoryView: TextView = view.findViewById(R.id.itemDateCategory) // Akan digunakan untuk deskripsi singkat
        val descriptionView: TextView = view.findViewById(R.id.itemDescription) // Akan digunakan untuk deskripsi lebih detail
        val btnEdit: Button = view.findViewById(R.id.btnEdit)
        val btnDelete: Button = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_admin_news, parent, false) // Menggunakan layout yang sama dengan item_admin_news
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        // Mengatur ikon layanan
        holder.imageView.setImageResource(item.iconResId)
        // Mungkin perlu menyesuaikan tint ikon jika item_admin_news memiliki tint default
        holder.imageView.setColorFilter(holder.itemView.context.resources.getColor(R.color.primaryBlue, null))


        holder.titleView.text = item.title
        // Gunakan dateCategoryView untuk menampilkan bagian deskripsi singkat dari LayananItem
        // Karena item_admin_news menggunakan dateCategoryView, kita bisa petakan LayananItem.description di sini
        holder.dateCategoryView.text = item.description

        // Untuk deskripsi lebih detail, gunakan descriptionView. Karena item_admin_news mungkin membatasi baris
        // pertimbangkan untuk menampilkan sebagian atau semua deskripsi, atau menyesuaikan visibilitas
        holder.descriptionView.text = item.description // Ini bisa jadi redundant atau perlu penyesuaian jika item_admin_news.xml hanya menunjukkan 3 baris
        holder.descriptionView.maxLines = 3 // Atau sesuaikan sesuai kebutuhan

        holder.btnEdit.setOnClickListener { onEditClick(item) }
        holder.btnDelete.setOnClickListener { onDeleteClick(item) }
    }

    override fun getItemCount() = items.size
}