package com.nurlana.kasir.Produk

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.*
import com.nurlana.kasir.R
import com.nurlana.kasir.model.ModelKategori
import com.nurlana.kasir.model.ModelProduk
import java.text.SimpleDateFormat
import java.util.*

class ModProdukActivity : AppCompatActivity() {

    private lateinit var ivKembali: ImageView
    private lateinit var tvJudul: TextView
    private lateinit var imgPreview: ImageView
    private lateinit var actvFoto: AutoCompleteTextView
    private lateinit var etNamaProduk: TextInputEditText
    private lateinit var etHarga: TextInputEditText
    private lateinit var etStok: TextInputEditText
    private lateinit var cbTanpaBatas: CheckBox
    private lateinit var actvKategori: AutoCompleteTextView
    private lateinit var actvCabang: AutoCompleteTextView
    private lateinit var actvStatus: AutoCompleteTextView
    private lateinit var btnSimpan: MaterialButton

    private val kategoriRef by lazy { FirebaseDatabase.getInstance().getReference("kategori") }
    private val cabangRef by lazy { FirebaseDatabase.getInstance().getReference("cabang") }
    private val produkRef by lazy { FirebaseDatabase.getInstance().getReference("produk") }

    private val listKategori = mutableListOf<ModelKategori>()
    private val listCabang = mutableListOf<Pair<String, String>>()

    private var mode = "tambah"
    private var produkEdit: ModelProduk? = null
    private var idKategoriDipilih: String? = null
    private var idCabangDipilih: String? = null
    private var fotoTerpilih: String = ""
    private val daftarFoto = listOf(
        "ayamgeprek",
        "frenchfries"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mod_produk)

        mode = intent.getStringExtra("mode") ?: "tambah"
        produkEdit = intent.getParcelableExtra("produk")

        initViews()
        setupDropdownFoto()
        setupDropdownStatus()
        loadKategori()
        loadCabang()

        tvJudul.text = if (mode == "edit") "Edit Produk" else "Tambah Produk"
        if (mode == "edit") isiDataEdit()

