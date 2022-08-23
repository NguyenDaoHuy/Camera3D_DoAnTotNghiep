package com.bhsoft.ar3d.ui.main.camera_detect_activity.ml

import android.opengl.GLSurfaceView
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.bhsoft.ar3d.R
import com.bhsoft.ar3d.ui.main.camera_detect_activity.common.helpers.SnackbarHelper
import com.bhsoft.ar3d.ui.main.camera_detect_activity.common.samplerender.SampleRender
import com.google.ar.sceneform.ux.ArFragment

/**
 * Wraps [R.layout.activity_main] and controls lifecycle operations for [GLSurfaceView].
 */
class MainActivityView(val activity: Camera_Detect_Activity, renderer: AppRenderer) :
    DefaultLifecycleObserver {
    val root = View.inflate(activity, R.layout.activity_camera_detect, null)
    val surfaceView = root.findViewById<GLSurfaceView>(R.id.surfaceview).apply {
        SampleRender(this, renderer, activity.assets)
    }

    // val useCloudMlSwitch = root.findViewById<SwitchCompat>(R.id.useCloudMlSwitch)
    val relaView = root.findViewById<RelativeLayout>(R.id.relative)
    val scanButton = root.findViewById<TextView>(R.id.scanButton)
    val resetButton = root.findViewById<TextView>(R.id.clearButton)
    val btnAddObjectFromGallery = root.findViewById<RelativeLayout>(R.id.btnAddObjectFromGallery)
   // val arFragment : ArFragment = activity.supportFragmentManager.findFragmentById(R.id.fragment) as ArFragment
  //  val rcvObject3d = root.findViewById<RecyclerView>(R.id.rcvObject3d)
   // val btnRemove = root.findViewById<Button>(R.id.remove)
    val snackbarHelper = SnackbarHelper().apply {
        setParentView(root.findViewById(R.id.coordinatorLayout))
        setMaxLines(6)
    }


    override fun onResume(owner: LifecycleOwner) {
        surfaceView.onResume()
    }

    override fun onPause(owner: LifecycleOwner) {
        surfaceView.onPause()
    }

    fun post(action: Runnable) = root.post(action)

    /**
     * Toggles the scan button depending on if scanning is in progress.
     */
    fun setScanningActive(active: Boolean) = when (active) {
        true -> {
//      scanButton.isEnabled = false
//      scanButton.setText("Scanning")
        }
        false -> {
//      scanButton.isEnabled = true
//      scanButton.setText("Scanning")
        }
    }
}