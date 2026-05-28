package com.nurlana.kasir.cabang

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
import com.nurlana.kasir.model.ModelCabang
import java.text.SimpleDateFormat
import java.util.*

class ModCabangActivity : AppCompatActivity() {

    private lateinit var ivKembali: ImageView
    private lateinit var tvJudul: TextView
    private lateinit var etNama: TextInputEditText
    private lateinit var etNomorTelepon: TextInputEditText
    private lateinit var etAlamat: TextInputEditText
    private lateinit var actvStatus: AutoCompleteTextView
    private lateinit var btnSimpan: MaterialButton
    private lateinit var btnHapus: MaterialButton

    private val cabangRef by lazy {
        FirebaseDatabase.getInstance().getReference("cabang")
    }

    private var cabangEdit: ModelCabang? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mod_cabang)

        initViews()
        setupDropdown()
        checkEditMode()
        setupListeners()
    }

    private fun initViews() {
        ivKembali = findViewById(R.id.ivKembali)
        tvJudul = findViewById(R.id.tvJudul)
        etNama = findViewById(R.id.etNama)
        etNomorTelepon = findViewById(R.id.etNomorTelepon)
        etAlamat = findViewById(R.id.etAlamat)
        actvStatus = findViewById(R.id.actvStatus)
        btnSimpan = findViewById(R.id.btnSimpan)
        btnHapus = findViewById(R.id.btnHapus)
    }

    private fun setupDropdown() {
        val statusList = listOf("Aktif", "Nonaktif")
        actvStatus.setAdapter(
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, statusList)
        )
    }

    private fun checkEditMode() {
        cabangEdit = intent.getParcelableExtra("cabang")
        if (cabangEdit != null) {
            tvJudul.text = "Edit Cabang"
            btnSimpan.text = "Update"
            btnHapus.visibility = View.VISIBLE
            etNama.setText(cabangEdit?.namaCabang)
            etNomorTelepon.setText(cabangEdit?.nomorTelepon)
            etAlamat.setText(cabangEdit?.alamat)
            actvStatus.setText(cabangEdit?.statusCabang, false)
        }
    }

    private fun setupListeners() {
        ivKembali.setOnClickListener { finish() }

        btnSimpan.setOnClickListener {
            val nama = etNama.text.toString().trim()
            val telepon = etNomorTelepon.text.toString().trim()
            val alamat = etAlamat.text.toString().trim()
            val status = actvStatus.text.toString().trim()

            if (nama.isEmpty()) {
                etNama.error = "Nama tidak boleh kosong"
                return@setOnClickListener
            }

            if (cabangEdit != null) {
                updateCabang(nama, telepon, alamat, status)
            } else {
                simpanCabang(nama, telepon, alamat, status)
            }
        }

        btnHapus.setOnClickListener {
            cabangEdit?.idCabang?.let { id ->
                cabangRef.child(id).removeValue()
                    .addOnSuccessListener {
                        Toast.makeText(this, "Cabang berhasil dihapus", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Gagal menghapus", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    private fun simpanCabang(nama: String, telepon: String, alamat: String, status: String) {
        val id = cabangRef.push().key ?: return
        val now = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
        val cabang = ModelCabang(
            idCabang = id,
            namaCabang = nama,
            nomorTelepon = telepon,
            alamat = alamat,
            statusCabang = status,
            createdAt = now
        )
        cabangRef.child(id).setValue(cabang)
            .addOnSuccessListener {
                Toast.makeText(this, "Cabang berhasil disimpan", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Gagal menyimpan", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateCabang(nama: String, telepon: String, alamat: String, status: String) {
        val id = cabangEdit?.idCabang ?: return
        val update = mapOf(
            "namaCabang" to nama,
            "nomorTelepon" to telepon,
            "alamat" to alamat,
            "statusCabang" to status
        )
        cabangRef.child(id).updateChildren(update)
            .addOnSuccessListener {
                Toast.makeText(this, "Cabang berhasil diupdate", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Gagal mengupdate", Toast.LENGTH_SHORT).show()
            }
    }
}