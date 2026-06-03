package com.nurlana.kasir.transaksi

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.material.button.MaterialButton
import com.nurlana.kasir.transaksi.TransaksiActivity
import com.nurlana.kasir.R
import com.nurlana.kasir.model.ModelTransaksi
import com.dantsu.escposprinter.EscPosPrinter
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections
import java.text.NumberFormat
import java.util.Locale

class StrukActivity : AppCompatActivity() {

    private lateinit var ivKembali: ImageView
    private lateinit var tvTanggal: TextView
    private lateinit var tvJam: TextView
    private lateinit var tvNomorTransaksi: TextView
    private lateinit var tvNamaPelanggan: TextView
    private lateinit var tvNamaKasir: TextView
    private lateinit var llItems: LinearLayout
    private lateinit var tvTotal: TextView
    private lateinit var tvBayar: TextView
    private lateinit var tvKembali: TextView
    private lateinit var btnTransaksiBaru: MaterialButton
    private lateinit var btnKeDashboard: MaterialButton
    private lateinit var btnPrint: MaterialButton

    private val format = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
    private var transaksi: ModelTransaksi? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_struk)

        initViews()
        tampilkanStruk()
        setupListeners()
    }

    private fun initViews() {
        ivKembali = findViewById(R.id.ivKembali)
        tvTanggal = findViewById(R.id.tvTanggal)
        tvJam = findViewById(R.id.tvJam)
        tvNomorTransaksi = findViewById(R.id.tvNomorTransaksi)
        tvNamaPelanggan = findViewById(R.id.tvNamaPelanggan)
        tvNamaKasir = findViewById(R.id.tvNamaKasir)
        llItems = findViewById(R.id.llItems)
        tvTotal = findViewById(R.id.tvTotal)
        tvBayar = findViewById(R.id.tvBayar)
        tvKembali = findViewById(R.id.tvKembali)
        btnTransaksiBaru = findViewById(R.id.btnTransaksiBaru)
        btnKeDashboard = findViewById(R.id.btnKeDashboard)
        btnPrint = findViewById(R.id.btnPrint)
    }

    private fun tampilkanStruk() {
        transaksi = intent.getParcelableExtra("transaksi")
        val t = transaksi ?: return

        tvTanggal.text = t.tanggal ?: "-"
        tvJam.text = t.jam ?: "-"
        tvNomorTransaksi.text = t.nomorTransaksi ?: "-"
        tvNamaPelanggan.text = t.namaPelanggan ?: "-"
        tvNamaKasir.text = t.namaKasir ?: "-"
        tvTotal.text = format.format(t.total ?: 0)
        tvBayar.text = format.format(t.bayar ?: 0)
        tvKembali.text = format.format(t.kembali ?: 0)

        t.items?.forEach { item ->
            val itemView = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply { setMargins(0, 0, 0, 8) }
            }

            val tvNama = TextView(this).apply {
                text = item.namaProduk ?: "-"
                textSize = 14f
                setTextColor(resources.getColor(android.R.color.black, null))
                setTypeface(null, android.graphics.Typeface.BOLD)
            }

            val rowHarga = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            }

            val tvHarga = TextView(this).apply {
                text = "${item.jumlah} x ${format.format(item.harga ?: 0)}"
                textSize = 13f
                setTextColor(resources.getColor(android.R.color.darker_gray, null))
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            }

            val tvSubtotal = TextView(this).apply {
                text = format.format(item.subtotal ?: 0)
                textSize = 13f
                setTextColor(resources.getColor(android.R.color.black, null))
            }

            rowHarga.addView(tvHarga)
            rowHarga.addView(tvSubtotal)
            itemView.addView(tvNama)
            itemView.addView(rowHarga)
            llItems.addView(itemView)
        }
    }

    private fun setupListeners() {
        ivKembali.setOnClickListener { finish() }
        btnTransaksiBaru.setOnClickListener {
            val intent = Intent(this, TransaksiActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }
        btnPrint.setOnClickListener { printStruk() }
    }

    private fun printStruk() {
        val t = transaksi ?: return

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.BLUETOOTH_CONNECT),
                    1001
                )
                return
            }
        }

        Thread {
            try {
                val printer = EscPosPrinter(
                    BluetoothPrintersConnections.selectFirstPaired(),
                    203, 48f, 32
                )

                val sb = StringBuilder()
                sb.append("[C]<u><font size='big'>CREAMORA</font></u>\n")
                sb.append("[C]================================\n")
                sb.append("[L]Tanggal  : ${t.tanggal}\n")
                sb.append("[L]Jam      : ${t.jam}\n")
                sb.append("[L]No       : ${t.nomorTransaksi}\n")
                sb.append("[L]Pelanggan: ${t.namaPelanggan ?: "-"}\n")
                sb.append("[L]Kasir    : ${t.namaKasir ?: "-"}\n")
                sb.append("[C]--------------------------------\n")

                t.items?.forEach { item ->
                    sb.append("[L]${item.namaProduk}\n")
                    sb.append("[L]${item.jumlah} x ${format.format(item.harga ?: 0)}[R]${format.format(item.subtotal ?: 0)}\n")
                }

                sb.append("[C]================================\n")
                sb.append("[L]Total  [R]${format.format(t.total ?: 0)}\n")
                sb.append("[L]Bayar  [R]${format.format(t.bayar ?: 0)}\n")
                sb.append("[L]Kembali[R]${format.format(t.kembali ?: 0)}\n")
                sb.append("[C]================================\n")
                sb.append("[C]Terima Kasih!\n\n\n")

                printer.printFormattedText(sb.toString())

                runOnUiThread {
                    Toast.makeText(this, "Print berhasil!", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this, "Gagal print: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }
}