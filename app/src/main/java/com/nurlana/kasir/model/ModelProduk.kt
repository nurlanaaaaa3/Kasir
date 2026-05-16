package com.nurlana.kasir.model

import android.os.Parcel
import android.os.Parcelable

data class ModelProduk(
    val idProduk: String? = null,
    val namaProduk: String? = null,
    val hargaProduk: Int? = 0,
    val idKategori: String? = null,
    val idCabang: String? = null,
    val fotoProduk: String? = null,
    val stokProduk: Int? = 0,
    val tanpaBatas: Boolean? = false,
    val statusProduk: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
) : Parcelable {

    var jumlahTerjual: Int = 0

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(idProduk)
        parcel.writeString(namaProduk)
        parcel.writeValue(hargaProduk)
        parcel.writeString(idKategori)
        parcel.writeString(idCabang)
        parcel.writeString(fotoProduk)
        parcel.writeValue(stokProduk)
        parcel.writeValue(tanpaBatas)
        parcel.writeString(statusProduk)
        parcel.writeString(createdAt)
        parcel.writeString(updatedAt)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<ModelProduk> {
        override fun createFromParcel(parcel: Parcel): ModelProduk {
            return ModelProduk(parcel)
        }

        override fun newArray(size: Int): Array<ModelProduk?> {
            return arrayOfNulls(size)
        }
    }
}