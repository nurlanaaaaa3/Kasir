package com.nurlana.kasir.transaksi

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.*
import com.nurlana.kasir.R
import com.nurlana.kasir.adapter.ItemTransaksiAdapter
import com.nurlana.kasir.model.ItemTransaksi
import com.nurlana.kasir.model.ModelProduk
import com.nurlana.kasir.model.ModelTransaksi
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class TransaksiActivity : AppCompatActivity() {

    private lateinit var ivKembali: ImageView
    private lateinit var etNamaPelanggan: TextInputEditText
    private lateinit var actvProduk: AutoCompleteTextView
    private lateinit var etJumlah: TextInputEditText
    private lateinit var btnTambahItem: MaterialButton
    private lateinit var rvItemTransaksi: RecyclerView
    private lateinit var tvTotal: TextView
    private lateinit var etBayar: TextInputEditText
    private lateinit var tvKembali: TextView
    private lateinit var btnProses: MaterialButton

    private val listProduk = mutableListOf<ModelProduk>()
    private val listItem = mutableListOf<ItemTransaksi>()
    private lateinit var adapter: ItemTransaksiAdapter
    private var produkDipilih: ModelProduk? = null
    private var total = 0L

    private val produkRef by lazy {
        FirebaseDatabase.getInstance().getReference("produk")
    }
    private val transaksiRef by lazy {
        FirebaseDatabase.getInstance().getReference("transaksi")
    }

    private val format = NumberFormat.getCurrencyInstance(Locale("id", "ID"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaksi)

        initViews()
        setupRecyclerView()
        loadProduk()
        setupListeners()
    }

    private fun initViews() {
        ivKembali = findViewById(R.id.ivKembali)
        etNamaPelanggan = findViewById(R.id.etNamaPelanggan)
        actvProduk = findViewById(R.id.actvProduk)
        etJumlah = findViewById(R.id.etJumlah)
        btnTambahItem = findViewById(R.id.btnTambahItem)
        rvItemTransaksi = findViewById(R.id.rvItemTransaksi)
        tvTotal = findViewById(R.id.tvTotal)
        etBayar = findViewById(R.id.etBayar)
        tvKembali = findViewById(R.id.tvKembali)
        btnProses = findViewById(R.id.btnProses)
    }

    private fun setupRecyclerView() {
        adapter = ItemTransaksiAdapter(listItem) { position ->
            total -= listItem[position].subtotal ?: 0
            listItem.removeAt(position)
            adapter.notifyDataSetChanged()
            updateTotal()
        }
        rvItemTransaksi.layoutManager = LinearLayoutManager(this)
        rvItemTransaksi.adapter = adapter
    }

    private fun loadProduk() {
        produkRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listProduk.clear()
                for (data in snapshot.children) {
                    val produk = data.getValue(ModelProduk::class.java)
                    if (produk != null) listProduk.add(produk)
                }
                val namaProduk = listProduk.map { it.namaProduk ?: "" }
                val adapterProduk = ArrayAdapter(
                    this@TransaksiActivity,
                    android.R.layout.simple_dropdown_item_1line,
                    namaProduk
                )
                actvProduk.setAdapter(adapterProduk)
                actvProduk.setOnItemClickListener { _, _, position, _ ->
                    produkDipilih = listProduk[position]
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun setupListeners() {
        ivKembali.setOnClickListener { finish() }

        btnTambahItem.setOnClickListener {
            val produk = produkDipilih
            val jumlah = etJumlah.text.toString().trim().toIntOrNull() ?: 0

            if (produk == null) {
                Toast.makeText(this, "Pilih produk dulu", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (jumlah <= 0) {
                Toast.makeText(this, "Jumlah harus lebih dari 0", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val subtotal = (produk.hargaProduk ?: 0) * jumlah
            val item = ItemTransaksi(
                idProduk = produk.idProduk,
                namaProduk = produk.namaProduk,
                harga = produk.hargaProduk?.toLong(),
                jumlah = jumlah,
                subtotal = subtotal.toLong()
            )
            listItem.add(item)
            adapter.notifyDataSetChanged()
            updateTotal()

            actvProduk.setText("")
            etJumlah.setText("")
            produkDipilih = null
        }

        etBayar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val bayar = s.toString().toLongOrNull() ?: 0
                val kembali = bayar - total
                tvKembali.text = format.format(if (kembali < 0) 0 else kembali)
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        btnProses.setOnClickListener { prosesTransaksi() }
    }

    private fun updateTotal() {
        total = listItem.sumOf { it.subtotal ?: 0 }
        tvTotal.text = format.format(total)
    }

    private fun prosesTransaksi() {
        if (listItem.isEmpty()) {
            Toast.makeText(this, "Tambahkan produk dulu", Toast.LENGTH_SHORT).show()
            return
        }
        val bayar = etBayar.text.toString().toLongOrNull() ?: 0
        if (bayar < total) {
            Toast.makeText(this, "Jumlah bayar kurang", Toast.LENGTH_SHORT).show()
            return
        }

        val prefs = getSharedPreferences("kasir_prefs", MODE_PRIVATE)
        val namaKasir = prefs.getString("namaUser", "-") ?: "-"

        val id = transaksiRef.push().key ?: return
        val now = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val jam = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        val nomorTransaksi = "No.${(0..999).random()}"

        val transaksi = ModelTransaksi(
            idTransaksi = id,
            nomorTransaksi = nomorTransaksi,
            tanggal = now,
            jam = jam,
            namaPelanggan = etNamaPelanggan.text.toString().trim(),
            namaKasir = namaKasir,
            items = listItem,
            total = total,
            bayar = bayar,
            kembali = bayar - total,
            createdAt = "$now $jam"
        )

        transaksiRef.child(id).setValue(transaksi)
            .addOnSuccessListener {
                val intent = Intent(this, StrukActivity::class.java)
                intent.putExtra("transaksi", transaksi)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Gagal: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}