package com.nurlana.kasir.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nurlana.kasir.R
import com.nurlana.kasir.model.ModelPelanggan

class DetailPelangganAdapter(
    private val list: MutableList<ModelPelanggan>,
    private val onItemClick: (ModelPelanggan) -> Unit
) : RecyclerView.Adapter<DetailPelangganAdapter.ViewHolder>() {

    fun updateData(newList: MutableList<ModelPelanggan>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pelanggan, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNama: TextView = itemView.findViewById(R.id.tvNamaPelanggan)
        val tvHP: TextView = itemView.findViewById(R.id.tvNomorHP)
        val tvAlamat: TextView = itemView.findViewById(R.id.tvAlamat)

        fun bind(pelanggan: ModelPelanggan) {
            tvNama.text = pelanggan.namaPelanggan ?: "-"
            tvHP.text = pelanggan.nomorHP ?: "-"
            tvAlamat.text = pelanggan.alamat ?: "-"
            itemView.setOnClickListener { onItemClick(pelanggan) }
        }
    }
}