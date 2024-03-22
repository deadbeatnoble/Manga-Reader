package com.example.retrofit.network.model.searchedmanga

data class Tag(
    val attributes: AttributesTag,
    val id: String,
    val relationships: List<Any>,
    val type: String
)