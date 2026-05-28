package com.nurlana.kasir.adapter

import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.nurlana.kasir.R
import com.nurlana.kasir.model.ModelKategori

class DetailKategoriAdapter(
    private val kategoriList: MutableList<ModelKategori>,
    private val onItemClick: (ModelKategori) -> Unit
) : RecyclerView.Adapter<DetailKategoriAdapter.KategoriViewHolder>() {

    fun updateData(newList: MutableList<ModelKategori>) {
        kategoriList.clear()
        kategoriList.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KategoriViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_data_kategori, parent, false)
        return KategoriViewHolder(view)
    }

    override fun onBindViewHolder(holder: KategoriViewHolder, position: Int) {
        holder.bind(kategoriList[position])
    }

    override fun getItemCount(): Int = kategoriList.size

    inner class KategoriViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNamaKategori: TextView = itemView.findViewById(R.id.tvNamaKategori)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)

        fun bind(kategori: ModelKategori) {
            tvNamaKategori.text = kategori.namaKategori

            val status = kategori.statusKategori ?: ""

            if (status.equals("Aktif", ignoreCase = true) || status == "1") {
                tvStatus.text = itemView.context.getString(R.string.status_aktif)
                tvStatus.setTextColor(
                    ContextCompat.getColor(itemView.context, R.color.status_aktif_text)
                )
                tvStatus.background = ContextCompat.getDrawable(
                    itemView.context, R.drawable.bg_status_aktif
                )
            } else {
                tvStatus.text = itemView.context.getString(R.string.status_nonatif)
                tvStatus.setTextColor(
                    ContextCompat.getColor(itemView.context, R.color.status_nonaktif_text)
                )
                tvStatus.background = ContextCompat.getDrawable(
                    itemView.context, R.drawable.bg_status_nonaktif
                )
            }

            itemView.setOnClickListener {
                onItemClick(kategori)
            }
        }
    }
}