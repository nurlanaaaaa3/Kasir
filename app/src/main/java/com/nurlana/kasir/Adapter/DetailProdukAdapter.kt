package com.nurlana.kasir.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.nurlana.kasir.R
import com.nurlana.kasir.model.ModelProduk
import java.text.NumberFormat
import java.util.Locale

class DetailProdukAdapter(
    private val produkList: MutableList<ModelProduk>,
    private val onItemClick: (ModelProduk) -> Unit
) : RecyclerView.Adapter<DetailProdukAdapter.ProdukViewHolder>() {

    fun updateData(newList: MutableList<ModelProduk>) {
        produkList.clear()
        produkList.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProdukViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_data_produk, parent, false)
        return ProdukViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProdukViewHolder, position: Int) {
        holder.bind(produkList[position])
    }

    override fun getItemCount(): Int = produkList.size

    inner class ProdukViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgProduk: ImageView = itemView.findViewById(R.id.img_produk)
        val tvNama: TextView = itemView.findViewById(R.id.tv_nama_produk)
        val tvHarga: TextView = itemView.findViewById(R.id.tv_harga_produk)
        val chipStatus: Chip = itemView.findViewById(R.id.chip_status)

        fun bind(produk: ModelProduk) {
            tvNama.text = produk.namaProduk

            val harga = produk.hargaProduk ?: 0
            val formatRupiah = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
            tvHarga.text = formatRupiah.format(harga)

            val status = produk.statusProduk ?: ""
            if (status.equals("Aktif", ignoreCase = true) || status == "1") {
                chipStatus.text = "Aktif"
                chipStatus.chipStrokeColor = ColorStateList.valueOf(
                    ContextCompat.getColor(itemView.context, R.color.status_active_text)
                )
                chipStatus.setTextColor(
                    ContextCompat.getColor(itemView.context, R.color.status_active_text)
                )
            } else {
                chipStatus.text = "Nonaktif"
                chipStatus.chipStrokeColor = ColorStateList.valueOf(
                    ContextCompat.getColor(itemView.context, R.color.status_inactive_text)
                )
                chipStatus.setTextColor(
                    ContextCompat.getColor(itemView.context, R.color.status_inactive_text)
                )
            }
            if (!produk.fotoProduk.isNullOrEmpty()) {
                val resId = itemView.context.resources.getIdentifier(
                    produk.fotoProduk, "drawable", itemView.context.packageName
                )
                if (resId != 0) {
                    imgProduk.setImageResource(resId)
                } else {
                    imgProduk.setImageResource(android.R.color.darker_gray)
                }
            } else {
                imgProduk.setImageResource(android.R.color.darker_gray)
            }
            itemView.setOnClickListener { onItemClick(produk) }
        }
    }
}