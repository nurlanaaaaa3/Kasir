package com.nurlana.kasir.pegawai

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.FirebaseDatabase
import com.nurlana.kasir.R
import com.nurlana.kasir.model.ModelPegawai
import java.text.SimpleDateFormat
import java.util.*

class ModPegawaiActivity : AppCompatActivity() {

    private lateinit var ivKembali: ImageView
    private lateinit var tvJudul: TextView
    private lateinit var etNama: TextInputEditText
    private lateinit var etNomorHP: TextInputEditText
    private lateinit var actvJabatan: AutoCompleteTextView
    private lateinit var etEmail: TextInputEditText
    private lateinit var etTanggalMasuk: TextInputEditText
    private lateinit var etAlamat: TextInputEditText
    private lateinit var actvStatus: AutoCompleteTextView
    private lateinit var btnSimpan: MaterialButton
    private lateinit var btnHapus: MaterialButton

    private val pegawaiRef by lazy {
        FirebaseDatabase.getInstance().getReference("pegawai")
    }

    private var pegawaiEdit: ModelPegawai? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mod_pegawai)

        initViews()
        setupDropdown()
        checkEditMode()
        setupListeners()
    }

    private fun initViews() {
        ivKembali = findViewById(R.id.ivKembali)
        tvJudul = findViewById(R.id.tvJudul)
        etNama = findViewById(R.id.etNama)
        etNomorHP = findViewById(R.id.etNomorHP)
        actvJabatan = findViewById(R.id.actvJabatan)
        etEmail = findViewById(R.id.etEmail)
        etTanggalMasuk = findViewById(R.id.etTanggalMasuk)
        etAlamat = findViewById(R.id.etAlamat)
        actvStatus = findViewById(R.id.actvStatus)
        btnSimpan = findViewById(R.id.btnSimpan)
        btnHapus = findViewById(R.id.btnHapus)
    }

    private fun setupDropdown() {
        val jabatanList = listOf("Kasir", "Manajer", "Admin", "Teknisi", "Lainnya")
        actvJabatan.setAdapter(
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, jabatanList)
        )

        val statusList = listOf("Aktif", "Nonaktif")
        actvStatus.setAdapter(
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, statusList)
        )
    }

    private fun checkEditMode() {
        pegawaiEdit = intent.getParcelableExtra("pegawai")
        if (pegawaiEdit != null) {
            tvJudul.text = "Edit Pegawai"
            btnSimpan.text = "Update"
            btnHapus.visibility = View.VISIBLE
            etNama.setText(pegawaiEdit?.namaPegawai)
            etNomorHP.setText(pegawaiEdit?.nomorHP)
            actvJabatan.setText(pegawaiEdit?.jabatan, false)
            etEmail.setText(pegawaiEdit?.email)
            etTanggalMasuk.setText(pegawaiEdit?.tanggalMasuk)
            etAlamat.setText(pegawaiEdit?.alamat)
            actvStatus.setText(pegawaiEdit?.statusPegawai, false)
        }
    }

    private fun setupListeners() {
        ivKembali.setOnClickListener { finish() }

        btnSimpan.setOnClickListener {
            val nama = etNama.text.toString().trim()
            val hp = etNomorHP.text.toString().trim()
            val jabatan = actvJabatan.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val tanggal = etTanggalMasuk.text.toString().trim()
            val alamat = etAlamat.text.toString().trim()
            val status = actvStatus.text.toString().trim()

            if (nama.isEmpty()) {
                etNama.error = "Nama tidak boleh kosong"
                return@setOnClickListener
            }

            if (pegawaiEdit != null) {
                updatePegawai(nama, hp, jabatan, email, tanggal, alamat, status)
            } else {
                simpanPegawai(nama, hp, jabatan, email, tanggal, alamat, status)
            }
        }

        btnHapus.setOnClickListener {
            pegawaiEdit?.idPegawai?.let { id ->
                pegawaiRef.child(id).removeValue()
                    .addOnSuccessListener {
                        Toast.makeText(this, "Pegawai berhasil dihapus", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Gagal menghapus", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    private fun simpanPegawai(nama: String, hp: String, jabatan: String, email: String, tanggal: String, alamat: String, status: String) {
        val id = pegawaiRef.push().key ?: return
        val now = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
        val pegawai = ModelPegawai(
            idPegawai = id,
            namaPegawai = nama,
            nomorHP = hp,
            jabatan = jabatan,
            email = email,
            tanggalMasuk = tanggal,
            alamat = alamat,
            statusPegawai = status,
            createdAt = now
        )
        pegawaiRef.child(id).setValue(pegawai)
            .addOnSuccessListener {
                Toast.makeText(this, "Pegawai berhasil disimpan", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Gagal menyimpan", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updatePegawai(nama: String, hp: String, jabatan: String, email: String, tanggal: String, alamat: String, status: String) {
        val id = pegawaiEdit?.idPegawai ?: return
        val update = mapOf(
            "namaPegawai" to nama,
            "nomorHP" to hp,
            "jabatan" to jabatan,
            "email" to email,
            "tanggalMasuk" to tanggal,
            "alamat" to alamat,
            "statusPegawai" to status
        )
        pegawaiRef.child(id).updateChildren(update)
            .addOnSuccessListener {
                Toast.makeText(this, "Pegawai berhasil diupdate", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Gagal mengupdate", Toast.LENGTH_SHORT).show()
            }
    }
}