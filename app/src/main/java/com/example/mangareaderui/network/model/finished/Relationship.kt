package com.example.retrofit.network.model.finished

import com.example.retrofit.network.model.mangadetail.Attributes

data class Relationship(
    val attributes: Attributes,
    val id: String,
    val related: String,
    val type: String
)