package com.nurlana.kasir.pelanggan

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.FirebaseDatabase
import com.nurlana.kasir.R
import com.nurlana.kasir.model.ModelPelanggan
import java.text.SimpleDateFormat
import java.util.*

class ModPelangganActivity : AppCompatActivity() {

    private lateinit var ivKembali: ImageView
    private lateinit var tvJudul: TextView
    private lateinit var etNama: TextInputEditText
    private lateinit var etNomorHP: TextInputEditText
    private lateinit var etAlamat: TextInputEditText
    private lateinit var btnSimpan: MaterialButton
    private lateinit var btnHapus: MaterialButton

    private val pelangganRef by lazy {
        FirebaseDatabase.getInstance().getReference("pelanggan")
    }

    private var pelangganEdit: ModelPelanggan? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mod_pelanggan)

        initViews()
        checkEditMode()
        setupListeners()
    }

    private fun initViews() {
        ivKembali = findViewById(R.id.ivKembali)
        tvJudul = findViewById(R.id.tvJudul)
        etNama = findViewById(R.id.etNama)
        etNomorHP = findViewById(R.id.etNomorHP)
        etAlamat = findViewById(R.id.etAlamat)
        btnSimpan = findViewById(R.id.btnSimpan)
        btnHapus = findViewById(R.id.btnHapus)
    }

    private fun checkEditMode() {
        pelangganEdit = intent.getParcelableExtra("pelanggan")
        if (pelangganEdit != null) {
            tvJudul.text = "Edit Pelanggan"
            btnSimpan.text = "Update"
            btnHapus.visibility = View.VISIBLE
            etNama.setText(pelangganEdit?.namaPelanggan)
            etNomorHP.setText(pelangganEdit?.nomorHP)
            etAlamat.setText(pelangganEdit?.alamat)
        }
    }

    private fun setupListeners() {
        ivKembali.setOnClickListener { finish() }

        btnSimpan.setOnClickListener {
            val nama = etNama.text.toString().trim()
            val hp = etNomorHP.text.toString().trim()
            val alamat = etAlamat.text.toString().trim()

            if (nama.isEmpty()) {
                etNama.error = "Nama tidak boleh kosong"
                return@setOnClickListener
            }

            if (pelangganEdit != null) {
                updatePelanggan(nama, hp, alamat)
            } else {
                simpanPelanggan(nama, hp, alamat)
            }
        }

        btnHapus.setOnClickListener {
            pelangganEdit?.idPelanggan?.let { id ->
                pelangganRef.child(id).removeValue()
                    .addOnSuccessListener {
                        Toast.makeText(this, "Pelanggan berhasil dihapus", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Gagal menghapus", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    private fun simpanPelanggan(nama: String, hp: String, alamat: String) {
        val id = pelangganRef.push().key ?: return
        val now = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
        val pelanggan = ModelPelanggan(
            idPelanggan = id,
            namaPelanggan = nama,
            nomorHP = hp,
            alamat = alamat,
            createdAt = now
        )
        pelangganRef.child(id).setValue(pelanggan)
            .addOnSuccessListener {
                Toast.makeText(this, "Pelanggan berhasil disimpan", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Gagal menyimpan", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updatePelanggan(nama: String, hp: String, alamat: String) {
        val id = pelangganEdit?.idPelanggan ?: return
        val update = mapOf(
            "namaPelanggan" to nama,
            "nomorHP" to hp,
            "alamat" to alamat
        )
        pelangganRef.child(id).updateChildren(update)
            .addOnSuccessListener {
                Toast.makeText(this, "Pelanggan berhasil diupdate", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Gagal mengupdate", Toast.LENGTH_SHORT).show()
            }
    }
}