package com.nurlana.kasir

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.firebase.database.FirebaseDatabase
import com.nurlana.kasir.kategori.DataKategoriActivity
import com.nurlana.kasir.Produk.DataProdukActivity
import com.nurlana.kasir.transaksi.TransaksiActivity
import com.nurlana.kasir.pelanggan.DataPelangganActivity
import com.nurlana.kasir.laporan.LaporanActivity
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var tvGreeting: TextView
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

    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        database = FirebaseDatabase.getInstance()

        initViews()
        setupGreetingAndDate()
        setupEstimasi()
        setupClickListeners()
    }

    private fun initViews() {
        tvGreeting = findViewById(R.id.tvGreeting)
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
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val greeting = when {
            hour < 12 -> "Selamat Pagi"
            hour < 15 -> "Selamat Siang"
            hour < 18 -> "Selamat Sore"
            else -> "Selamat Malam"
        }
        tvGreeting.text = greeting

        val dateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id", "ID"))
        tvDate.text = dateFormat.format(Date())
    }

    private fun setupEstimasi() {
        val format = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        tvEstimasi.text = format.format(0)
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
            Toast.makeText(this, "Pegawai", Toast.LENGTH_SHORT).show()
        }
        menuCabang.setOnClickListener {
            Toast.makeText(this, "Cabang", Toast.LENGTH_SHORT).show()
        }
        menuPrinter.setOnClickListener {
            Toast.makeText(this, "Printer", Toast.LENGTH_SHORT).show()
        }
    }
}