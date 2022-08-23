package com.bhsoft.ar3d.ui.main.camera_detect_activity.ml.drag_object
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bhsoft.ar3d.R
import com.bhsoft.ar3d.databinding.ActivityDragObjectBinding

class DragObjectActivity : AppCompatActivity(){
    private var binding : ActivityDragObjectBinding?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_drag_object)

    }
}