package com.example.retrofit.network.model.searchedmanga

import com.google.gson.annotations.SerializedName

data class Description(
    val ar: String,
    val az: String,
    val de: String,
    val el: String,
    val en: String,
    val es: String,
    @SerializedName("es-la")
    val es_la: String,
    val fa: String,
    val fi: String,
    val fr: String,
    val id: String,
    val it: String,
    val ja: String,
    val ko: String,
    val pl: String,
    val pt: String,
    @SerializedName("pt-br")
    val pt_br: String,
    val ru: String,
    val sv: String,
    val th: String,
    val vi: String,
    @SerializedName("zh-hk")
    val zh_hk: String
)