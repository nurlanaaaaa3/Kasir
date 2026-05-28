package com.nurlana.kasir

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.*

class LoginActivity : AppCompatActivity() {

    private lateinit var etUsername: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var btnLogin: MaterialButton
    private lateinit var prefs: SharedPreferences

    private val userRef by lazy {
        FirebaseDatabase.getInstance().getReference("users")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        prefs = getSharedPreferences("kasir_prefs", MODE_PRIVATE)

        if (prefs.getString("namaUser", null) != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_login)

        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)

        btnLogin.setOnClickListener { login() }
    }

    private fun login() {
        val username = etUsername.text.toString().trim()
        val password = etPassword.text.toString().trim()

        if (username.isEmpty()) {
            etUsername.error = "Username tidak boleh kosong"
            return
        }
        if (password.isEmpty()) {
            etPassword.error = "Password tidak boleh kosong"
            return
        }

        btnLogin.isEnabled = false
        btnLogin.text = "Loading..."

        userRef.orderByChild("username").equalTo(username)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (!snapshot.exists()) {
                        Toast.makeText(this@LoginActivity, "Username tidak ditemukan", Toast.LENGTH_SHORT).show()
                        btnLogin.isEnabled = true
                        btnLogin.text = "Login"
                        return
                    }

                    for (data in snapshot.children) {
                        val passwordFirebase = data.child("password").value?.toString() ?: ""
                        val idUser = data.child("idUser").value?.toString() ?: ""
                        val namaLengkap = data.child("namaLengkap").value?.toString() ?: ""
                        val usernameDb = data.child("username").value?.toString() ?: ""
                        val role = data.child("role").value?.toString() ?: ""

                        if (passwordFirebase == password) {
                            prefs.edit()
                                .putString("idUser", idUser)
                                .putString("namaUser", namaLengkap)
                                .putString("username", usernameDb)
                                .putString("role", role)
                                .apply()

                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this@LoginActivity, "Password salah", Toast.LENGTH_SHORT).show()
                            btnLogin.isEnabled = true
                            btnLogin.text = "Login"
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    btnLogin.isEnabled = true
                    btnLogin.text = "Login"
                }
            })
    }
}