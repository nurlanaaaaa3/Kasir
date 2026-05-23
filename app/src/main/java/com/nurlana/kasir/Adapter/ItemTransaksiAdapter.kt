package com.nurlana.kasir.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nurlana.kasir.R
import com.nurlana.kasir.model.ItemTransaksi
import java.text.NumberFormat
import java.util.Locale

class ItemTransaksiAdapter(
    private val items: MutableList<ItemTransaksi>,
    private val onHapus: (Int) -> Unit
) : RecyclerView.Adapter<ItemTransaksiAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaksi, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], position)
    }

    override fun getItemCount() = items.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNama: TextView = itemView.findViewById(R.id.tvNamaProduk)
        val tvHarga: TextView = itemView.findViewById(R.id.tvHargaSatuan)
        val tvSubtotal: TextView = itemView.findViewById(R.id.tvSubtotal)
        val ivHapus: ImageView = itemView.findViewById(R.id.ivHapus)

        fun bind(item: ItemTransaksi, position: Int) {
            val format = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
            tvNama.text = item.namaProduk
            tvHarga.text = "${item.jumlah} x ${format.format(item.harga)}"
            tvSubtotal.text = format.format(item.subtotal)
            ivHapus.setOnClickListener { onHapus(position) }
        }
    }
}