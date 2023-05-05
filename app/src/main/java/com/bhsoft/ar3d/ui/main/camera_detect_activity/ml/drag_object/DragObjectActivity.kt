package com.bhsoft.ar3d.ui.main.camera_detect_activity.ml.drag_object

import android.content.ClipData
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.util.Log
import android.view.DragEvent
import android.view.PixelCopy
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import com.bhsoft.ar3d.R
import com.bhsoft.ar3d.databinding.ActivityDragObjectBinding
import com.bhsoft.ar3d.ui.fragment.camera_fragment.Constants
import com.bhsoft.ar3d.ui.utils.Utility
import com.google.ar.core.Anchor
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.ArSceneView
import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.*
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*


class DragObjectActivity : AppCompatActivity(), View.OnClickListener,View.OnLongClickListener {
    private var binding : ActivityDragObjectBinding?=null
    private var arFragment : ArFragment?=null
    private var tableRenderable : ModelRenderable?=null
    private var chairRenderable : ModelRenderable?=null
    private var lampRenderable : ModelRenderable?=null
    private var bookShelfRenderable : ModelRenderable?=null
    private var tvRenderable : ModelRenderable?=null
    private var pianoRenderable : ModelRenderable?=null
    private var mayGiatRenderable : ModelRenderable?=null
    private var officeChairRenderable : ModelRenderable?=null
    private var bedRenderable : ModelRenderable?=null
    private var monitorRenderable : ModelRenderable?=null
    private var ironManRenderable : ModelRenderable?=null
    private var sofaRenderable : ModelRenderable?=null
    private var drawerbedRenderable : ModelRenderable?=null
    private var mesapcRenderable : ModelRenderable?=null
    private var macbookRenderable : ModelRenderable?=null
    private var selected = 1
    private var arrayView: Array<View>?=null
    private var checkStatus = true
    private var widthLineRender: ModelRenderable? = null
    private var heightLineRender: ModelRenderable? = null
    private lateinit var viewRenderable: ViewRenderable
    private var pointRender: ModelRenderable? = null
    private var aimRender: ModelRenderable? = null
    private val currentAnchorNode = ArrayList<AnchorNode>()
    private var node2Pos: Vector3? = null
    private var node1Pos: Vector3? = null
    private var difference: Vector3? = null
    private var totalLength = 0f
    private lateinit var distanceInMeters: CardView
    private val currentAnchor = ArrayList<Anchor?>()
    private val labelArray: ArrayList<AnchorNode> = ArrayList()
    private var listLine: ArrayList<AnchorNode> = ArrayList()
    private var listDistance: ArrayList<AnchorNode> = ArrayList()
    private var list3dObject: ArrayList<AnchorNode> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_drag_object)
        arFragment = supportFragmentManager.findFragmentById(R.id.fragment_drag_object) as ArFragment?
        setArrayView()
        setOnClickListener()
        setupModel()
        dragObject()
        setOnLongClickListener()
        arFragment!!.setOnTapArPlaneListener { hitResult,plane, motionEvent ->
            if(checkStatus){
                val anchor = hitResult.createAnchor()
                val anchorNode = AnchorNode(anchor)
                var distance = anchorNode.localPosition.z * -1
                Toast.makeText(this,convertFloatToString(distance),Toast.LENGTH_SHORT).show()
                anchorNode.setParent(arFragment!!.arSceneView.scene)
                createMode(anchorNode, selected,distance)
                list3dObject.add(anchorNode)
            }else if (!checkStatus){
                val anchor = hitResult.createAnchor()
                val anchorNode = AnchorNode(anchor)
                anchorNode.setParent(arFragment!!.arSceneView.scene)
                createPoint(anchorNode)
            }
        }

        val handler = Handler()
        binding!!.btnCapture.setOnClickListener {
            val view: ArSceneView = arFragment!!.getArSceneView()
            val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
            PixelCopy.request(view, bitmap, { copyResult ->
                if (copyResult === PixelCopy.SUCCESS) {
                    Utility.share_ = bitmap
                    val imageSavePath = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DCIM
                    ).path + "/" + resources.getString(R.string.app_name)
                    File(imageSavePath).mkdirs()
                    val str: String = imageSavePath
                    Utility.shareFile = File(str, SimpleDateFormat(
                        Constants.FILE_NAME_FORMAT,
                        Locale.getDefault())
                        .format(System.currentTimeMillis())  + ".png")
                    try {
                        val fileOutputStream = FileOutputStream(Utility.shareFile)
                        Utility.share_.compress(Bitmap.CompressFormat.PNG, 90, fileOutputStream)
                        fileOutputStream.flush()
                        fileOutputStream.close()
                        Toast.makeText(this,"Capture Success",Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(this,"Capture Failed",Toast.LENGTH_SHORT).show()
                    }

                } else {
                    // Error
                }
            }, handler)
        }
