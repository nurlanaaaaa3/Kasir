package com.nurlana.kasir

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.firebase.database.FirebaseDatabase
import com.nurlana.kasir.kategori.DataKategoriActivity
import com.nurlana.kasir.Produk.DataProdukActivity
import com.nurlana.kasir.cabang.DataCabangActivity
import com.nurlana.kasir.transaksi.TransaksiActivity
import com.nurlana.kasir.pelanggan.DataPelangganActivity
import com.nurlana.kasir.laporan.LaporanActivity
import com.nurlana.kasir.pegawai.DataPegawaiActivity
import com.nurlana.kasir.printer.PrinterActivity
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var tvGreeting: TextView
    private lateinit var tvWelcome: TextView
    private lateinit var tvDate: TextView
    private lateinit var tvEstimasi: TextView

    private lateinit var menuTransaksi: LinearLayout
    private lateinit var menuPelanggan: LinearLayout
    private lateinit var menuLaporan: LinearLayout

    private lateinit var menuAkun: CardView
    private lateinit var menuLayanan: CardView
    private lateinit var menuTambahan: CardView
    private lateinit var menuPegawai: CardView
    private lateinit var menuCabang: CardView
    private lateinit var menuPrinter: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        initViews()
        setupGreetingAndDate()
        setupEstimasi()
        setupClickListeners()
    }

    private fun initViews() {
        tvGreeting = findViewById(R.id.tvGreeting)
        tvWelcome = findViewById(R.id.tvWelcome)
        tvDate = findViewById(R.id.tvDate)
        tvEstimasi = findViewById(R.id.tvEstimasi)

        menuTransaksi = findViewById(R.id.menuTransaksi)
        menuPelanggan = findViewById(R.id.menuPelanggan)
        menuLaporan = findViewById(R.id.menuLaporan)
        menuAkun = findViewById(R.id.menuAkun)
        menuLayanan = findViewById(R.id.menuLayanan)
        menuTambahan = findViewById(R.id.menuTambahan)
        menuPegawai = findViewById(R.id.menuPegawai)
        menuCabang = findViewById(R.id.menuCabang)
        menuPrinter = findViewById(R.id.menuPrinter)
    }

    private fun setupGreetingAndDate() {
        val prefs = getSharedPreferences("kasir_prefs", MODE_PRIVATE)
        val namaUser = prefs.getString("namaUser", "Kasir") ?: "Kasir"

        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val greeting = when {
            hour < 12 -> "Selamat Pagi"
            hour < 15 -> "Selamat Siang"
            hour < 18 -> "Selamat Sore"
            else -> "Selamat Malam"
        }
        tvGreeting.text = "$greeting, $namaUser!"
        tvWelcome.text = "Selamat datang di Creamora 🍦"

        val dateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id", "ID"))
        tvDate.text = dateFormat.format(Date())
    }

    private fun setupEstimasi() {
        val format = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        FirebaseDatabase.getInstance().getReference("transaksi")
            .addValueEventListener(object : com.google.firebase.database.ValueEventListener {
                override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {
                    var totalHariIni = 0L
                    for (data in snapshot.children) {
                        val tanggal = data.child("tanggal").value?.toString() ?: ""
                        val total = data.child("total").value
                        if (tanggal == today) {
                            totalHariIni += when (total) {
                                is Long -> total
                                is Int -> total.toLong()
                                else -> 0L
                            }
                        }
                    }
                    tvEstimasi.text = format.format(totalHariIni)
                }
                override fun onCancelled(error: com.google.firebase.database.DatabaseError) {}
            })
    }

    private fun setupClickListeners() {
        menuTransaksi.setOnClickListener {
            startActivity(Intent(this, TransaksiActivity::class.java))
        }
        menuPelanggan.setOnClickListener {
            startActivity(Intent(this, DataPelangganActivity::class.java))
        }
        menuLaporan.setOnClickListener {
            startActivity(Intent(this, LaporanActivity::class.java))
        }
        menuAkun.setOnClickListener {
            startActivity(Intent(this, AkunActivity::class.java))
        }
        menuLayanan.setOnClickListener {
            startActivity(Intent(this, DataProdukActivity::class.java))
        }
        menuTambahan.setOnClickListener {
            startActivity(Intent(this, DataKategoriActivity::class.java))
        }
        menuPegawai.setOnClickListener {
            startActivity(Intent(this, DataPegawaiActivity::class.java))
        }
        menuCabang.setOnClickListener {
            startActivity(Intent(this, DataCabangActivity::class.java))
        }
        menuPrinter.setOnClickListener {
            startActivity(Intent(this, PrinterActivity::class.java))
        }
    }
}