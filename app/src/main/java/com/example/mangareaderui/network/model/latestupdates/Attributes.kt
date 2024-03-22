package com.example.retrofit.network.model.latestupdates

data class Attributes(
    val chapter: String,
    val createdAt: String,
    val externalUrl: String,
    val pages: Int,
    val publishAt: String,
    val readableAt: String,
    val title: String,
    val translatedLanguage: String,
    val updatedAt: String,
    val version: Int,
    val volume: String
)