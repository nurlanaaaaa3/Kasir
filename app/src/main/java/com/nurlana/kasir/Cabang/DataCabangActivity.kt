package com.nurlana.kasir.cabang

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
import com.nurlana.kasir.adapter.DetailCabangAdapter
import com.nurlana.kasir.model.ModelCabang

class DataCabangActivity : AppCompatActivity() {

    private lateinit var ivKembali: ImageView
    private lateinit var ivReload: ImageView
    private lateinit var etSearch: EditText
    private lateinit var rvCabang: RecyclerView
    private lateinit var fabTambah: FloatingActionButton

    private lateinit var adapter: DetailCabangAdapter
    private val listCabang = mutableListOf<ModelCabang>()

    private val cabangRef by lazy {
        FirebaseDatabase.getInstance().getReference("cabang")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_cabang)

        initViews()
        setupRecyclerView()
        setupListeners()
        loadData()
    }

    private fun initViews() {
        ivKembali = findViewById(R.id.ivKembali)
        ivReload = findViewById(R.id.ivReload)
        etSearch = findViewById(R.id.etSearch)
        rvCabang = findViewById(R.id.rvCabang)
        fabTambah = findViewById(R.id.fabTambah)
    }

    private fun setupRecyclerView() {
        adapter = DetailCabangAdapter(mutableListOf()) { cabang ->
            val intent = Intent(this, ModCabangActivity::class.java)
            intent.putExtra("cabang", cabang)
            startActivity(intent)
        }
        rvCabang.layoutManager = LinearLayoutManager(this)
        rvCabang.adapter = adapter
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
            startActivity(Intent(this, ModCabangActivity::class.java))
        }
    }

    private fun loadData() {
        cabangRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listCabang.clear()
                for (data in snapshot.children) {
                    val c = data.getValue(ModelCabang::class.java)
                    if (c != null) listCabang.add(c)
                }
                adapter.updateData(mutableListOf<ModelCabang>().apply { addAll(listCabang) })
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun filterData(query: String) {
        val filtered = listCabang.filter {
            it.namaCabang?.contains(query, ignoreCase = true) == true ||
                    it.alamat?.contains(query, ignoreCase = true) == true
        }
        adapter.updateData(filtered.toMutableList())
    }
}