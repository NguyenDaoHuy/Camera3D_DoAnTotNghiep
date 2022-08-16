
package com.bhsoft.ar3d.ui.main.camera_detect_activity.ml.render

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.opengl.GLES30
import android.os.Build
import androidx.annotation.RequiresApi
import com.bhsoft.ar3d.ui.main.camera_detect_activity.common.samplerender.SampleRender
import com.bhsoft.ar3d.ui.main.camera_detect_activity.common.samplerender.Texture
import java.nio.ByteBuffer

/**
 * Generates and caches GL textures for label names.
 */
class TextTextureCache {
  companion object {
    private const val TAG = "TextTextureCache"
  }

  private val cacheMap = mutableMapOf<String, Texture>()

  /**
   * Get a texture for a given string. If that string hasn't been used yet, create a texture for it
   * and cache the result.
   */
  @RequiresApi(Build.VERSION_CODES.N)
  fun get(render: SampleRender, string: String): Texture {
    return cacheMap.computeIfAbsent(string) {
      generateTexture(render, string)
    }
  }

  private fun generateTexture(render: SampleRender, string: String): Texture {
    val texture = Texture(render, Texture.Target.TEXTURE_2D, Texture.WrapMode.CLAMP_TO_EDGE)

    val bitmap = generateBitmapFromString(string)
    val buffer = ByteBuffer.allocateDirect(bitmap.byteCount)
    bitmap.copyPixelsToBuffer(buffer)
    buffer.rewind()

    GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texture.textureId[0])
    GLES30.glTexImage2D(
      GLES30.GL_TEXTURE_2D,
      0,
      GLES30.GL_RGBA8,
      bitmap.width,
      bitmap.height,
      0,
      GLES30.GL_RGBA,
      GLES30.GL_UNSIGNED_BYTE,
      buffer
    )
    GLES30.glGenerateMipmap(GLES30.GL_TEXTURE_2D)

    return texture
  }

  val textPaint = Paint().apply {
    textSize = 26f
    setARGB(0xff, 0xea, 0x43, 0x35)
    style = Paint.Style.FILL
    isAntiAlias = true
    textAlign = Paint.Align.CENTER
    typeface = Typeface.DEFAULT_BOLD
    strokeWidth = 2f
  }

  val strokePaint = Paint(textPaint).apply {
    setARGB(0xff, 0x00, 0x00, 0x00)
    style = Paint.Style.STROKE
  }

  private fun generateBitmapFromString(string: String): Bitmap {
    val w = 256
    val h = 256
    return Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888).apply {
      eraseColor(0)

      Canvas(this).apply {
        drawText(string, w / 2f, h / 2f, strokePaint)

        drawText(string, w / 2f, h / 2f, textPaint)
      }
    }
  }
}