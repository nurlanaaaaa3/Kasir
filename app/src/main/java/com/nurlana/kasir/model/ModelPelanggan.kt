package com.nurlana.kasir.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ModelPelanggan(
    val idPelanggan: String? = null,
    val namaPelanggan: String? = null,
    val nomorHP: String? = null,
    val alamat: String? = null,
    val createdAt: String? = null
) : Parcelable