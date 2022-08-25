package com.bhsoft.ar3d.ui.main.camera_detect_activity.ml

import android.content.Intent
import android.opengl.Matrix
import android.widget.Toast
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.bhsoft.ar3d.ui.main.camera_detect_activity.common.helpers.DisplayRotationHelper
import com.bhsoft.ar3d.ui.main.camera_detect_activity.common.samplerender.SampleRender
import com.bhsoft.ar3d.ui.main.camera_detect_activity.common.samplerender.arcore.BackgroundRenderer
import com.bhsoft.ar3d.ui.main.camera_detect_activity.ml.classfication.DetectedObjectResult
import com.bhsoft.ar3d.ui.main.camera_detect_activity.ml.classfication.GoogleCloudVisionDetector
import com.bhsoft.ar3d.ui.main.camera_detect_activity.ml.classfication.MLKitObjectDetector
import com.bhsoft.ar3d.ui.main.camera_detect_activity.ml.classfication.ObjectDetector
import com.bhsoft.ar3d.ui.main.camera_detect_activity.ml.drag_object.DragObjectActivity
import com.bhsoft.ar3d.ui.main.camera_detect_activity.ml.render.LabelRender
import com.bhsoft.ar3d.ui.main.camera_detect_activity.ml.render.PointCloudRender
import com.google.ar.core.Anchor
import com.google.ar.core.Coordinates2d
import com.google.ar.core.Frame
import com.google.ar.core.TrackingState
import com.google.ar.core.exceptions.CameraNotAvailableException
import com.google.ar.core.exceptions.NotYetAvailableException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.util.*
class AppRenderer(val activity: Camera_Detect_Activity) : DefaultLifecycleObserver,
    SampleRender.Renderer, CoroutineScope by MainScope() {
    lateinit var view: MainActivityView
    val displayRotationHelper = DisplayRotationHelper(activity)
    lateinit var backgroundRenderer: BackgroundRenderer
    private val pointCloudRender = PointCloudRender()
    val labelRenderer = LabelRender()
    val viewMatrix = FloatArray(16)
    val projectionMatrix = FloatArray(16)
    val viewProjectionMatrix = FloatArray(16)
    val arLabeledAnchors = Collections.synchronizedList(mutableListOf<ARLabeledAnchor>())
    var scanButtonWasPressed = false
    val mlKitAnalyzer = MLKitObjectDetector(activity)
    val gcpAnalyzer = GoogleCloudVisionDetector(activity)
    var currentAnalyzer: ObjectDetector = gcpAnalyzer
    private var timer: Timer? = null
    private var checkDetecting = false

    override fun onResume(owner: LifecycleOwner) {
        displayRotationHelper.onResume()
    }

    override fun onPause(owner: LifecycleOwner) {
        displayRotationHelper.onPause()
    }

    fun bindView(view: MainActivityView) {
        this.view = view

        labelingObject(view)



        val gcpConfigured = gcpAnalyzer.credentials != null
        currentAnalyzer = if (gcpConfigured) gcpAnalyzer else mlKitAnalyzer

        if (!gcpConfigured) {
            showSnackbar("Google Cloud Vision isn't configured (see README). The Cloud ML switch will be disabled.")
        }

        view.scanButton.setOnClickListener {
            if(checkDetecting){
                checkDetecting = false
                labelingObject(view)
            }else{
                Toast.makeText(view.activity, "Scanning", Toast.LENGTH_SHORT).show()
            }
        }

        view.resetButton.setOnClickListener {
            if(!checkDetecting){
                checkDetecting = true
                timer!!.cancel()
                timer = null
                arLabeledAnchors.clear()
                hideSnackbar()
            }else {
                Toast.makeText(view.activity, "Clear", Toast.LENGTH_SHORT).show()
            }
        }

        view.btnAddObjectFromGallery.setOnClickListener {
            Toast.makeText(view.activity, "Add", Toast.LENGTH_SHORT).show()
            view.activity.startActivity(Intent(view.activity,DragObjectActivity::class.java))
        }
    }
    fun labelingObject(view: MainActivityView) {
        timer = Timer()
        timer!!.scheduleAtFixedRate(
            object : TimerTask() {
                override fun run() {
                    arLabeledAnchors.clear()
                    hideSnackbar()

                    scanButtonWasPressed = true
                    view.setScanningActive(true)
                    hideSnackbar()
                }
            }, 0, 2000
        )
    }

    override fun onSurfaceCreated(render: SampleRender?) {
        backgroundRenderer = BackgroundRenderer(render).apply {
            setUseDepthVisualization(render, false)
        }
        pointCloudRender.onSurfaceCreated(render!!)
        labelRenderer.onSurfaceCreated(render)
    }

    override fun onSurfaceChanged(render: SampleRender?, width: Int, height: Int) {
        displayRotationHelper.onSurfaceChanged(width, height)
    }

    var objectResults: List<DetectedObjectResult>? = null

    override fun onDrawFrame(render: SampleRender?) {
        val session = activity.arCoreSessionHelper.sessionCache ?: return
        session.setCameraTextureNames(intArrayOf(backgroundRenderer.colorTexture.textureId[0]))

        // Notify ARCore session that the view size changed so that the perspective matrix and
        // the video background can be properly adjusted.
        displayRotationHelper.updateSessionIfNeeded(session)

        val frame = try {
            session.update()
        } catch (e: CameraNotAvailableException) {
            showSnackbar("Camera not available. Try restarting the app.")
            return
        }

        backgroundRenderer.updateDisplayGeometry(frame)
        backgroundRenderer.drawBackground(render!!)

        // Get camera and projection matrices.
        val camera = frame.camera
        camera.getViewMatrix(viewMatrix, 0)
        camera.getProjectionMatrix(projectionMatrix, 0, 0.01f, 100.0f)
        Matrix.multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0)

        // Handle tracking failures.
        if (camera.trackingState != TrackingState.TRACKING) {
            return
        }

        // Draw point cloud.
        frame.acquirePointCloud().use { pointCloud ->
            pointCloudRender.drawPointCloud(render, pointCloud, viewProjectionMatrix)
        }

        // Frame.acquireCameraImage must be used on the GL thread.
        // Check if the button was pressed last frame to start processing the camera image.
        if (scanButtonWasPressed) {
            scanButtonWasPressed = false
            val cameraImage = frame.tryAcquireCameraImage()
            if (cameraImage != null) {
                // Call our ML model on an IO thread.
                launch(Dispatchers.IO) {
                    val cameraId = session.cameraConfig.cameraId
                    val imageRotation =
                        displayRotationHelper.getCameraSensorToDisplayRotation(cameraId)
                    objectResults = currentAnalyzer.analyze(cameraImage, imageRotation)
                    cameraImage.close()
                }
            }
        }

        /** If results were completed this frame, create [Anchor]s from model results. */
        val objects = objectResults
        if (objects != null) {
            objectResults = null
            val anchors = objects.mapNotNull { obj ->
                obj.label
                val (atX, atY) = obj.centerCoordinate
                val anchor =
                    createAnchor(atX.toFloat(), atY.toFloat(), frame) ?: return@mapNotNull null
                ARLabeledAnchor(anchor, obj.label)
            }
            arLabeledAnchors.addAll(anchors)
            view.post {
//        view.resetButton.isEnabled = arLabeledAnchors.isNotEmpty()
                view.setScanningActive(false)
//                when {
//                    objects.isEmpty() && currentAnalyzer == mlKitAnalyzer && !mlKitAnalyzer.hasCustomModel() ->
//                        showSnackbar(
//                            "Default ML Kit classification model returned no results. " +
//                                    "For better classification performance, see the README to configure a custom model."
//                        )
//                    objects.isEmpty() ->
//                        showSnackbar("Classification model returned no results.")
//                    anchors.size != objects.size ->
//                        showSnackbar(
//                            "Objects were classified, but could not be attached to an anchor. " +
//                                    "Try moving your device around to obtain a better understanding of the environment."
//                        )
//                }
            }
        }

        // Draw labels at their anchor position.
            for (arDetectedObject in arLabeledAnchors) {
                val anchor = arDetectedObject.anchor
                if (anchor.trackingState != TrackingState.TRACKING) continue
                labelRenderer.draw(
                    render,
                    viewProjectionMatrix,
                    anchor.pose,
                    camera.pose,
                    arDetectedObject.label
                )
            }
    }

    fun Frame.tryAcquireCameraImage() = try {
        acquireCameraImage()
    } catch (e: NotYetAvailableException) {
        null
    } catch (e: Throwable) {
        throw e
    }

    private fun showSnackbar(message: String): Unit =
        activity.view.snackbarHelper.showError(activity, message)

    private fun hideSnackbar() = activity.view.snackbarHelper.hide(activity)

    private val convertFloats = FloatArray(4)
    private val convertFloatsOut = FloatArray(4)

    /** Create an anchor using (x, y) coordinates in the [Coordinates2d.IMAGE_PIXELS] coordinate space. */
    fun createAnchor(xImage: Float, yImage: Float, frame: Frame): Anchor? {
        // IMAGE_PIXELS -> VIEW
        convertFloats[0] = xImage
        convertFloats[1] = yImage
        frame.transformCoordinates2d(
            Coordinates2d.IMAGE_PIXELS,
            convertFloats,
            Coordinates2d.VIEW,
            convertFloatsOut
        )

        // Conduct a hit test using the VIEW coordinates
        val hits = frame.hitTest(convertFloatsOut[0], convertFloatsOut[1])
        val result = hits.getOrNull(0) ?: return null
        return result.trackable.createAnchor(result.hitPose)
    }
}

data class ARLabeledAnchor(val anchor: Anchor, val label: String)