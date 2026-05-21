package com.nurlana.kasir

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class AkunActivity : AppCompatActivity() {

    private lateinit var ivKembali: ImageView
    private lateinit var tvNamaAkun: TextView
    private lateinit var tvRoleAkun: TextView
    private lateinit var tvNama: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvRole: TextView
    private lateinit var btnLogout: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_akun)

        initViews()
        setupListeners()
    }

    private fun initViews() {
        ivKembali = findViewById(R.id.ivKembali)
        tvNamaAkun = findViewById(R.id.tvNamaAkun)
        tvRoleAkun = findViewById(R.id.tvRoleAkun)
        tvNama = findViewById(R.id.tvNama)
        tvEmail = findViewById(R.id.tvEmail)
        tvRole = findViewById(R.id.tvRole)
        btnLogout = findViewById(R.id.btnLogout)
    }

    private fun setupListeners() {
        ivKembali.setOnClickListener { finish() }

        btnLogout.setOnClickListener {
            Toast.makeText(this, "Berhasil keluar", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}