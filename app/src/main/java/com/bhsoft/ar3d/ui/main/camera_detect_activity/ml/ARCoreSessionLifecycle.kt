
package com.bhsoft.ar3d.ui.main.camera_detect_activity.ml

import android.app.Activity
import android.widget.Toast
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.bhsoft.ar3d.ui.main.camera_detect_activity.CameraPermissionHelper
import com.google.ar.core.ArCoreApk
import com.google.ar.core.Session
import com.google.ar.core.exceptions.CameraNotAvailableException


class ARCoreSessionLifecycleHelper(
  val activity: Activity,
  val features: Set<Session.Feature> = setOf()
) : DefaultLifecycleObserver {
  var installRequested = false
  var sessionCache: Session? = null
    private set

  var exceptionCallback: ((Exception) -> Unit)? = null

  var beforeSessionResume: ((Session) -> Unit)? = null

  // Creates a session. If ARCore is not installed, an installation will be requested.
  fun tryCreateSession(): Session? {
    // Request an installation if necessary.
    when (ArCoreApk.getInstance().requestInstall(activity, !installRequested)!!) {
      ArCoreApk.InstallStatus.INSTALL_REQUESTED -> {
        installRequested = true
        // tryCreateSession will be called again, so we return null for now.
        return null
      }
      ArCoreApk.InstallStatus.INSTALLED -> {
        // Left empty; nothing needs to be done
      }
    }

    // Create a session if ARCore is installed.
    return try {
      Session(activity, features)
    } catch (e: Exception) {
      exceptionCallback?.invoke(e)
      null
    }
  }

  override fun onResume(owner: LifecycleOwner) {
    if (!CameraPermissionHelper.hasCameraPermission(activity)) {
        CameraPermissionHelper.requestCameraPermission(activity)
      return
    }

    val session = tryCreateSession() ?: return
    try {
      beforeSessionResume?.invoke(session)
      session.resume()
      sessionCache = session
    } catch (e: CameraNotAvailableException) {
      exceptionCallback?.invoke(e)
    }
  }

  override fun onPause(owner: LifecycleOwner) {
    sessionCache?.pause()
  }

  override fun onDestroy(owner: LifecycleOwner) {
    sessionCache?.close()
    sessionCache = null
  }

  fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<out String>,
    grantResults: IntArray
  ) {
    if (!CameraPermissionHelper.hasCameraPermission(activity)) {
      Toast.makeText(activity, "Camera permission is needed to run this application", Toast.LENGTH_LONG)
        .show()
      if (!CameraPermissionHelper.shouldShowRequestPermissionRationale(activity)) {
        // Permission denied with checking "Do not ask again".
          CameraPermissionHelper.launchPermissionSettings(activity)
      }
      activity.finish()
    }
  }
}
