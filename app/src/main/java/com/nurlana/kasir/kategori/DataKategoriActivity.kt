package com.nurlana.kasir.kategori

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.nurlana.kasir.R
import com.nurlana.kasir.adapter.DetailKategoriAdapter
import com.nurlana.kasir.model.ModelKategori

class DataKategoriActivity : AppCompatActivity() {

    private lateinit var ivKembali: ImageView
    private lateinit var ivSearch: ImageView
    private lateinit var etSearch: EditText
    private lateinit var ivReload: ImageView
    private lateinit var rvKategori: RecyclerView
    private lateinit var fabTambah: FloatingActionButton

    private lateinit var adapter: DetailKategoriAdapter
    private val listKategori: MutableList<ModelKategori> = mutableListOf()

    private val myRef: DatabaseReference = FirebaseDatabase.getInstance(
        "https://kasir-9cf50-329e6-default-rtdb.asia-southeast1.firebasedatabase.app"
    ).getReference("kategori")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_kategori)

        initViews()
        setupRecyclerView()
        setupListeners()
        loadData()
    }

    private fun initViews() {
        ivKembali  = findViewById(R.id.ivKembali)
        ivSearch   = findViewById(R.id.ivSearch)
        etSearch   = findViewById(R.id.etSearch)
        ivReload   = findViewById(R.id.ivReload)
        rvKategori = findViewById(R.id.rvKategori)
        fabTambah  = findViewById(R.id.fabTambah)
    }

    private fun setupRecyclerView() {
        // ✅ Inisialisasi adapter dengan list kosong dulu
        adapter = DetailKategoriAdapter(mutableListOf()) { kategori ->
            val intent = Intent(this, ModKategoriActivity::class.java).apply {
                putExtra("mode", "edit")
                putExtra("idKategori", kategori.idKategori)
                putExtra("namaKategori", kategori.namaKategori)
                putExtra("statusKategori", kategori.statusKategori)
            }
            startActivity(intent)
        }
        rvKategori.layoutManager = LinearLayoutManager(this)
        rvKategori.adapter = adapter
    }

    private fun setupListeners() {
        ivKembali.setOnClickListener { finish() }

        ivReload.setOnClickListener {
            etSearch.setText("")
            loadData()
        }

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterData(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        fabTambah.setOnClickListener {
            val intent = Intent(this, ModKategoriActivity::class.java).apply {
                putExtra("mode", "tambah")
            }
            startActivity(intent)
        }
    }

    private fun loadData() {
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("FIREBASE", "exists: ${snapshot.exists()}, count: ${snapshot.childrenCount}")
                listKategori.clear()
                for (data in snapshot.children) {
                    val kategori = data.getValue(ModelKategori::class.java)
                    if (kategori != null) {
                        listKategori.add(kategori)
                    }
                }
                // ✅ Kirim copy baru, bukan reference yang sama
                adapter.updateData(mutableListOf<ModelKategori>().apply { addAll(listKategori) })
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FIREBASE", "error: ${error.message}")
            }
        })
    }

    private fun filterData(query: String) {
        val filtered = listKategori.filter {
            it.namaKategori?.contains(query, ignoreCase = true) == true ||
                    it.statusKategori?.contains(query, ignoreCase = true) == true
        }
        // ✅ Kirim copy baru
        adapter.updateData(mutableListOf<ModelKategori>().apply { addAll(filtered) })
    }

    override fun onResume() {
        super.onResume()
    }
}