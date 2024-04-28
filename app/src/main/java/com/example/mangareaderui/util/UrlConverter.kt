package com.example.mangareaderui.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import coil.ImageLoader
import coil.request.ErrorResult
import coil.request.ImageRequest
import coil.request.SuccessResult

class UrlConverter {
    suspend fun urlToBitmap(
        context: Context,
        imageUrl: String,
        bitmapResult: BitmapResult
    ) {
        val loader = ImageLoader(context)

        val request = ImageRequest.Builder(context)
            .data(imageUrl)
            .allowHardware(false)
            .build()

        val result = loader.execute(request)

        if (result is SuccessResult) {
            bitmapResult.onSuccess(bitmap = (result.drawable as BitmapDrawable).bitmap)
        } else if (result is ErrorResult) {
            bitmapResult.onFailure(bitmap = null)
        }

    }
}

interface BitmapResult {
    fun onSuccess(bitmap: Bitmap)
    fun onFailure(bitmap: Bitmap?)
}