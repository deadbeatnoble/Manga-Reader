package com.example.mangareaderui.network.model.popular

import com.example.retrofit.network.model.mangadetail.Attributes
import com.example.retrofit.network.model.mangadetail.Relationship

data class Data(
    val attributes: Attributes,
    val id: String,
    val relationships: List<Relationship>,
    val type: String
)