        setupListeners()
    }

    private fun initViews() {
        ivKembali = findViewById(R.id.ivKembali)
        tvJudul = findViewById(R.id.tvJudul)
        imgPreview = findViewById(R.id.imgPreview)
        actvFoto = findViewById(R.id.actvFoto)
        etNamaProduk = findViewById(R.id.etNamaProduk)
        etHarga = findViewById(R.id.etHarga)
        etStok = findViewById(R.id.etStok)
        cbTanpaBatas = findViewById(R.id.cbTanpaBatas)
        actvKategori = findViewById(R.id.actvKategori)
        actvCabang = findViewById(R.id.actvCabang)
        actvStatus = findViewById(R.id.actvStatus)
        btnSimpan = findViewById(R.id.btnSimpan)
    }

    private fun setupListeners() {
        ivKembali.setOnClickListener { finish() }

        cbTanpaBatas.setOnCheckedChangeListener { _, isChecked ->
            etStok.isEnabled = !isChecked
            if (isChecked) etStok.setText("")
        }

        btnSimpan.setOnClickListener { simpanProduk() }
    }

    private fun setupDropdownFoto() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, daftarFoto)
        actvFoto.setAdapter(adapter)

        actvFoto.setOnItemClickListener { _, _, position, _ ->
            fotoTerpilih = daftarFoto[position]
            tampilkanPreview(fotoTerpilih)
        }
    }

    private fun tampilkanPreview(namaFoto: String) {
        val resId = resources.getIdentifier(namaFoto, "drawable", packageName)
        if (resId != 0) {
            imgPreview.setImageResource(resId)
        } else {
            imgPreview.setImageResource(android.R.color.darker_gray)
        }
    }

    private fun setupDropdownStatus() {
        val statusList = listOf("Aktif", "Nonaktif")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, statusList)
        actvStatus.setAdapter(adapter)
    }

    private fun loadKategori() {
        kategoriRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listKategori.clear()
                for (data in snapshot.children) {
                    val k = data.getValue(ModelKategori::class.java)
                    if (k != null) listKategori.add(k)
                }
                val namaList = listKategori.map { it.namaKategori ?: "" }
                val adapter = ArrayAdapter(
                    this@ModProdukActivity,
                    android.R.layout.simple_dropdown_item_1line,
                    namaList
                )
                actvKategori.setAdapter(adapter)

                actvKategori.setOnItemClickListener { _, _, position, _ ->
                    idKategoriDipilih = listKategori[position].idKategori
                }

                if (mode == "edit") {
                    val kategori = listKategori.find { it.idKategori == produkEdit?.idKategori }
                    actvKategori.setText(kategori?.namaKategori, false)
                    idKategoriDipilih = kategori?.idKategori
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun loadCabang() {
        cabangRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listCabang.clear()
                for (data in snapshot.children) {
                    val id = data.child("idCabang").getValue(String::class.java) ?: continue
                    val nama = data.child("namaCabang").getValue(String::class.java) ?: continue
                    listCabang.add(Pair(id, nama))
                }
                val namaList = listCabang.map { it.second }
                val adapter = ArrayAdapter(
                    this@ModProdukActivity,
                    android.R.layout.simple_dropdown_item_1line,
                    namaList
                )
                actvCabang.setAdapter(adapter)

                actvCabang.setOnItemClickListener { _, _, position, _ ->
                    idCabangDipilih = listCabang[position].first
                }

                if (mode == "edit") {
                    val cabang = listCabang.find { it.first == produkEdit?.idCabang }
                    actvCabang.setText(cabang?.second, false)
                    idCabangDipilih = cabang?.first
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun isiDataEdit() {
        produkEdit?.let { p ->
            etNamaProduk.setText(p.namaProduk)
            etHarga.setText(p.hargaProduk?.toString())

            if (p.tanpaBatas == true) {
                cbTanpaBatas.isChecked = true
                etStok.isEnabled = false
            } else {
                etStok.setText(p.stokProduk?.toString())
            }

            actvStatus.setText(p.statusProduk, false)

            val namaFoto = p.fotoProduk ?: ""
            if (namaFoto.isNotEmpty()) {
                fotoTerpilih = namaFoto
                actvFoto.setText(namaFoto, false)
                tampilkanPreview(namaFoto)
            }
        }
    }

    private fun simpanProduk() {
        val nama = etNamaProduk.text.toString().trim()
        val harga = etHarga.text.toString().trim().toIntOrNull() ?: 0
        val tanpaBatas = cbTanpaBatas.isChecked
        val stok = if (tanpaBatas) 0 else etStok.text.toString().trim().toIntOrNull() ?: 0
        val status = actvStatus.text.toString().trim()
        val foto = if (fotoTerpilih.isNotEmpty()) fotoTerpilih else produkEdit?.fotoProduk ?: ""

        if (nama.isEmpty()) {
            etNamaProduk.error = "Nama produk wajib diisi"
            return
        }
        if (idKategoriDipilih == null) {
            Toast.makeText(this, "Pilih kategori terlebih dahulu", Toast.LENGTH_SHORT).show()
            return
        }

        btnSimpan.isEnabled = false
        btnSimpan.text = "Menyimpan..."

        val now = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

        if (mode == "tambah") {
            val idBaru = produkRef.push().key ?: return
            val produk = ModelProduk(
                idProduk = idBaru,
                namaProduk = nama,
                hargaProduk = harga,
                stokProduk = stok,
                tanpaBatas = tanpaBatas,
                idKategori = idKategoriDipilih,
                idCabang = idCabangDipilih,
                fotoProduk = foto,
                statusProduk = status,
                createdAt = now,
                updatedAt = now
            )
            produkRef.child(idBaru).setValue(produk)
                .addOnSuccessListener {
                    Toast.makeText(this, "Produk berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    btnSimpan.isEnabled = true
                    btnSimpan.text = "Simpan"
                    Toast.makeText(this, "Gagal: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            val idEdit = produkEdit?.idProduk ?: return
            val update = mapOf(
                "namaProduk" to nama,
                "hargaProduk" to harga,
                "stokProduk" to stok,
                "tanpaBatas" to tanpaBatas,
                "idKategori" to idKategoriDipilih,
                "idCabang" to idCabangDipilih,
                "fotoProduk" to foto,
                "statusProduk" to status,
                "updatedAt" to now
            )
            produkRef.child(idEdit).updateChildren(update)
                .addOnSuccessListener {
                    Toast.makeText(this, "Produk berhasil diperbarui", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    btnSimpan.isEnabled = true
                    btnSimpan.text = "Simpan"
                    Toast.makeText(this, "Gagal: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}