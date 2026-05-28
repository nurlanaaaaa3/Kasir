package com.nurlana.kasir.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nurlana.kasir.R
import com.nurlana.kasir.model.ModelCabang

class DetailCabangAdapter(
    private val list: MutableList<ModelCabang>,
    private val onItemClick: (ModelCabang) -> Unit
) : RecyclerView.Adapter<DetailCabangAdapter.ViewHolder>() {

    fun updateData(newList: MutableList<ModelCabang>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cabang, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNama: TextView = itemView.findViewById(R.id.tvNamaCabang)
        val tvTelepon: TextView = itemView.findViewById(R.id.tvNomorTelepon)
        val tvAlamat: TextView = itemView.findViewById(R.id.tvAlamat)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatusCabang)

        fun bind(cabang: ModelCabang) {
            tvNama.text = cabang.namaCabang ?: "-"
            tvTelepon.text = cabang.nomorTelepon ?: "-"
            tvAlamat.text = cabang.alamat ?: "-"
            tvStatus.text = cabang.statusCabang ?: "-"
            if (cabang.statusCabang == "Aktif") {
                tvStatus.setTextColor(itemView.context.getColor(android.R.color.holo_green_dark))
            } else {
                tvStatus.setTextColor(itemView.context.getColor(android.R.color.holo_red_dark))
            }
            itemView.setOnClickListener { onItemClick(cabang) }
        }
    }
}