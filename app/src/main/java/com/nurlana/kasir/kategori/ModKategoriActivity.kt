package com.nurlana.kasir.kategori

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.nurlana.kasir.model.ModelKategori
import com.nurlana.kasir.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.FirebaseDatabase

class ModKategoriActivity : AppCompatActivity() {

    private lateinit var etNamaKategori: TextInputEditText
    private lateinit var spinnerJenis: AutoCompleteTextView
    private lateinit var btnSimpan: MaterialButton
    private lateinit var btnBack: ImageView
    private lateinit var btnHapus: MaterialButton

    private val kategoriRef = FirebaseDatabase.getInstance().getReference("kategori")

    private var idKategori: String? = null
    private var isEditMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mod_kategori)

        initView()
        setupDropdown()
        checkEditMode()
        setupClickListener()
    }

    private fun initView() {
        etNamaKategori = findViewById(R.id.etNamaKategori)
        spinnerJenis = findViewById(R.id.spinnerJenis)
        btnSimpan = findViewById(R.id.btnSimpan)
        btnBack = findViewById(R.id.btnBack)
        btnHapus = findViewById(R.id.btnHapus)
    }

    private fun setupDropdown() {
        val statusList = listOf("Aktif", "Nonaktif")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, statusList)
        spinnerJenis.setAdapter(adapter)
    }

    private fun checkEditMode() {
        idKategori = intent.getStringExtra("idKategori")

        if (!idKategori.isNullOrEmpty()) {
            isEditMode = true
            etNamaKategori.setText(intent.getStringExtra("namaKategori") ?: "")
            spinnerJenis.setText(intent.getStringExtra("statusKategori") ?: "", false)
            btnSimpan.text = "Update"
            btnHapus.visibility = View.VISIBLE
        } else {
            btnHapus.visibility = View.GONE
        }
    }

    private fun setupClickListener() {
        btnBack.setOnClickListener { finish() }

        btnSimpan.setOnClickListener {
            val nama = etNamaKategori.text?.toString()?.trim() ?: ""
            val status = spinnerJenis.text?.toString()?.trim() ?: ""

            if (nama.isEmpty()) {
                etNamaKategori.error = "Nama kategori tidak boleh kosong"
                return@setOnClickListener
            }
            if (status.isEmpty()) {
                Toast.makeText(this, "Pilih status kategori", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (isEditMode) updateKategori(nama, status) else simpanKategori(nama, status)
        }

        btnHapus.setOnClickListener {
            idKategori?.let { id ->
                kategoriRef.child(id).removeValue()
                    .addOnSuccessListener {
                        Toast.makeText(this, "Kategori berhasil dihapus", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Gagal menghapus", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    private fun simpanKategori(nama: String, status: String) {
        val id = kategoriRef.push().key ?: return
        val data = ModelKategori(idKategori = id, namaKategori = nama, statusKategori = status)
        kategoriRef.child(id).setValue(data)
            .addOnSuccessListener {
                Toast.makeText(this, "Kategori berhasil disimpan", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateKategori(nama: String, status: String) {
        val id = idKategori ?: return
        val updates = mapOf("namaKategori" to nama, "statusKategori" to status)
        kategoriRef.child(id).updateChildren(updates)
            .addOnSuccessListener {
                Toast.makeText(this, "Kategori berhasil diupdate", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Gagal mengupdate data", Toast.LENGTH_SHORT).show()
            }
    }
}