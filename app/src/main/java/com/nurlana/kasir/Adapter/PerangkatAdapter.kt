package com.nurlana.kasir.adapter

import android.bluetooth.BluetoothDevice
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.nurlana.kasir.R

class PerangkatAdapter(
    private val list: MutableList<BluetoothDevice>,
    private val onHubungkan: (BluetoothDevice) -> Unit
) : RecyclerView.Adapter<PerangkatAdapter.ViewHolder>() {

    fun updateData(newList: List<BluetoothDevice>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_perangkat, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNama: TextView = itemView.findViewById(R.id.tvNamaPerangkat)
        val tvAlamat: TextView = itemView.findViewById(R.id.tvAlamatPerangkat)
        val btnHubungkan: MaterialButton = itemView.findViewById(R.id.btnHubungkan)

        fun bind(device: BluetoothDevice) {
            tvNama.text = device.name ?: "Unknown"
            tvAlamat.text = device.address
            btnHubungkan.setOnClickListener { onHubungkan(device) }
        }
    }
}