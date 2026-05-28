package com.nurlana.kasir.laporan

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.firebase.database.*
import com.nurlana.kasir.R
import com.nurlana.kasir.adapter.LaporanAdapter
import com.nurlana.kasir.model.ModelTransaksi
import android.content.Intent
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class LaporanActivity : AppCompatActivity() {

    private lateinit var ivKembali: ImageView
    private lateinit var tvTotalTransaksi: TextView
    private lateinit var tvTotalPendapatan: TextView
    private lateinit var rvLaporan: RecyclerView
    private lateinit var btnTanggalDari: MaterialButton
    private lateinit var btnTanggalSampai: MaterialButton
    private lateinit var btnFilter: MaterialButton
    private lateinit var btnReset: MaterialButton

    private lateinit var adapter: LaporanAdapter
    private val listTransaksi = mutableListOf<ModelTransaksi>()
    private val format = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    private var tanggalDari: String? = null
    private var tanggalSampai: String? = null

    private val transaksiRef by lazy {
        FirebaseDatabase.getInstance().getReference("transaksi")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_laporan)

        initViews()
        setupRecyclerView()
        loadData()
        setupListeners()
    }

    private fun initViews() {
        ivKembali = findViewById(R.id.ivKembali)
        tvTotalTransaksi = findViewById(R.id.tvTotalTransaksi)
        tvTotalPendapatan = findViewById(R.id.tvTotalPendapatan)
        rvLaporan = findViewById(R.id.rvLaporan)
        btnTanggalDari = findViewById(R.id.btnTanggalDari)
        btnTanggalSampai = findViewById(R.id.btnTanggalSampai)
        btnFilter = findViewById(R.id.btnFilter)
        btnReset = findViewById(R.id.btnReset)
    }

    private fun setupRecyclerView() {
        adapter = LaporanAdapter(mutableListOf()) { transaksi ->
            val intent = Intent(this, com.nurlana.kasir.transaksi.StrukActivity::class.java)
            intent.putExtra("transaksi", transaksi)
            startActivity(intent)
        }
        rvLaporan.adapter = adapter
    }

    private fun setupListeners() {
        ivKembali.setOnClickListener { finish() }

        btnTanggalDari.setOnClickListener {
            showDatePicker { tanggal ->
                tanggalDari = tanggal
                btnTanggalDari.text = tanggal
            }
        }

        btnTanggalSampai.setOnClickListener {
            showDatePicker { tanggal ->
                tanggalSampai = tanggal
                btnTanggalSampai.text = tanggal
            }
        }

        btnFilter.setOnClickListener {
            if (tanggalDari == null || tanggalSampai == null) {
                Toast.makeText(this, "Pilih tanggal dulu", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            filterData()
        }

        btnReset.setOnClickListener {
            tanggalDari = null
            tanggalSampai = null
            btnTanggalDari.text = "Pilih Tanggal"
            btnTanggalSampai.text = "Pilih Tanggal"
            updateUI(listTransaksi)
        }
    }

    private fun showDatePicker(onDateSelected: (String) -> Unit) {
        val cal = Calendar.getInstance()
        DatePickerDialog(
            this,
            { _, year, month, day ->
                val tanggal = String.format("%04d-%02d-%02d", year, month + 1, day)
                onDateSelected(tanggal)
            },
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun filterData() {
        val dari = tanggalDari ?: return
        val sampai = tanggalSampai ?: return

        val filtered = listTransaksi.filter { t ->
            val tgl = t.tanggal ?: return@filter false
            tgl >= dari && tgl <= sampai
        }

        if (filtered.isEmpty()) {
            Toast.makeText(this, "Tidak ada transaksi pada periode ini", Toast.LENGTH_SHORT).show()
        }
        updateUI(filtered)
    }

    private fun updateUI(list: List<ModelTransaksi>) {
        adapter.updateData(list.toMutableList())
        tvTotalTransaksi.text = list.size.toString()
        val totalPendapatan = list.sumOf { it.total ?: 0 }
        tvTotalPendapatan.text = format.format(totalPendapatan)
    }

    private fun loadData() {
        transaksiRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listTransaksi.clear()
                for (data in snapshot.children) {
                    val t = data.getValue(ModelTransaksi::class.java)
                    if (t != null) listTransaksi.add(t)
                }
                listTransaksi.sortByDescending { it.tanggal }
                updateUI(listTransaksi)
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }
}