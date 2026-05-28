package com.nurlana.kasir.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ModelCabang(
    val idCabang: String? = null,
    val namaCabang: String? = null,
    val nomorTelepon: String? = null,
    val alamat: String? = null,
    val statusCabang: String? = null,
    val createdAt: String? = null
) : Parcelable