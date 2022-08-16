
package com.bhsoft.ar3d.ui.main.camera_detect_activity.ml.render

import com.google.ar.core.PointCloud
import com.bhsoft.ar3d.ui.main.camera_detect_activity.common.samplerender.Mesh
import com.bhsoft.ar3d.ui.main.camera_detect_activity.common.samplerender.SampleRender
import com.bhsoft.ar3d.ui.main.camera_detect_activity.common.samplerender.Shader
import com.bhsoft.ar3d.ui.main.camera_detect_activity.common.samplerender.VertexBuffer

class PointCloudRender {
  lateinit var pointCloudVertexBuffer: VertexBuffer
  lateinit var pointCloudMesh: Mesh
  lateinit var pointCloudShader: Shader

  var lastPointCloudTimestamp: Long = 0

  fun onSurfaceCreated(render: SampleRender) {
    // Point cloud
    pointCloudShader = Shader.createFromAssets(
      render, "shaders/point_cloud.vert", "shaders/point_cloud.frag",  /*defines=*/null
    )
      .setVec4(
        "u_Color", floatArrayOf(31.0f / 255.0f, 188.0f / 255.0f, 210.0f / 255.0f, 1.0f)
      )
      .setFloat("u_PointSize", 5.0f)

    // four entries per vertex: X, Y, Z, confidence
    pointCloudVertexBuffer = VertexBuffer(render, 4, null)
    val pointCloudVertexBuffers = arrayOf(pointCloudVertexBuffer)
    pointCloudMesh = Mesh(
      render, Mesh.PrimitiveMode.POINTS, null, pointCloudVertexBuffers
    )
  }

  fun drawPointCloud(
      render: SampleRender,
      pointCloud: PointCloud,
      modelViewProjectionMatrix: FloatArray
  ) {
    if (pointCloud.timestamp > lastPointCloudTimestamp) {
      pointCloudVertexBuffer.set(pointCloud.points)
      lastPointCloudTimestamp = pointCloud.timestamp
    }
    pointCloudShader.setMat4("u_ModelViewProjection", modelViewProjectionMatrix)
    render.draw(pointCloudMesh, pointCloudShader)
  }
}