package com.example.retrofit.network.model.mangadetail

import com.google.gson.annotations.SerializedName

data class Title(
    val en: String?,
    val ja: String?,
    @SerializedName("ja-ro")
    val ja_ro: String?
)