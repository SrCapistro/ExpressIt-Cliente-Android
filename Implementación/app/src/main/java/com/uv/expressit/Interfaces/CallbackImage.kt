package com.uv.expressit.Interfaces

import android.graphics.Bitmap

interface CallbackImage {
    fun onSuccessResponse(image: Bitmap)
}