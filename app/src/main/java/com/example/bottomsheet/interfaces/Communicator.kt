package com.example.bottomsheet.interfaces

import android.graphics.Bitmap
import android.net.Uri

interface Communicator {
    fun capturedImage(image: Bitmap)
    fun selectedImage(image: Uri)
}