package com.nurlana.kasir

import android.content.Intent
import android.content.SharedPreferences
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
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_akun)

        prefs = getSharedPreferences("kasir_prefs", MODE_PRIVATE)

        initViews()
        tampilkanData()
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

    private fun tampilkanData() {
        val nama = prefs.getString("namaUser", "-") ?: "-"
        val username = prefs.getString("username", "-") ?: "-"
        val role = prefs.getString("role", "-") ?: "-"

        tvNamaAkun.text = nama
        tvRoleAkun.text = role
        tvNama.text = nama
        tvEmail.text = username
        tvRole.text = role
    }

    private fun setupListeners() {
        ivKembali.setOnClickListener { finish() }

        btnLogout.setOnClickListener {
            prefs.edit().clear().apply()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }
}