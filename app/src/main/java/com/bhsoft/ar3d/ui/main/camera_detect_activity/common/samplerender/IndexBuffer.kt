
package com.bhsoft.ar3d.ui.main.camera_detect_activity.common.samplerender

import android.opengl.GLES30
import java.io.Closeable
import java.nio.IntBuffer


class IndexBuffer(render: SampleRender?, entries: IntBuffer?) : Closeable {
    private val buffer: GpuBuffer

    fun set(entries: IntBuffer?) {
        buffer.set(entries)
    }

    override fun close() {
        buffer.free()
    }

    /* package-private */
    val bufferId: Int
        get() = buffer.getBufferId()

    /* package-private */
    val size: Int
        get() = buffer.size
    init {
        buffer = GpuBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER, GpuBuffer.INT_SIZE, entries)
    }
}