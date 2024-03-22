package com.example.retrofit.network.model.mangadetail

import com.google.gson.annotations.SerializedName

data class Description(
    val en: String?,
    val ar: String?,
    val az: String?,
    val cs: String?,
    val de: String?,
    val el: String?,
    val es: String?,
    @SerializedName("es-la")
    val es_la: String?,
    val fa: String?,
    val fi: String?,
    val fr: String?,
    val hr: String?,
    val hu: String?,
    val id: String?,
    val it: String?,
    val ja: String?,
    val ka: String?,
    val kk: String?,
    val ko: String?,
    val pl: String?,
    val pt: String?,
    @SerializedName("pt-br")
    val pt_br: String?,
    val ro: String?,
    val ru: String?,
    val sr: String?,
    val th: String?,
    val tr: String?,
    val uk: String?,
    val vi: String?,
    val zh: String?,
    @SerializedName("zh-hk")
    val zh_hk: String?
)