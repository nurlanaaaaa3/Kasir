package com.nurlana.kasir.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nurlana.kasir.R
import com.nurlana.kasir.model.ModelTransaksi
import java.text.NumberFormat
import java.util.Locale

class LaporanAdapter(
    private val list: MutableList<ModelTransaksi>,
    private val onItemClick: (ModelTransaksi) -> Unit
) : RecyclerView.Adapter<LaporanAdapter.ViewHolder>() {

    fun updateData(newList: MutableList<ModelTransaksi>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_laporan, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNomor: TextView = itemView.findViewById(R.id.tvNomorTransaksi)
        val tvTotal: TextView = itemView.findViewById(R.id.tvTotal)
        val tvTanggal: TextView = itemView.findViewById(R.id.tvTanggal)
        val tvPelanggan: TextView = itemView.findViewById(R.id.tvNamaPelanggan)

        fun bind(transaksi: ModelTransaksi) {
            val format = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
            tvNomor.text = transaksi.nomorTransaksi ?: "-"
            tvTotal.text = format.format(transaksi.total ?: 0)
            tvTanggal.text = "${transaksi.tanggal} ${transaksi.jam}"
            tvPelanggan.text = transaksi.namaPelanggan ?: "-"
            itemView.setOnClickListener { onItemClick(transaksi) }
        }
    }
}