package com.nurlana.kasir.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nurlana.kasir.R
import com.nurlana.kasir.model.ModelPegawai

class DetailPegawaiAdapter(
    private val list: MutableList<ModelPegawai>,
    private val onItemClick: (ModelPegawai) -> Unit
) : RecyclerView.Adapter<DetailPegawaiAdapter.ViewHolder>() {

    fun updateData(newList: MutableList<ModelPegawai>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pegawai, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNama: TextView = itemView.findViewById(R.id.tvNamaPegawai)
        val tvJabatan: TextView = itemView.findViewById(R.id.tvJabatan)
        val tvHP: TextView = itemView.findViewById(R.id.tvNomorHP)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatusPegawai)

        fun bind(pegawai: ModelPegawai) {
            tvNama.text = pegawai.namaPegawai ?: "-"
            tvJabatan.text = pegawai.jabatan ?: "-"
            tvHP.text = pegawai.nomorHP ?: "-"
            tvStatus.text = pegawai.statusPegawai ?: "-"
            if (pegawai.statusPegawai == "Aktif") {
                tvStatus.setTextColor(itemView.context.getColor(android.R.color.holo_green_dark))
            } else {
                tvStatus.setTextColor(itemView.context.getColor(android.R.color.holo_red_dark))
            }
            itemView.setOnClickListener { onItemClick(pegawai) }
        }
    }
}