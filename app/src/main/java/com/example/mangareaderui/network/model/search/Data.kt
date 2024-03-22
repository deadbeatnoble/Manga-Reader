package com.example.retrofit.network.model.searchedmanga

data class Data(
    val attributes: Attributes,
    val id: String,
    val relationships: List<Relationship>,
    val type: String
)