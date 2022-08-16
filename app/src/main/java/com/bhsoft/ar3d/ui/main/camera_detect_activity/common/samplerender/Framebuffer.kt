
package com.bhsoft.ar3d.ui.main.camera_detect_activity.common.samplerender

import android.opengl.GLES30
import java.io.Closeable

/** A framebuffer associated with a texture.  */
class Framebuffer(render: SampleRender?, width: Int, height: Int) : Closeable {
    private val framebufferId = intArrayOf(0)
    /** Returns the color texture associated with this framebuffer.  */
    var colorTexture: Texture? = null
    /** Returns the depth texture associated with this framebuffer.  */
    var depthTexture: Texture? = null
    /** Returns the width of the framebuffer.  */
    var width = -1
        private set
    /** Returns the height of the framebuffer.  */
    var height = -1
        private set

    override fun close() {
        if (framebufferId[0] != 0) {
            GLES30.glDeleteFramebuffers(1, framebufferId, 0)
            framebufferId[0] = 0
        }
        colorTexture!!.close()
        depthTexture!!.close()
    }

    /** Resizes the framebuffer to the given dimensions.  */
    fun resize(width: Int, height: Int) {
        if (this.width == width && this.height == height) {
            return
        }
        this.width = width
        this.height = height

        // Color texture
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, colorTexture!!.textureId[0])
        GLES30.glTexImage2D(
            GLES30.GL_TEXTURE_2D,  /*level=*/
            0,
            GLES30.GL_RGBA,
            width,
            height,  /*border=*/
            0,
            GLES30.GL_RGBA,
            GLES30.GL_UNSIGNED_BYTE,  /*pixels=*/
            null
        )

        // Depth texture
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, depthTexture!!.textureId[0])
        GLES30.glTexImage2D(
            GLES30.GL_TEXTURE_2D,  /*level=*/
            0,
            GLES30.GL_DEPTH_COMPONENT32F,
            width,
            height,  /*border=*/
            0,
            GLES30.GL_DEPTH_COMPONENT,
            GLES30.GL_FLOAT,  /*pixels=*/
            null
        )
    }

    /* package-private */
    fun getFramebufferId(): Int {
        return framebufferId[0]
    }

    companion object {
        private val TAG = Framebuffer::class.java.simpleName
    }

    init {
        try {
            colorTexture = Texture(
                render,
                Texture.Target.TEXTURE_2D,
                Texture.WrapMode.CLAMP_TO_EDGE,  /*useMipmaps=*/
                false
            )
            depthTexture = Texture(
                render,
                Texture.Target.TEXTURE_2D,
                Texture.WrapMode.CLAMP_TO_EDGE,  /*useMipmaps=*/
                false
            )

            // Set parameters of the depth texture so that it's readable by shaders.
            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, depthTexture!!.getTextureId())
            GLES30.glTexParameteri(
                GLES30.GL_TEXTURE_2D,
                GLES30.GL_TEXTURE_COMPARE_MODE,
                GLES30.GL_NONE
            )
            GLES30.glTexParameteri(
                GLES30.GL_TEXTURE_2D,
                GLES30.GL_TEXTURE_MIN_FILTER,
                GLES30.GL_NEAREST
            )
            GLES30.glTexParameteri(
                GLES30.GL_TEXTURE_2D,
                GLES30.GL_TEXTURE_MAG_FILTER,
                GLES30.GL_NEAREST
            )

            // Set initial dimensions.
            resize(width, height)

            // Create framebuffer object and bind to the color and depth textures.
            GLES30.glGenFramebuffers(1, framebufferId, 0)
            GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, framebufferId[0])
            GLES30.glFramebufferTexture2D(
                GLES30.GL_FRAMEBUFFER,
                GLES30.GL_COLOR_ATTACHMENT0,
                GLES30.GL_TEXTURE_2D,
                colorTexture!!.getTextureId(),  /*level=*/
                0
            )
            GLES30.glFramebufferTexture2D(
                GLES30.GL_FRAMEBUFFER,
                GLES30.GL_DEPTH_ATTACHMENT,
                GLES30.GL_TEXTURE_2D,
                depthTexture!!.getTextureId(),  /*level=*/
                0
            )
            val status = GLES30.glCheckFramebufferStatus(GLES30.GL_FRAMEBUFFER)
            check(status == GLES30.GL_FRAMEBUFFER_COMPLETE) { "Framebuffer construction not complete: code $status" }
        } catch (t: Throwable) {
            close()
            throw t
        }
    }
}