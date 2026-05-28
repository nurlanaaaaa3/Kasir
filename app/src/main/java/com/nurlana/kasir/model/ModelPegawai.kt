package com.nurlana.kasir.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ModelPegawai(
    val idPegawai: String? = null,
    val namaPegawai: String? = null,
    val nomorHP: String? = null,
    val jabatan: String? = null,
    val alamat: String? = null,
    val email: String? = null,
    val tanggalMasuk: String? = null,
    val statusPegawai: String? = null,
    val createdAt: String? = null
) : Parcelable
