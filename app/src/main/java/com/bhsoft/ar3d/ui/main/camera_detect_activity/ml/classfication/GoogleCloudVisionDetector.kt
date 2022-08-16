
package com.bhsoft.ar3d.ui.main.camera_detect_activity.ml.classfication

import android.media.Image
import com.bhsoft.ar3d.ui.main.camera_detect_activity.ml.Camera_Detect_Activity
import com.bhsoft.ar3d.ui.main.camera_detect_activity.ml.classfication.ultis.ImageUtils
import com.bhsoft.ar3d.ui.main.camera_detect_activity.ml.classfication.ultis.ImageUtils.toByteArray
import com.bhsoft.ar3d.ui.main.camera_detect_activity.ml.classfication.ultis.VertexUtils.calculateAverage
import com.bhsoft.ar3d.ui.main.camera_detect_activity.ml.classfication.ultis.VertexUtils.rotateCoordinates
import com.bhsoft.ar3d.ui.main.camera_detect_activity.ml.classfication.ultis.VertexUtils.toAbsoluteCoordinates
import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.vision.v1.AnnotateImageRequest
import com.google.cloud.vision.v1.Feature
import com.google.cloud.vision.v1.ImageAnnotatorClient
import com.google.cloud.vision.v1.ImageAnnotatorSettings
import com.google.protobuf.ByteString
import com.google.cloud.vision.v1.Image as GCVImage

/**
 * https://cloud.google.com/vision/docs/object-localizer
 *
 * Finds detected objects ([DetectedObjectResult]s) given an [android.media.Image].
 */
class GoogleCloudVisionDetector(val activity: Camera_Detect_Activity) : ObjectDetector(activity) {
    companion object {
        val TAG = "GoogleCloudVisionDetector"
    }

    val credentials = try {
        // Providing GCP credentials is not mandatory for this app, so the existence of R.raw.credentials
        // is not guaranteed. Instead, use getIdentifier to determine an optional resource.
        val res = activity.resources.getIdentifier("credentials", "raw", activity.packageName)
        if (res == 0) error("Missing GCP credentials in res/raw/credentials.json.")
        GoogleCredentials.fromStream(activity.resources.openRawResource(res))
    } catch (e: Exception) {
        null
    }
    val settings = ImageAnnotatorSettings.newBuilder().setCredentialsProvider { credentials }.build()
    val vision = ImageAnnotatorClient.create(settings)

    override suspend fun analyze(image: Image, imageRotation: Int): List<DetectedObjectResult> {
        // `image` is in YUV (https://developers.google.com/ar/reference/java/com/google/ar/core/Frame#acquireCameraImage()),
        val convertYuv = convertYuv(image)

        // The model performs best on upright images, so rotate it.
        val rotatedImage = ImageUtils.rotateBitmap(convertYuv, imageRotation)

        // Perform request on Google Cloud Vision APIs.
        val request = createAnnotateImageRequest(rotatedImage.toByteArray())
        val response = vision.batchAnnotateImages(listOf(request))

        // Process result and map to DetectedObjectResult.
        val objectAnnotationsResult = response.responsesList.first().localizedObjectAnnotationsList
        return objectAnnotationsResult.map {
            val center = it.boundingPoly.normalizedVerticesList.calculateAverage()
            val absoluteCoordinates = center.toAbsoluteCoordinates(rotatedImage.width, rotatedImage.height)
            val rotatedCoordinates = absoluteCoordinates.rotateCoordinates(rotatedImage.width, rotatedImage.height, imageRotation)
            DetectedObjectResult(it.score, it.name, rotatedCoordinates)
        }
    }

    /**
     * Creates an [AnnotateImageRequest] from image's byte array.
     *
     * https://cloud.google.com/vision/docs/reference/rest/v1/AnnotateImageRequest
     */
    private fun createAnnotateImageRequest(imageBytes: ByteArray): AnnotateImageRequest {
        // GCVImage is a typealias for com.google.cloud.vision's Image, needed to differentiate from android.media.Image
        val image = GCVImage.newBuilder().setContent(ByteString.copyFrom(imageBytes))
        val features = Feature.newBuilder().setType(Feature.Type.OBJECT_LOCALIZATION)
        return AnnotateImageRequest.newBuilder()
                .setImage(image)
                .addFeatures(features)
                .build()
    }
}