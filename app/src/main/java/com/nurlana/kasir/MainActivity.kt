package com.nurlana.kasir

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.nurlana.kasir.kategori.DataKategoriActivity
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var tvGreeting: TextView
    private lateinit var tvDate: TextView
    private lateinit var tvEstimasi: TextView

    // Menu cards
    private lateinit var menuTransaksi: CardView
    private lateinit var menuPelanggan: CardView
    private lateinit var menuLaporan: CardView
    private lateinit var menuAkun: CardView
    private lateinit var menuLayanan: CardView
    private lateinit var menuTambahan: CardView
    private lateinit var menuPegawai: CardView
    private lateinit var menuCabang: CardView
    private lateinit var menuPrinter: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(resources.getIdentifier("activity_main", "layout", packageName))

        // Inisialisasi views
        initViews()

        // Set greeting dan tanggal
        setupGreetingAndDate()

        // Set estimasi
        setupEstimasi()

        // Setup click listeners
        setupClickListeners()
    }

    private fun initViews() {
        try {
            tvGreeting = findViewById(getResId("tvGreeting", "id"))
            tvDate = findViewById(getResId("tvDate", "id"))
            tvEstimasi = findViewById(getResId("tvEstimasi", "id"))

            menuTransaksi = findViewById(getResId("menuTransaksi", "id"))
            menuPelanggan = findViewById(getResId("menuPelanggan", "id"))
            menuLaporan = findViewById(getResId("menuLaporan", "id"))
            menuAkun = findViewById(getResId("menuAkun", "id"))
            menuLayanan = findViewById(getResId("menuLayanan", "id"))
            menuTambahan = findViewById(getResId("menuTambahan", "id"))
            menuPegawai = findViewById(getResId("menuPegawai", "id"))
            menuCabang = findViewById(getResId("menuCabang", "id"))
            menuPrinter = findViewById(getResId("menuPrinter", "id"))
        } catch (e: Exception) {
            Toast.makeText(this, "Error inisialisasi: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getResId(resName: String, resType: String): Int {
        return resources.getIdentifier(resName, resType, packageName)
    }

    private fun setupGreetingAndDate() {
        // Set greeting berdasarkan waktu
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)

        val greeting = when (hour) {
            in 0..10 -> "Selamat Pagi"
            in 11..14 -> "Selamat Siang"
            in 15..18 -> "Selamat Sore"
            else -> "Selamat Malam"
        }

        tvGreeting.text = "$greeting, Nurlana!"

        // Set tanggal
        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
        val currentDate = dateFormat.format(Date())
        tvDate.text = currentDate
    }

    private fun setupEstimasi() {
        // Format rupiah
        val estimasi = 20000.0
        val formatRupiah = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        tvEstimasi.text = formatRupiah.format(estimasi).replace("Rp", "Rp ")
    }

    private fun setupClickListeners() {
        // Menu Transaksi
        menuTransaksi.setOnClickListener {
            Toast.makeText(this, "Menu Transaksi", Toast.LENGTH_SHORT).show()
            // TODO: Buka TransaksiActivity
            // startActivity(Intent(this, TransaksiActivity::class.java))
        }

        // Menu Pelanggan
        menuPelanggan.setOnClickListener {
            Toast.makeText(this, "Menu Pelanggan", Toast.LENGTH_SHORT).show()
            // TODO: Buka PelangganActivity
            // startActivity(Intent(this, PelangganActivity::class.java))
        }

        // Menu Laporan
        menuLaporan.setOnClickListener {
            Toast.makeText(this, "Menu Laporan", Toast.LENGTH_SHORT).show()
            // TODO: Buka LaporanActivity
            // startActivity(Intent(this, LaporanActivity::class.java))
        }

        // Menu Akun
        menuAkun.setOnClickListener {
            Toast.makeText(this, "Menu Akun", Toast.LENGTH_SHORT).show()
            // TODO: Buka AkunActivity
            // startActivity(Intent(this, AkunActivity::class.java))
        }

        // Menu Produk (Layanan)
        menuLayanan.setOnClickListener {
            Toast.makeText(this, "Menu Produk", Toast.LENGTH_SHORT).show()
            // TODO: Buka ProdukActivity
            // startActivity(Intent(this, ProdukActivity::class.java))
        }

        // Menu Kategori (Tambahan)
        menuTambahan.setOnClickListener {
            Toast.makeText(this, "Menu Kategori", Toast.LENGTH_SHORT).show()
            // Buka DataKategoriActivity
            startActivity(Intent(this, DataKategoriActivity::class.java))
        }

        // Menu Pegawai
        menuPegawai.setOnClickListener {
            Toast.makeText(this, "Menu Pegawai", Toast.LENGTH_SHORT).show()
            // TODO: Buka PegawaiActivity
            // startActivity(Intent(this, PegawaiActivity::class.java))
        }

        // Menu Cabang
        menuCabang.setOnClickListener {
            Toast.makeText(this, "Menu Cabang", Toast.LENGTH_SHORT).show()
            // TODO: Buka CabangActivity
            // startActivity(Intent(this, CabangActivity::class.java))
        }

        // Menu Printer
        menuPrinter.setOnClickListener {
            Toast.makeText(this, "Menu Printer", Toast.LENGTH_SHORT).show()
            // TODO: Buka PrinterActivity
            // startActivity(Intent(this, PrinterActivity::class.java))
        }
    }
}