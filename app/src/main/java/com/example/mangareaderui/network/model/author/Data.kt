package com.example.mangareaderui.network.model.author

data class Data(
    val attributes: Attributes,
    val id: String,
    val relationships: List<Relationship>,
    val type: String
)