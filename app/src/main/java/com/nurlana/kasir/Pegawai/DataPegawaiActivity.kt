package com.nurlana.kasir.pegawai

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
import com.nurlana.kasir.adapter.DetailPegawaiAdapter
import com.nurlana.kasir.model.ModelPegawai

class DataPegawaiActivity : AppCompatActivity() {

    private lateinit var ivKembali: ImageView
    private lateinit var ivReload: ImageView
    private lateinit var etSearch: EditText
    private lateinit var rvPegawai: RecyclerView
    private lateinit var fabTambah: FloatingActionButton

    private lateinit var adapter: DetailPegawaiAdapter
    private val listPegawai = mutableListOf<ModelPegawai>()

    private val pegawaiRef by lazy {
        FirebaseDatabase.getInstance().getReference("pegawai")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_pegawai)

        initViews()
        setupRecyclerView()
        setupListeners()
        loadData()
    }

    private fun initViews() {
        ivKembali = findViewById(R.id.ivKembali)
        ivReload = findViewById(R.id.ivReload)
        etSearch = findViewById(R.id.etSearch)
        rvPegawai = findViewById(R.id.rvPegawai)
        fabTambah = findViewById(R.id.fabTambah)
    }

    private fun setupRecyclerView() {
        adapter = DetailPegawaiAdapter(mutableListOf()) { pegawai ->
            val intent = Intent(this, ModPegawaiActivity::class.java)
            intent.putExtra("pegawai", pegawai)
            startActivity(intent)
        }
        rvPegawai.layoutManager = LinearLayoutManager(this)
        rvPegawai.adapter = adapter
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
            startActivity(Intent(this, ModPegawaiActivity::class.java))
        }
    }

    private fun loadData() {
        pegawaiRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listPegawai.clear()
                for (data in snapshot.children) {
                    val p = data.getValue(ModelPegawai::class.java)
                    if (p != null) listPegawai.add(p)
                }
                adapter.updateData(mutableListOf<ModelPegawai>().apply { addAll(listPegawai) })
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun filterData(query: String) {
        val filtered = listPegawai.filter {
            it.namaPegawai?.contains(query, ignoreCase = true) == true ||
                    it.jabatan?.contains(query, ignoreCase = true) == true
        }
        adapter.updateData(filtered.toMutableList())
    }
}