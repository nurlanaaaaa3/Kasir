package com.nurlana.kasir.kategori

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference

class DataKategoriActivity : AppCompatActivity() {

    // Firebase
    private val database = FirebaseDatabase.getInstance()
    private val myRef: DatabaseReference = database.getReference("kategori")

    // Views - Form Input
    private var tvJudulToolbar: TextView? = null
    private var tvJudul: TextView? = null
    private var etNamaKategori: TextInputEditText? = null
    private var etStatusKategori: TextInputLayout? = null
    private var spStatusKategori: MaterialAutoCompleteTextView? = null
    private var btSimpan: MaterialButton? = null

    // Views - RecyclerView
    private var recyclerView: RecyclerView? = null
    private var btnBack: ImageView? = null
    private var btnSearch: ImageView? = null
    private var btnTambah: FloatingActionButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Load layout menggunakan nama resource string
        val layoutId = resources.getIdentifier("activity_data_kategori", "layout", packageName)
        setContentView(layoutId)

        // Inisialisasi view
        initViews()

        // Setup RecyclerView
        setupRecyclerView()

        // Setup listeners
        setupListeners()

        // Load data dari Firebase
        loadDataFromFirebase()
    }

    private fun initViews() {
        try {
            // Inisialisasi form input menggunakan getIdentifier
            tvJudulToolbar = findViewById(getResId("tvJudulToolbar", "id"))
            tvJudul = findViewById(getResId("tvJudul", "id"))
            etNamaKategori = findViewById(getResId("etNamaKategori", "id"))
            etStatusKategori = findViewById(getResId("etStatusKategori", "id"))
            spStatusKategori = findViewById(getResId("spStatusKategori", "id"))
            btSimpan = findViewById(getResId("btSimpan", "id"))

            // Inisialisasi RecyclerView
            recyclerView = findViewById(getResId("recyclerView", "id"))
            btnBack = findViewById(getResId("btnBack", "id"))
            btnSearch = findViewById(getResId("btnSearch", "id"))
            btnTambah = findViewById(getResId("btnTambah", "id"))
        } catch (e: Exception) {
            Toast.makeText(this, "Error inisialisasi: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getResId(resName: String, resType: String): Int {
        return resources.getIdentifier(resName, resType, packageName)
    }

    private fun setupRecyclerView() {
        try {
            recyclerView?.apply {
                layoutManager = GridLayoutManager(this@DataKategoriActivity, 2)
                setHasFixedSize(true)
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Error setup RecyclerView: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupListeners() {
        // Tombol Back
        btnBack?.setOnClickListener {
            finish()
        }

        // Tombol Search
        btnSearch?.setOnClickListener {
            Toast.makeText(this, "Fitur pencarian", Toast.LENGTH_SHORT).show()
        }

        // Floating Action Button Tambah
        btnTambah?.setOnClickListener {
            tambahKategori()
        }

        // Tombol Simpan
        btSimpan?.setOnClickListener {
            simpanKategori()
        }
    }

    private fun loadDataFromFirebase() {
        myRef.get()
            .addOnSuccessListener { dataSnapshot ->
                if (dataSnapshot.exists()) {
                    Toast.makeText(this, "Data berhasil dimuat", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Data kosong", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun tambahKategori() {
        Toast.makeText(this, "Tambah kategori baru", Toast.LENGTH_SHORT).show()
    }

    private fun simpanKategori() {
        // Ambil data dari input
        val namaKategori = etNamaKategori?.text.toString().trim()
        val statusKategori = spStatusKategori?.text.toString().trim()

        // Validasi nama kategori
        if (namaKategori.isEmpty()) {
            etNamaKategori?.error = "Nama kategori tidak boleh kosong"
            etNamaKategori?.requestFocus()
            return
        }

        // Validasi status kategori
        if (statusKategori.isEmpty()) {
            etStatusKategori?.error = "Pilih status kategori"
            spStatusKategori?.requestFocus()
            return
        }

        // Clear error
        etNamaKategori?.error = null
        etStatusKategori?.error = null

        // Generate ID unik
        val kategoriId = myRef.push().key

        if (kategoriId != null) {
            // Buat data kategori
            val kategoriData = hashMapOf(
                "id" to kategoriId,
                "nama" to namaKategori,
                "status" to statusKategori,
                "timestamp" to System.currentTimeMillis()
            )

            // Simpan ke Firebase
            myRef.child(kategoriId).setValue(kategoriData)
                .addOnSuccessListener {
                    Toast.makeText(this, "Kategori berhasil disimpan!", Toast.LENGTH_SHORT).show()

                    // Clear input fields
                    etNamaKategori?.text?.clear()
                    spStatusKategori?.text?.clear()

                    // Reload data
                    loadDataFromFirebase()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Gagal menyimpan: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Error membuat ID kategori", Toast.LENGTH_SHORT).show()
        }
    }
}