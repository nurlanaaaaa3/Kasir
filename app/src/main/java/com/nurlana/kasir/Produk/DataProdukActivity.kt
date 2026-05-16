package com.nurlana.kasir.Produk

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import com.nurlana.kasir.R
import com.nurlana.kasir.adapter.DetailProdukAdapter
import com.nurlana.kasir.model.ModelKategori
import com.nurlana.kasir.model.ModelProduk
import com.nurlana.kasir.Produk.ModProdukActivity

class DataProdukActivity : AppCompatActivity() {

    private lateinit var ivKembali: ImageView
    private lateinit var etSearch: EditText
    private lateinit var ivReload: ImageView
    private lateinit var rvProduk: RecyclerView
    private lateinit var fabTambah: FloatingActionButton
    private lateinit var llFilterKategori: LinearLayout

    private lateinit var adapter: DetailProdukAdapter
    private val listProduk = mutableListOf<ModelProduk>()
    private val listProdukAll = mutableListOf<ModelProduk>()
    private val listKategori = mutableListOf<ModelKategori>()

    private val produkRef by lazy {
        FirebaseDatabase.getInstance().getReference("produk")
    }

    private val kategoriRef by lazy {
        FirebaseDatabase.getInstance().getReference("kategori")
    }

    private var filterAktif = "Semua"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_produk)

        initViews()
        setupRecyclerView()
        setupListeners()
        loadKategori()
        loadProduk()
    }

    private fun initViews() {
        ivKembali = findViewById(R.id.ivKembali)
        etSearch = findViewById(R.id.etSearch)
        ivReload = findViewById(R.id.ivReload)
        rvProduk = findViewById(R.id.rvProduk)
        fabTambah = findViewById(R.id.fabTambah)
        llFilterKategori = findViewById(R.id.llFilterKategori)
    }

    private fun setupRecyclerView() {
        adapter = DetailProdukAdapter(listProduk) { produk ->
            val intent = Intent(this, ModProdukActivity::class.java)
            intent.putExtra("mode", "edit")
            intent.putExtra("produk", produk)
            startActivity(intent)
        }

        rvProduk.layoutManager = LinearLayoutManager(this)
        rvProduk.setHasFixedSize(true)
        rvProduk.adapter = adapter
    }

    private fun setupListeners() {
        ivKembali.setOnClickListener { finish() }

        ivReload.setOnClickListener {
            etSearch.setText("")
            filterAktif = "Semua"
            adapter.updateData(listProdukAll)
            setupFilterButtons()
        }

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterData(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        fabTambah.setOnClickListener {
            val intent = Intent(this, ModProdukActivity::class.java)
            intent.putExtra("mode", "tambah")
            startActivity(intent)
        }
    }

    private fun loadKategori() {
        kategoriRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listKategori.clear()
                for (data in snapshot.children) {
                    val kategori = data.getValue(ModelKategori::class.java)
                    if (kategori != null) listKategori.add(kategori)
                }
                setupFilterButtons()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun setupFilterButtons() {
        llFilterKategori.removeAllViews()

        val semuaList = mutableListOf<ModelKategori>()
        semuaList.add(ModelKategori(namaKategori = "Semua"))
        semuaList.addAll(listKategori)

        for (kategori in semuaList) {
            val namaKategori = kategori.namaKategori ?: "Semua"
            val isAktif = namaKategori == filterAktif

            val btn = MaterialButton(this).apply {
                text = namaKategori
                textSize = 13f
                isAllCaps = false
                strokeWidth = 2
                cornerRadius = 50
                setPadding(32, 12, 32, 12)

                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 0, 12, 0)
                }

                if (isAktif) {
                    setBackgroundColor(ContextCompat.getColor(context, R.color.purple_200))
                    setTextColor(ContextCompat.getColor(context, R.color.purple_700))
                    strokeColor = ContextCompat.getColorStateList(context, R.color.purple_700)
                } else {
                    setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
                    setTextColor(ContextCompat.getColor(context, R.color.black))
                    strokeColor = ContextCompat.getColorStateList(context, R.color.purple_200)
                }

                setOnClickListener {
                    filterAktif = namaKategori
                    filterByKategori(filterAktif)
                    setupFilterButtons()
                }
            }

            llFilterKategori.addView(btn)
        }
    }

    private fun loadProduk() {
        produkRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listProdukAll.clear()
                for (data in snapshot.children) {
                    val produk = data.getValue(ModelProduk::class.java)
                    if (produk != null) listProdukAll.add(produk)
                }
                adapter.updateData(listProdukAll)
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun filterByKategori(namaKategori: String) {
        val filtered = if (namaKategori == "Semua") {
            listProdukAll
        } else {
            val idKategoriDipilih = listKategori
                .find { it.namaKategori == namaKategori }
                ?.idKategori
            listProdukAll.filter { it.idKategori == idKategoriDipilih }
        }
        adapter.updateData(filtered.toMutableList())
    }

    private fun filterData(query: String) {
        val filtered = listProdukAll.filter {
            it.namaProduk?.contains(query, ignoreCase = true) == true
        }
        adapter.updateData(filtered.toMutableList())
    }
}