package com.bhsoft.ar3d.data.model

import android.graphics.Bitmap
import java.io.Serializable

data class Image(var name: String,
                 var bitmap : Bitmap) : Serializable