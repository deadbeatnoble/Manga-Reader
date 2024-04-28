package com.example.mangareaderui.domain.model

import android.graphics.Bitmap


data class Page(
    var image: Bitmap?,
    var num: Int?,
    var state: PageState,
    val url: String?
)

enum class PageState {
    LOADING,
    LOADED,
    FAILED
}
