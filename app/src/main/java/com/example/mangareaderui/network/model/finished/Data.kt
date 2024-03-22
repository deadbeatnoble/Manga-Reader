package com.example.retrofit.network.model.finished

import com.example.retrofit.network.model.mangadetail.Attributes

data class Data(
    val attributes: Attributes,
    val id: String,
    val relationships: List<Relationship>,
    val type: String
)