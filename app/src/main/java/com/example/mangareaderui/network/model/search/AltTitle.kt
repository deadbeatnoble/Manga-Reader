package com.example.retrofit.network.model.searchedmanga

import com.google.gson.annotations.SerializedName

data class AltTitle(
    val ar: String,
    val az: String,
    val cs: String,
    val da: String,
    val de: String,
    val el: String,
    val en: String,
    val es: String,
    @SerializedName("es-la")
    val es_la: String,
    val fa: String,
    val fi: String,
    val fr: String,
    val hi: String,
    val hr: String,
    val id: String,
    val it: String,
    val ja: String,
    @SerializedName("ja-ro")
    val ja_ro: String,
    val ka: String,
    val ko: String,
    @SerializedName("ko-ro")
    val ko_ro: String,
    val ms: String,
    val pl: String,
    val pt: String,
    @SerializedName("pt-br")
    val pt_br: String,
    val ru: String,
    val th: String,
    val tr: String,
    val uk: String,
    val vi: String,
    val zh: String,
    @SerializedName("zh-hk")
    val zh_hk: String,
    @SerializedName("zh-ro")
    val zh_ro: String
)