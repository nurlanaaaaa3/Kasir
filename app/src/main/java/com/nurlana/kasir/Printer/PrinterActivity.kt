package com.nurlana.kasir.printer

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.nurlana.kasir.R
import com.nurlana.kasir.adapter.PerangkatAdapter
import com.dantsu.escposprinter.EscPosPrinter
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections

class PrinterActivity : AppCompatActivity() {

    private lateinit var ivKembali: ImageView
    private lateinit var tvStatusPrinter: TextView
    private lateinit var tvNamaPrinter: TextView
    private lateinit var rvPerangkat: RecyclerView
    private lateinit var btnScan: MaterialButton
    private lateinit var btnTestPrint: MaterialButton

    private lateinit var adapter: PerangkatAdapter
    private var bluetoothAdapter: BluetoothAdapter? = null
    private var selectedDevice: BluetoothDevice? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_printer)

        initViews()
        setupBluetooth()
        setupRecyclerView()
        setupListeners()
    }

    private fun initViews() {
        ivKembali = findViewById(R.id.ivKembali)
        tvStatusPrinter = findViewById(R.id.tvStatusPrinter)
        tvNamaPrinter = findViewById(R.id.tvNamaPrinter)
        rvPerangkat = findViewById(R.id.rvPerangkat)
        btnScan = findViewById(R.id.btnScan)
        btnTestPrint = findViewById(R.id.btnTestPrint)
    }

    private fun setupBluetooth() {
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Perangkat tidak mendukung Bluetooth", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupRecyclerView() {
        adapter = PerangkatAdapter(mutableListOf()) { device ->
            selectedDevice = device
            tvStatusPrinter.text = "Terhubung"
            tvStatusPrinter.setTextColor(getColor(android.R.color.holo_green_dark))
            tvNamaPrinter.text = device.name ?: device.address
            Toast.makeText(this, "Printer dipilih: ${device.name}", Toast.LENGTH_SHORT).show()
        }
        rvPerangkat.layoutManager = LinearLayoutManager(this)
        rvPerangkat.adapter = adapter
    }

    private fun setupListeners() {
        ivKembali.setOnClickListener { finish() }

        btnScan.setOnClickListener {
            scanPerangkat()
        }

        btnTestPrint.setOnClickListener {
            testPrint()
        }
    }

    private fun scanPerangkat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN),
                    1001
                )
                return
            }
        }

        val pairedDevices = bluetoothAdapter?.bondedDevices
        if (pairedDevices.isNullOrEmpty()) {
            Toast.makeText(this, "Tidak ada perangkat yang dipasangkan", Toast.LENGTH_SHORT).show()
            return
        }
        adapter.updateData(pairedDevices.toList())
        Toast.makeText(this, "${pairedDevices.size} perangkat ditemukan", Toast.LENGTH_SHORT).show()
    }

    private fun testPrint() {
        val device = selectedDevice
        if (device == null) {
            Toast.makeText(this, "Pilih printer dulu", Toast.LENGTH_SHORT).show()
            return
        }

        Thread {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT)
                        != PackageManager.PERMISSION_GRANTED) return@Thread
                }

                val printer = EscPosPrinter(
                    BluetoothPrintersConnections.selectFirstPaired(),
                    203, 48f, 32
                )
                printer.printFormattedText(
                    "[C]<u><font size='big'>NAMA TOKO</font></u>\n" +
                            "[C]================================\n" +
                            "[C]Test Print Berhasil!\n" +
                            "[C]================================\n" +
                            "[C]Terima Kasih\n\n\n"
                )
                runOnUiThread {
                    Toast.makeText(this, "Test print berhasil!", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this, "Gagal: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }
}