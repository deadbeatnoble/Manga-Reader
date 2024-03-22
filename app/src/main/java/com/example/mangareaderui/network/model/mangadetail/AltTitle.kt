package com.example.retrofit.network.model.mangadetail

import com.google.gson.annotations.SerializedName

data class AltTitle(
    val en: String?,
    val ja: String?,
    val ru: String?,
    val vi: String?,

    val ar: String?,
    val az: String?,
    val bg: String?,
    val bn: String?,
    val ca: String?,
    val cs: String?,
    val de: String?,
    val el: String?,
    val es: String?,
    @SerializedName("es-la")
    val es_la: String?,
    val et: String?,
    val fa: String?,
    val fi: String?,
    val fr: String?,
    val he: String?,
    val hi: String?,
    val hr: String?,
    val hu: String?,
    val id: String?,
    val it: String?,
    @SerializedName("ja-ro")
    val ja_ro: String?,
    val ka: String?,
    val kk: String?,
    val ko: String?,
    @SerializedName("ko-ro")
    val ko_ro: String?,
    val la: String,
    val lt: String,
    val mn: String,
    val ms: String,
    val my: String,
    val ne: String,
    val no: String,
    val pl: String,
    val pt: String,
    @SerializedName("pt-br")
    val pt_br: String,
    val ro: String,
    val sq: String,
    val sr: String,
    val sv: String,
    val ta: String,
    val th: String,
    val tl: String,
    val tr: String,
    val uk: String,
    val zh: String,
    @SerializedName("zh-hk")
    val zh_hk: String,
    @SerializedName("zh-ro")
    val zh_ro: String
)