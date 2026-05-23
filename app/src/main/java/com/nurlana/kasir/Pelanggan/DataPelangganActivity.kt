package com.nurlana.kasir.pelanggan

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import com.nurlana.kasir.R
import com.nurlana.kasir.adapter.DetailPelangganAdapter
import com.nurlana.kasir.model.ModelPelanggan

class DataPelangganActivity : AppCompatActivity() {

    private lateinit var ivKembali: ImageView
    private lateinit var ivReload: ImageView
    private lateinit var etSearch: EditText
    private lateinit var rvPelanggan: RecyclerView
    private lateinit var fabTambah: FloatingActionButton

    private lateinit var adapter: DetailPelangganAdapter
    private val listPelanggan = mutableListOf<ModelPelanggan>()

    private val pelangganRef by lazy {
        FirebaseDatabase.getInstance().getReference("pelanggan")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_pelanggan)

        initViews()
        setupRecyclerView()
        setupListeners()
        loadData()
    }

    private fun initViews() {
        ivKembali = findViewById(R.id.ivKembali)
        ivReload = findViewById(R.id.ivReload)
        etSearch = findViewById(R.id.etSearch)
        rvPelanggan = findViewById(R.id.rvPelanggan)
        fabTambah = findViewById(R.id.fabTambah)
    }

    private fun setupRecyclerView() {
        adapter = DetailPelangganAdapter(mutableListOf()) { pelanggan ->
            val intent = Intent(this, ModPelangganActivity::class.java)
            intent.putExtra("pelanggan", pelanggan)
            startActivity(intent)
        }
        rvPelanggan.layoutManager = LinearLayoutManager(this)
        rvPelanggan.adapter = adapter
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
            startActivity(Intent(this, ModPelangganActivity::class.java))
        }
    }

    private fun loadData() {
        pelangganRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listPelanggan.clear()
                for (data in snapshot.children) {
                    val p = data.getValue(ModelPelanggan::class.java)
                    if (p != null) listPelanggan.add(p)
                }
                adapter.updateData(mutableListOf<ModelPelanggan>().apply { addAll(listPelanggan) })
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun filterData(query: String) {
        val filtered = listPelanggan.filter {
            it.namaPelanggan?.contains(query, ignoreCase = true) == true ||
                    it.nomorHP?.contains(query, ignoreCase = true) == true
        }
        adapter.updateData(filtered.toMutableList())
    }
}