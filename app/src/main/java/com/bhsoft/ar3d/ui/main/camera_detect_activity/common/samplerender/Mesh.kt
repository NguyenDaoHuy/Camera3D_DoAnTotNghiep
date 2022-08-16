
package com.bhsoft.ar3d.ui.main.camera_detect_activity.common.samplerender

import android.opengl.GLES30
import de.javagl.obj.ObjData
import de.javagl.obj.ObjReader
import de.javagl.obj.ObjUtils
import java.io.Closeable
import java.io.IOException

class Mesh(
    render: SampleRender?,
    primitiveMode: PrimitiveMode,
    indexBuffer: IndexBuffer?,
    vertexBuffers: Array<VertexBuffer>?
) : Closeable {
    enum class PrimitiveMode(  /* package-private */
        val glesEnum: Int
    ) {
        POINTS(GLES30.GL_POINTS), LINE_STRIP(GLES30.GL_LINE_STRIP), LINE_LOOP(GLES30.GL_LINE_LOOP), LINES(
            GLES30.GL_LINES
        ),
        TRIANGLE_STRIP(GLES30.GL_TRIANGLE_STRIP), TRIANGLE_FAN(GLES30.GL_TRIANGLE_FAN), TRIANGLES(
            GLES30.GL_TRIANGLES
        );
    }

    private val vertexArrayId = intArrayOf(0)
    private val primitiveMode: PrimitiveMode
    private val indexBuffer: IndexBuffer?
    private val vertexBuffers: Array<VertexBuffer>
    override fun close() {
        if (vertexArrayId[0] != 0) {
            GLES30.glDeleteVertexArrays(1, vertexArrayId, 0)
        }
    }


    fun lowLevelDraw() {
        check(vertexArrayId[0] != 0) { "Tried to draw a freed Mesh" }
        GLES30.glBindVertexArray(vertexArrayId[0])
        if (indexBuffer == null) {
            // Sanity check for debugging
            val numberOfVertices = vertexBuffers[0].numberOfVertices
            for (i in 1 until vertexBuffers.size) {
                check(vertexBuffers[i].numberOfVertices == numberOfVertices) { "Vertex buffers have mismatching numbers of vertices" }
            }
            GLES30.glDrawArrays(primitiveMode.glesEnum, 0, numberOfVertices)
        } else {
            GLES30.glDrawElements(
                primitiveMode.glesEnum, indexBuffer.size, GLES30.GL_UNSIGNED_INT, 0
            )
        }
    }

    companion object {
        private val TAG = Mesh::class.java.simpleName

        @Throws(IOException::class)
        fun createFromAsset(render: SampleRender, assetFileName: String?): Mesh {
            render.assets.open(assetFileName!!).use { inputStream ->
                val obj = ObjUtils.convertToRenderable(ObjReader.read(inputStream))

                // Obtain the data from the OBJ, as direct buffers:
                val vertexIndices = ObjData.getFaceVertexIndices(obj,  /*numVerticesPerFace=*/3)
                val localCoordinates = ObjData.getVertices(obj)
                val textureCoordinates = ObjData.getTexCoords(obj,  /*dimensions=*/2)
                val normals = ObjData.getNormals(obj)
                val vertexBuffers = arrayOf(
                    VertexBuffer(render, 3, localCoordinates),
                    VertexBuffer(render, 2, textureCoordinates),
                    VertexBuffer(render, 3, normals)
                )
                val indexBuffer = IndexBuffer(render, vertexIndices)
                return Mesh(render, PrimitiveMode.TRIANGLES, indexBuffer, vertexBuffers)
            }
        }
    }

    init {
        require(!(vertexBuffers == null || vertexBuffers.size == 0)) { "Must pass at least one vertex buffer" }
        this.primitiveMode = primitiveMode
        this.indexBuffer = indexBuffer
        this.vertexBuffers = vertexBuffers
        try {
            // Create vertex array
            GLES30.glGenVertexArrays(1, vertexArrayId, 0)

            // Bind vertex array
            GLES30.glBindVertexArray(vertexArrayId[0])
            if (indexBuffer != null) {
                GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER, indexBuffer.bufferId)
            }
            for (i in vertexBuffers.indices) {
                // Bind each vertex buffer to vertex array
                GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vertexBuffers[i].bufferId)
                GLES30.glVertexAttribPointer(
                    i, vertexBuffers[i].numberOfEntriesPerVertex, GLES30.GL_FLOAT, false, 0, 0
                )
                GLES30.glEnableVertexAttribArray(i)
            }
        } catch (t: Throwable) {
            close()
            throw t
        }
    }
}