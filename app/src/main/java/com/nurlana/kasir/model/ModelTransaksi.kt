package com.nurlana.kasir.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ItemTransaksi(
    val idProduk: String? = null,
    val namaProduk: String? = null,
    val harga: Long? = null,
    val jumlah: Int? = null,
    val subtotal: Long? = null
) : Parcelable

@Parcelize
data class ModelTransaksi(
    val idTransaksi: String? = null,
    val nomorTransaksi: String? = null,
    val tanggal: String? = null,
    val jam: String? = null,
    val namaPelanggan: String? = null,
    val items: List<ItemTransaksi>? = null,
    val total: Long? = null,
    val bayar: Long? = null,
    val kembali: Long? = null,
    val createdAt: String? = null
) : Parcelable