//        button.setOnClickListener {
//            arFragment!!.arSceneView.scene.addOnUpdateListener {
//                // Chụp ảnh
//                val bitmap = arFragment!!.arSceneView.takeSnapshot(null)
//
//            }
//        }
        clickButton()
    }
    private fun clickButton(){
        binding!!.btnRuler.setOnClickListener {
            checkStatus = false
            binding!!.linearButton.visibility = View.VISIBLE
            binding!!.btnRuler.visibility = View.GONE
            initObjects()
        }
        binding!!.btnDone.setOnClickListener {
            checkStatus = true
            binding!!.linearButton.visibility = View.GONE
            binding!!.btnRuler.visibility = View.VISIBLE
            clearAnchors()
        }
        binding!!.btnRemoveObject.setOnClickListener {
            if(checkStatus){
                if(list3dObject.size > 0){
                    val objectNote = list3dObject.get(list3dObject.size-1)
                    objectNote.setParent(null)
                    arFragment!!.arSceneView.scene.removeChild(objectNote)
                    list3dObject.remove(objectNote)
                }else{
                    Toast.makeText(this,"No object !",Toast.LENGTH_SHORT).show()
                }
            }else if(!checkStatus){
                if(listLine.size > 0 && listDistance.size >0){
                    val lineNote = listLine.get(listLine.size-1)
                    val distanceNote = listDistance.get(listDistance.size-1)
                    lineNote.setParent(null)
                    arFragment!!.arSceneView.scene.removeChild(lineNote)
                    distanceNote.setParent(null)
                    arFragment!!.arSceneView.scene.removeChild(distanceNote)
                    listLine.remove(lineNote)
                    listDistance.remove(distanceNote)
                }else{
                    Toast.makeText(this,"Haven't measured the distance",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupModel() {
        ModelRenderable.builder()
            .setSource(this,R.raw.table)
            .build().thenAccept {renderable ->
                tableRenderable = renderable
            }
        ModelRenderable.builder()
            .setSource(this,R.raw.chair)
            .build().thenAccept {renderable ->
                chairRenderable = renderable
            }
        ModelRenderable.builder()
            .setSource(this,R.raw.lamp)
            .build().thenAccept {renderable ->
                lampRenderable = renderable
            }
        ModelRenderable.builder()
            .setSource(this,R.raw.model)
            .build().thenAccept {renderable ->
                bookShelfRenderable = renderable
            }
        ModelRenderable.builder()
            .setSource(this,R.raw.tv)
            .build().thenAccept {renderable ->
                tvRenderable = renderable
            }
        ModelRenderable.builder()
            .setSource(this,R.raw.piano)
            .build().thenAccept {renderable ->
                pianoRenderable = renderable
            }
        ModelRenderable.builder()
            .setSource(this,R.raw.maygiat)
            .build().thenAccept {renderable ->
                mayGiatRenderable = renderable
            }
        ModelRenderable.builder()
            .setSource(this,R.raw.officechair)
            .build().thenAccept {renderable ->
                officeChairRenderable = renderable
            }
        ModelRenderable.builder()
            .setSource(this,R.raw.bed)
            .build().thenAccept {renderable ->
                bedRenderable = renderable
            }
        ModelRenderable.builder()
            .setSource(this,R.raw.monitor)
            .build().thenAccept {renderable ->
                monitorRenderable = renderable
            }
        ModelRenderable.builder()
            .setSource(this,R.raw.ironman)
            .build().thenAccept {renderable ->
                ironManRenderable = renderable
            }
        ModelRenderable.builder()
            .setSource(this,R.raw.sofa)
            .build().thenAccept {renderable ->
                sofaRenderable = renderable
            }
        ModelRenderable.builder()
            .setSource(this,R.raw.drawbed)
            .build().thenAccept {renderable ->
                drawerbedRenderable = renderable
            }
        ModelRenderable.builder()
            .setSource(this,R.raw.mesapc)
            .build().thenAccept {renderable ->
                mesapcRenderable = renderable
            }
        ModelRenderable.builder()
            .setSource(this,R.raw.macbook)
            .build().thenAccept {renderable ->
                macbookRenderable = renderable
            }
    }

    private fun createMode(anchorNode: AnchorNode, selected: Int,distance : Float) {
        when (selected) {
            1 -> {
                val bear = TransformableNode(arFragment!!.transformationSystem)
                bear.setParent(anchorNode)
                bear.renderable = sofaRenderable
                bear.select()
                addDistance(anchorNode,bear,convertFloatToString(distance))
            }
            2 -> {
                val bear = TransformableNode(arFragment!!.transformationSystem)
                bear.setParent(anchorNode)
                bear.renderable = drawerbedRenderable
                bear.select()
                addDistance(anchorNode,bear,convertFloatToString(distance))
            }
            3 -> {
                val bear = TransformableNode(arFragment!!.transformationSystem)
                bear.setParent(anchorNode)
                bear.renderable = mesapcRenderable
                bear.select()
                addDistance(anchorNode,bear,convertFloatToString(distance))
            }
            4 -> {
                val bear = TransformableNode(arFragment!!.transformationSystem)
                bear.setParent(anchorNode)
                bear.renderable = macbookRenderable
                bear.select()
                addDistance(anchorNode,bear,convertFloatToString(distance))
            }
            13 -> {
                val bear = TransformableNode(arFragment!!.transformationSystem)
                bear.setParent(anchorNode)
                bear.renderable = tableRenderable
                bear.select()
                addDistance(anchorNode,bear,convertFloatToString(distance))
            }
            14 -> {
                val bear = TransformableNode(arFragment!!.transformationSystem)
                bear.setParent(anchorNode)
                bear.renderable = chairRenderable
                bear.select()
                addDistance(anchorNode,bear,convertFloatToString(distance))
            }
            15 -> {
                val bear = TransformableNode(arFragment!!.transformationSystem)
                bear.setParent(anchorNode)
                bear.renderable = lampRenderable
                bear.select()
                addDistance(anchorNode,bear,convertFloatToString(distance))
            }
            16 -> {
                val bear = TransformableNode(arFragment!!.transformationSystem)
                bear.setParent(anchorNode)
                bear.renderable = bookShelfRenderable
                bear.select()
                addDistance(anchorNode,bear,convertFloatToString(distance))
            }
            17 -> {
                val bear = TransformableNode(arFragment!!.transformationSystem)
                bear.setParent(anchorNode)
                bear.renderable = tvRenderable
                bear.select()
                addDistance(anchorNode,bear,convertFloatToString(distance))
            }
            18 -> {
                val bear = TransformableNode(arFragment!!.transformationSystem)
                bear.setParent(anchorNode)
                bear.renderable = pianoRenderable
                bear.select()
                addDistance(anchorNode,bear,convertFloatToString(distance))
            }
            19 -> {
                val bear = TransformableNode(arFragment!!.transformationSystem)
                bear.setParent(anchorNode)
                bear.renderable = mayGiatRenderable
                bear.select()
                addDistance(anchorNode,bear,convertFloatToString(distance))
            }
            20 -> {
                val bear = TransformableNode(arFragment!!.transformationSystem)
                bear.setParent(anchorNode)
                bear.renderable = officeChairRenderable
                bear.select()
                addDistance(anchorNode,bear,convertFloatToString(distance))
            }
            21 -> {
                val bear = TransformableNode(arFragment!!.transformationSystem)
                bear.setParent(anchorNode)
                bear.renderable = bedRenderable
                bear.select()
                addDistance(anchorNode,bear,convertFloatToString(distance))
            }
            22 -> {
                val bear = TransformableNode(arFragment!!.transformationSystem)
                bear.setParent(anchorNode)
                bear.renderable = monitorRenderable
                bear.select()
                addDistance(anchorNode,bear,convertFloatToString(distance))
            }
            23 -> {
                val bear = TransformableNode(arFragment!!.transformationSystem)
                bear.setParent(anchorNode)
                bear.renderable = ironManRenderable
                bear.select()
                addDistance(anchorNode,bear,convertFloatToString(distance))
            }
        }
    }

    private fun addDistance(anchorNode: AnchorNode, model: TransformableNode, distance: String) {
//        ViewRenderable.builder().setView(this,R.layout.distance_animal)
//            .build()
//            .thenAccept{ viewRenderable ->
//                val distanceView = TransformableNode(arFragment!!.transformationSystem)
//                distanceView.localPosition = Vector3(0F,model.localPosition.y,0F)
//                distanceView.setParent(anchorNode)
//                distanceView.renderable = viewRenderable
//
//                val txtDistance = viewRenderable!!.view as TextView?
//                txtDistance!!.text = distance
//
//                txtDistance.setOnClickListener {
//                    anchorNode.setParent(null)
//                }
//        }
    }

    private fun setOnClickListener() {
        for (i in 0 until arrayView!!.size) {
            arrayView!![i].setOnClickListener(this@DragObjectActivity)
        }
    }

    private fun setOnLongClickListener(){
        for (i in 0 until arrayView!!.size) {
            arrayView!![i].setOnLongClickListener(this@DragObjectActivity)
        }
    }

    private fun setArrayView() {
        arrayView = arrayOf(
            binding!!.macbook,
            binding!!.mesapc,
            binding!!.drawerbed,
            binding!!.sofa,
            binding!!.table,
            binding!!.chair,
            binding!!.lamp,
            binding!!.bookshelf,
            binding!!.odltv,
            binding!!.piano,
            binding!!.mayGiat,
            binding!!.chairOffice,
            binding!!.bed,
            binding!!.monitor,
            binding!!.ironman
        )
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.sofa -> {
                selected = 1
                setBackground(v.id)
            }
            R.id.drawerbed -> {
                selected = 2
                setBackground(v.id)
            }
            R.id.mesapc -> {
                selected = 3
                setBackground(v.id)
            }
            R.id.macbook -> {
                selected = 4
                setBackground(v.id)
            }
            R.id.table -> {
                selected = 13
                setBackground(v.id)
            }
            R.id.chair -> {
                selected = 14
                setBackground(v.id)
            }
            R.id.lamp -> {
                selected = 15
                setBackground(v.id)
            }
            R.id.bookshelf ->{
                selected = 16
                setBackground(v.id)
            }
            R.id.odltv -> {
                selected = 17
                setBackground(v.id)
            }
            R.id.piano -> {
                selected = 18
                setBackground(v.id)
            }
            R.id.mayGiat -> {
                selected = 19
                setBackground(v.id)
            }
            R.id.chairOffice -> {
                selected = 20
                setBackground(v.id)
            }
            R.id.bed -> {
                selected = 21
                setBackground(v.id)
            }
            R.id.monitor -> {
                selected = 22
                setBackground(v.id)
            }
            R.id.ironman -> {
                selected = 23
                setBackground(v.id)
            }
        }
    }

    private fun setBackground(id: Int) {
        for (i in 0 until arrayView!!.size){
            if (arrayView!![i].id == id){
                arrayView!![i].setBackgroundColor(Color.parseColor("#80333639"))
            }else{
                arrayView!![i].setBackgroundColor(Color.TRANSPARENT)
            }
        }
    }

    private fun dragObject() {
        binding!!.relativeLayout.setOnDragListener { view, dragEvent ->
            when (dragEvent.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    true
                }
                DragEvent.ACTION_DRAG_ENTERED -> {
                    true
                }
                DragEvent.ACTION_DRAG_EXITED -> {
                    true
                }
                DragEvent.ACTION_DROP -> {
                    true
                }
                DragEvent.ACTION_DRAG_ENDED -> {

                    true
                }
                else -> {
                    false
                }
            }
        }
    }

    fun touchListener(imgView: ImageView){
        imgView.setOnTouchListener { view, motionEvent ->
            val data = ClipData.newPlainText("","")
            val shadow = View.DragShadowBuilder(imgView)
            view.startDrag(data,shadow,null,0)
            return@setOnTouchListener false
        }
    }

    override fun onLongClick(v: View?): Boolean {
        when (v!!.id) {
            R.id.table -> {
                selected = 13
                touchListener(binding!!.table)
                return false
            }
            R.id.chair -> {
                selected = 14
                touchListener(binding!!.chair)
                return false
            }
            R.id.lamp -> {
                selected = 15
                touchListener(binding!!.lamp)
                return false
            }
            R.id.bookshelf ->{
                selected = 16
                touchListener(binding!!.bookshelf)
                return false
            }
            R.id.odltv -> {
                selected = 17
                touchListener(binding!!.odltv)
                return false
            }
        }
        return false
    }

    fun convertFloatToString(n : Float) : String {
        val myStr = String.format("%.3f m", n)
        return myStr
    }

    private fun initObjects() {
        MaterialFactory.makeOpaqueWithColor(this, Color(Color.rgb(219, 68, 55)))
            .thenAccept { material: Material? ->
                heightLineRender = ShapeFactory.makeCube(Vector3(.015f, 1f, 1f),
                    Vector3.zero(), material)
                heightLineRender!!.apply {
                    isShadowCaster = false
                    isShadowReceiver = false
                }
            }
        MaterialFactory.makeOpaqueWithColor(this, Color(Color.rgb(23, 107, 230)))
            .thenAccept { material: Material? ->
                widthLineRender = ShapeFactory.makeCube(Vector3(.01f, 0f, 1f), Vector3.zero(), material)
                widthLineRender!!.apply {
                    isShadowCaster = false
                    isShadowReceiver = false
                }
            }

        MaterialFactory.makeTransparentWithColor(this, Color(Color.rgb(23, 107, 230)))
            .thenAccept { material: Material? ->
                pointRender = ShapeFactory.makeCylinder(0.02f, 0.0003f, Vector3.zero(), material)
                pointRender!!.isShadowCaster = false
                pointRender!!.isShadowReceiver = false
            }

        ViewRenderable.builder()
            .setView(this, R.layout.distance)
            .build()
            .thenAccept { renderable: ViewRenderable ->
                renderable.apply {
                    isShadowCaster = false
                    isShadowReceiver = false
                    verticalAlignment = ViewRenderable.VerticalAlignment.BOTTOM
                }
                viewRenderable = renderable
            }

        Texture.builder()
            .setSource(this, R.drawable.aim)
            .build().thenAccept { texture ->
                MaterialFactory.makeTransparentWithTexture(this, texture)
                    .thenAccept { material: Material? ->
                        aimRender = ShapeFactory.makeCylinder(0.08f, 0f, Vector3.zero(), material)
                        aimRender!!.isShadowCaster = false
                        aimRender!!.isShadowReceiver = false
                    }
            }
    }

    private fun initTextBoxes(meters: Float, transformableNode: AnchorNode, isFromCreateNewAnchor: Boolean) {
        if (isFromCreateNewAnchor) {
            ViewRenderable.builder()
                .setView(this, R.layout.distance)
                .build()
                .thenAccept { renderable: ViewRenderable ->
                    renderable.apply {
                        isShadowCaster = false
                        isShadowReceiver = false
                        verticalAlignment = ViewRenderable.VerticalAlignment.BOTTOM
                    }
                    addDistanceCard(renderable, meters, transformableNode)

                     val txtDistance = renderable!!.view as CardView?
                     txtDistance!!.setOnClickListener {
                         transformableNode.setParent(null)
                         arFragment!!.arSceneView.scene.removeChild(transformableNode)
                     }
                }
        } else {
            addDistanceCard(viewRenderable, meters, transformableNode)
        }
    }

    private fun addDistanceCard(distanceRenderable: ViewRenderable, meters: Float, transformableNode: AnchorNode) {
        distanceInMeters = distanceRenderable.view as CardView
        val metersString: String = if (meters < 1f) {
            String.format(Locale.ENGLISH, "%.0f", meters * 100) + " cm"
        } else {
            String.format(Locale.ENGLISH, "%.2f", meters) + " m"
        }
        val tv = distanceInMeters.getChildAt(0) as TextView
        tv.text = metersString
        Log.e("meters", metersString)
        transformableNode.renderable = distanceRenderable
    }

    fun clearAnchors() {
        for (i in currentAnchorNode){
            arFragment!!.arSceneView.scene.removeChild(i)
        }
        currentAnchorNode.clear()
        currentAnchor.clear()
        labelArray.clear()
        totalLength = 0f
    }

    private fun createPoint(anchorNode: AnchorNode){
        if(currentAnchorNode.size == 0){
            //create Point 1
            TransformableNode(arFragment!!.transformationSystem).apply {
                renderable = pointRender
                setParent(anchorNode)
            }
            val anchor = anchorNode.anchor
            arFragment!!.arSceneView.scene.addChild(anchorNode)
            currentAnchor.add(anchor)
            currentAnchorNode.add(anchorNode)
            node1Pos = anchorNode?.worldPosition
        }else if(currentAnchorNode.size == 1){
            //create Point 2
            TransformableNode(arFragment!!.transformationSystem).apply {
                renderable = pointRender
                setParent(anchorNode)
            }
            val anchor = anchorNode.anchor
            arFragment!!.arSceneView.scene.addChild(anchorNode)
            currentAnchor.add(anchor)
            currentAnchorNode.add(anchorNode)
            node2Pos = anchorNode?.worldPosition
            //measure distance
            difference = Vector3.subtract(node1Pos, node2Pos)
            totalLength += difference!!.length()
            val rotationFromAToB = Quaternion.lookRotation(difference!!.normalized(), Vector3.up())
            //setting lines between points
            AnchorNode().apply {
                setParent(arFragment!!.arSceneView.scene)
                this.worldPosition = Vector3.add(node1Pos, node2Pos).scaled(.5f)
                this.worldRotation = rotationFromAToB
                localScale = Vector3(1f, 1f, difference!!.length())
                renderable = widthLineRender
                listLine.add(this)
            }
            //setting labels with distances
            labelArray.add(AnchorNode().apply {
                setParent(arFragment!!.arSceneView.scene)
                this.worldPosition = Vector3.add(node1Pos, node2Pos).scaled(.5f)
                initTextBoxes(difference!!.length(), this, true)
                listDistance.add(this)
            })
        }
    }
}