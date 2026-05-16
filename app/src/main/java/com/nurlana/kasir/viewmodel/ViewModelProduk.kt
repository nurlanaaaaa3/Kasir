package com.nurlana.kasir.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.nurlana.kasir.model.ModelProduk

class ViewModelProduk : ViewModel() {

    private val _produkList = MutableLiveData<List<ModelProduk>>()
    val produkList: LiveData<List<ModelProduk>> = _produkList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val dbRef = FirebaseDatabase.getInstance().getReference("produk")

    fun loadProduk() {
        _isLoading.value = true
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<ModelProduk>()
                for (data in snapshot.children) {
                    val produk = data.getValue(ModelProduk::class.java)
                    if (produk != null) {
                        list.add(produk)
                    }
                }

                _produkList.value = list
                _isLoading.value = false
            }

            override fun onCancelled(error: DatabaseError) {
                _errorMessage.value = error.message
                _isLoading.value = false
            }
        })
    }

    fun loadProdukByKategori(idKategori: String) {
        _isLoading.value = true
        dbRef.orderByChild("idKategori").equalTo(idKategori)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = mutableListOf<ModelProduk>()
                    for (data in snapshot.children) {
                        val produk = data.getValue(ModelProduk::class.java)
                        if (produk != null) {
                            list.add(produk)
                        }
                    }
                    _produkList.value = list
                    _isLoading.value = false
                }

                override fun onCancelled(error: DatabaseError) {
                    _errorMessage.value = error.message
                    _isLoading.value = false
                }
            })
    }

    fun searchProduk(query: String) {
        val currentList = _produkList.value ?: return
        val filtered = currentList.filter {
            it.namaProduk?.contains(query, ignoreCase = true) == true ||
                    it.idKategori?.contains(query, ignoreCase = true) == true
        }
        _produkList.value = filtered
    }
}