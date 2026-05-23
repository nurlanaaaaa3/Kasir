package com.nurlana.kasir.transaksi

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.nurlana.kasir.MainActivity
import com.nurlana.kasir.R
import com.nurlana.kasir.model.ModelTransaksi
import java.text.NumberFormat
import java.util.Locale

class StrukActivity : AppCompatActivity() {

    private lateinit var ivKembali: ImageView
    private lateinit var tvTanggal: TextView
    private lateinit var tvJam: TextView
    private lateinit var tvNomorTransaksi: TextView
    private lateinit var tvNamaPelanggan: TextView
    private lateinit var llItems: LinearLayout
    private lateinit var tvTotal: TextView
    private lateinit var tvBayar: TextView
    private lateinit var tvKembali: TextView
    private lateinit var btnTransaksiBaru: MaterialButton
    private lateinit var btnKeDashboard: MaterialButton

    private val format = NumberFormat.getCurrencyInstance(Locale("id", "ID"))

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
        llItems = findViewById(R.id.llItems)
        tvTotal = findViewById(R.id.tvTotal)
        tvBayar = findViewById(R.id.tvBayar)
        tvKembali = findViewById(R.id.tvKembali)
        btnTransaksiBaru = findViewById(R.id.btnTransaksiBaru)
        btnKeDashboard = findViewById(R.id.btnKeDashboard)
    }

    private fun tampilkanStruk() {
        val transaksi = intent.getParcelableExtra<ModelTransaksi>("transaksi") ?: return

        tvTanggal.text = transaksi.tanggal ?: "-"
        tvJam.text = transaksi.jam ?: "-"
        tvNomorTransaksi.text = transaksi.nomorTransaksi ?: "-"
        tvNamaPelanggan.text = transaksi.namaPelanggan ?: "-"
        tvTotal.text = format.format(transaksi.total ?: 0)
        tvBayar.text = format.format(transaksi.bayar ?: 0)
        tvKembali.text = format.format(transaksi.kembali ?: 0)

        transaksi.items?.forEach { item ->
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
            finish()
        }

        btnKeDashboard.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
    }
}