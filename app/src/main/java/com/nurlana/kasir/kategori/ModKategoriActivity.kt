package com.nurlana.kasir.kategori

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.nurlana.kasir.R

class ModKategoriActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageView
    private lateinit var etNamaKategori: EditText
    private lateinit var spinnerStatus: Spinner
    private lateinit var btnSimpan: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mod_kategori)

        initViews()
        setupSpinner()
        setupClickListeners()
    }

    private fun initViews() {
        btnBack = findViewById(R.id.btnBack)
        etNamaKategori = findViewById(R.id.etNamaKategori)
        spinnerStatus = findViewById(R.id.spinnerStatus)
        btnSimpan = findViewById(R.id.btnSimpan)
    }

    private fun setupSpinner() {
        // Data untuk spinner: Aktif dan Non Aktif
        val statusList = arrayOf("Aktif", "Non Aktif")

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            statusList
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerStatus.adapter = adapter

        // Set default ke "Aktif"
        spinnerStatus.setSelection(0)
    }

    private fun setupClickListeners() {
        btnBack.setOnClickListener {
            finish()
        }

        btnSimpan.setOnClickListener {
            simpanKategori()
        }
    }

    private fun simpanKategori() {
        val namaKategori = etNamaKategori.text.toString().trim()
        val status = spinnerStatus.selectedItem.toString()

        // Validasi input
        if (namaKategori.isEmpty()) {
            Toast.makeText(this, "Nama kategori tidak boleh kosong!", Toast.LENGTH_SHORT).show()
            etNamaKategori.requestFocus()
            return
        }

        // Tampilkan pesan sukses
        Toast.makeText(
            this,
            "Kategori '$namaKategori' dengan status '$status' berhasil disimpan!",
            Toast.LENGTH_SHORT
        ).show()

        // Kembali ke halaman sebelumnya
        finish()
    }
}