package com.example.bottomsheet.interfaces

import android.graphics.Bitmap
import android.net.Uri

interface Communicator {
    fun onCapturedImage(image: Bitmap)
    fun onSelectedImage(image: Uri)
}