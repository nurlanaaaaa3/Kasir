package com.nurlana.kasir.laporan

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.nurlana.kasir.R
import com.nurlana.kasir.adapter.LaporanAdapter
import com.nurlana.kasir.model.ModelTransaksi
import java.text.NumberFormat
import java.util.Locale

class LaporanActivity : AppCompatActivity() {

    private lateinit var ivKembali: ImageView
    private lateinit var tvTotalTransaksi: TextView
    private lateinit var tvTotalPendapatan: TextView
    private lateinit var rvLaporan: RecyclerView

    private lateinit var adapter: LaporanAdapter
    private val listTransaksi = mutableListOf<ModelTransaksi>()

    private val transaksiRef by lazy {
        FirebaseDatabase.getInstance().getReference("transaksi")
    }

    private val format = NumberFormat.getCurrencyInstance(Locale("id", "ID"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_laporan)

        initViews()
        setupRecyclerView()
        loadData()
    }

    private fun initViews() {
        ivKembali = findViewById(R.id.ivKembali)
        tvTotalTransaksi = findViewById(R.id.tvTotalTransaksi)
        tvTotalPendapatan = findViewById(R.id.tvTotalPendapatan)
        rvLaporan = findViewById(R.id.rvLaporan)
    }

    private fun setupRecyclerView() {
        adapter = LaporanAdapter(mutableListOf())
        rvLaporan.adapter = adapter
    }

    private fun loadData() {
        transaksiRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listTransaksi.clear()
                for (data in snapshot.children) {
                    val t = data.getValue(ModelTransaksi::class.java)
                    if (t != null) listTransaksi.add(t)
                }
                adapter.updateData(mutableListOf<ModelTransaksi>().apply { addAll(listTransaksi) })
                tvTotalTransaksi.text = listTransaksi.size.toString()
                val totalPendapatan = listTransaksi.sumOf { it.total ?: 0 }
                tvTotalPendapatan.text = format.format(totalPendapatan)
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }
}