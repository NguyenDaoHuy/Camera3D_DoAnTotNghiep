
package com.bhsoft.ar3d.ui.main.camera_detect_activity.common.samplerender

import android.content.res.AssetManager
import android.opengl.GLES30
import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/** A SampleRender context.  */
class SampleRender(
    glSurfaceView: GLSurfaceView, renderer: Renderer, /* package-private */
    val assets: AssetManager
) {
    private var viewportWidth = 1
    private var viewportHeight = 1
    @JvmOverloads
    fun draw(mesh: Mesh, shader: Shader, framebuffer: Framebuffer? =  /*framebuffer=*/null) {
        useFramebuffer(framebuffer)
        shader.lowLevelUse()
        mesh.lowLevelDraw()
    }


    fun clear(framebuffer: Framebuffer?, r: Float, g: Float, b: Float, a: Float) {
        useFramebuffer(framebuffer)
        GLES30.glClearColor(r, g, b, a)
        GLES30.glDepthMask(true)
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT or GLES30.GL_DEPTH_BUFFER_BIT)

    }

    interface Renderer {

        fun onSurfaceCreated(render: SampleRender?)


        fun onSurfaceChanged(render: SampleRender?, width: Int, height: Int)

        fun onDrawFrame(render: SampleRender?)
    }

    private fun useFramebuffer(framebuffer: Framebuffer?) {
        val framebufferId: Int
        val viewportWidth: Int
        val viewportHeight: Int
        if (framebuffer == null) {
            framebufferId = 0
            viewportWidth = this.viewportWidth
            viewportHeight = this.viewportHeight
        } else {
            framebufferId = framebuffer.getFramebufferId()
            viewportWidth = framebuffer.width
            viewportHeight = framebuffer.height
        }
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, framebufferId)
        GLES30.glViewport(0, 0, viewportWidth, viewportHeight)
    }

    companion object {
        private val TAG = SampleRender::class.java.simpleName
    }

    /**
     * Constructs a SampleRender object and instantiates GLSurfaceView parameters.
     *
     * @param glSurfaceView Android GLSurfaceView
     * @param renderer Renderer implementation to receive callbacks
     * @param assetManager AssetManager for loading Android resources
     */
    init {
        glSurfaceView.preserveEGLContextOnPause = true
        glSurfaceView.setEGLContextClientVersion(3)
        glSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0)
        glSurfaceView.setRenderer(
            object : GLSurfaceView.Renderer {
                override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
                    GLES30.glEnable(GLES30.GL_BLEND)
                    renderer.onSurfaceCreated(this@SampleRender)
                }

                override fun onSurfaceChanged(gl: GL10, w: Int, h: Int) {
                    viewportWidth = w
                    viewportHeight = h
                    renderer.onSurfaceChanged(this@SampleRender, w, h)
                }

                override fun onDrawFrame(gl: GL10) {
                    clear( /*framebuffer=*/null, 0f, 0f, 0f, 1f)
                    renderer.onDrawFrame(this@SampleRender)
                }
            })
        glSurfaceView.renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
        glSurfaceView.setWillNotDraw(false)
    }
}