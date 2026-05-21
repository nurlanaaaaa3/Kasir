package com.nurlana.kasir

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class TransaksiActivity : AppCompatActivity() {

    private lateinit var ivKembali: ImageView
    private lateinit var tvTotalTransaksi: TextView
    private lateinit var tvTotalPendapatan: TextView
    private lateinit var btnTambahTransaksi: MaterialButton
    private lateinit var rvTransaksi: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaksi)

        initViews()
        setupListeners()
    }

    private fun initViews() {
        ivKembali = findViewById(R.id.ivKembali)
        tvTotalTransaksi = findViewById(R.id.tvTotalTransaksi)
        tvTotalPendapatan = findViewById(R.id.tvTotalPendapatan)
        btnTambahTransaksi = findViewById(R.id.btnTambahTransaksi)
        rvTransaksi = findViewById(R.id.rvTransaksi)
    }

    private fun setupListeners() {
        ivKembali.setOnClickListener { finish() }

        btnTambahTransaksi.setOnClickListener {
            Toast.makeText(this, "Tambah Transaksi", Toast.LENGTH_SHORT).show()
        }
    }
}