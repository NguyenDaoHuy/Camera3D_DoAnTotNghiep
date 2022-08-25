package com.bhsoft.ar3d.ui.main.camera_detect_activity.ml.drag_object
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bhsoft.ar3d.R
import com.bhsoft.ar3d.databinding.ActivityDragObjectBinding
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode

class DragObjectActivity : AppCompatActivity(), View.OnClickListener {
    private var binding : ActivityDragObjectBinding?=null
    private var arFragment : ArFragment?=null
    private var bearRenderable : ModelRenderable?=null
    private var catRenderable : ModelRenderable?=null
    private var dogRenderable : ModelRenderable?=null
    private var cowRenderable : ModelRenderable?=null
    private var elephantRenderable : ModelRenderable?=null
    private var ferretRenderable : ModelRenderable?=null
    private var hippopotamusRenderable : ModelRenderable?=null
    private var horserRenderable : ModelRenderable?=null
    private var koalarRenderable : ModelRenderable?=null
    private var lionRenderable : ModelRenderable?=null
    private var reindeerRenderable : ModelRenderable?=null
    private var wolverineRenderable : ModelRenderable?=null
    private var tableRenderable : ModelRenderable?=null
    private var chairRenderable : ModelRenderable?=null
    private var lampRenderable : ModelRenderable?=null
    private var bookShelfRenderable : ModelRenderable?=null
    private var tvRenderable : ModelRenderable?=null
    private var selected = 1
    var arrayView: Array<View>?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_drag_object)
        arFragment = supportFragmentManager.findFragmentById(R.id.fragment_drag_object) as ArFragment?
        setArrayView()
        setOnClickListener()
        setupModel()
        arFragment!!.setOnTapArPlaneListener { hitResult, _, _ ->
                val anchor = hitResult.createAnchor()
                val anchorNode = AnchorNode(anchor)
                anchorNode.setParent(arFragment!!.arSceneView.scene)
                createMode(anchorNode,selected)
        }
    }

    private fun setupModel() {

        ModelRenderable.builder()
            .setSource(this,R.raw.bear)
            .build().thenAccept {renderable ->
                bearRenderable = renderable
            }
        ModelRenderable.builder()
            .setSource(this,R.raw.cat)
            .build().thenAccept {renderable ->
                catRenderable = renderable
            }
        ModelRenderable.builder()
            .setSource(this,R.raw.cow)
            .build().thenAccept {renderable ->
                cowRenderable = renderable
            }
        ModelRenderable.builder()
            .setSource(this,R.raw.dog)
            .build().thenAccept {renderable ->
                dogRenderable = renderable
            }
        ModelRenderable.builder()
            .setSource(this,R.raw.elephant)
            .build().thenAccept {renderable ->
                elephantRenderable = renderable
            }
        ModelRenderable.builder()
            .setSource(this,R.raw.ferret)
            .build().thenAccept {renderable ->
                ferretRenderable = renderable
            }
        ModelRenderable.builder()
            .setSource(this,R.raw.hippopotamus)
            .build().thenAccept {renderable ->
                hippopotamusRenderable = renderable
            }
        ModelRenderable.builder()
            .setSource(this,R.raw.horse)
            .build().thenAccept {renderable ->
                horserRenderable = renderable
            }
        ModelRenderable.builder()
            .setSource(this,R.raw.lion)
            .build().thenAccept {renderable ->
                lionRenderable = renderable
            }
        ModelRenderable.builder()
            .setSource(this,R.raw.koala_bear)
            .build().thenAccept {renderable ->
                koalarRenderable = renderable
            }
        ModelRenderable.builder()
            .setSource(this,R.raw.reindeer)
            .build().thenAccept {renderable ->
                reindeerRenderable = renderable
            }
        ModelRenderable.builder()
            .setSource(this,R.raw.wolverine)
            .build().thenAccept {renderable ->
                wolverineRenderable = renderable
            }
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
    }

    private fun createMode(anchorNode: AnchorNode, selected: Int) {
        when (selected) {
            1 -> {
                val bear = TransformableNode(arFragment!!.transformationSystem)
                bear.setParent(anchorNode)
                bear.renderable = bearRenderable
                bear.select()
            }
            2 -> {
                val bear = TransformableNode(arFragment!!.transformationSystem)
                bear.setParent(anchorNode)
                bear.renderable = catRenderable
                bear.select()
            }
            3 -> {
                val bear = TransformableNode(arFragment!!.transformationSystem)
                bear.setParent(anchorNode)
                bear.renderable = cowRenderable
                bear.select()
            }
            4 -> {
                val bear = TransformableNode(arFragment!!.transformationSystem)
                bear.setParent(anchorNode)
                bear.renderable = dogRenderable
                bear.select()
            }
            5 -> {
                val bear = TransformableNode(arFragment!!.transformationSystem)
                bear.setParent(anchorNode)
                bear.renderable = elephantRenderable
                bear.select()
            }
            6 -> {
                val bear = TransformableNode(arFragment!!.transformationSystem)
                bear.setParent(anchorNode)
                bear.renderable = ferretRenderable
                bear.select()
            }
            7 -> {
                val bear = TransformableNode(arFragment!!.transformationSystem)
                bear.setParent(anchorNode)
                bear.renderable = hippopotamusRenderable
                bear.select()
            }
            8 -> {
                val bear = TransformableNode(arFragment!!.transformationSystem)
                bear.setParent(anchorNode)
                bear.renderable = horserRenderable
                bear.select()
            }
            9 -> {
                val bear = TransformableNode(arFragment!!.transformationSystem)
                bear.setParent(anchorNode)
                bear.renderable = koalarRenderable
                bear.select()
            }
            10 -> {
                val bear = TransformableNode(arFragment!!.transformationSystem)
                bear.setParent(anchorNode)
                bear.renderable = lionRenderable
                bear.select()
            }
            11 -> {
                val bear = TransformableNode(arFragment!!.transformationSystem)
                bear.setParent(anchorNode)
                bear.renderable = reindeerRenderable
                bear.select()
            }
            12 -> {
                val bear = TransformableNode(arFragment!!.transformationSystem)
                bear.setParent(anchorNode)
                bear.renderable = wolverineRenderable
                bear.select()
            }
            13 -> {
                val bear = TransformableNode(arFragment!!.transformationSystem)
                bear.setParent(anchorNode)
                bear.renderable = tableRenderable
                bear.select()
            }
            14 -> {
                val bear = TransformableNode(arFragment!!.transformationSystem)
                bear.setParent(anchorNode)
                bear.renderable = chairRenderable
                bear.select()
            }
            15 -> {
                val bear = TransformableNode(arFragment!!.transformationSystem)
                bear.setParent(anchorNode)
                bear.renderable = lampRenderable
                bear.select()
            }
            16 -> {
                val bear = TransformableNode(arFragment!!.transformationSystem)
                bear.setParent(anchorNode)
                bear.renderable = bookShelfRenderable
                bear.select()
            }
            17 -> {
                val bear = TransformableNode(arFragment!!.transformationSystem)
                bear.setParent(anchorNode)
                bear.renderable = tvRenderable
                bear.select()
            }
        }

    }

    private fun setOnClickListener() {
        for (i in 0 until arrayView!!.size) {
            arrayView!![i].setOnClickListener(this@DragObjectActivity)
        }
    }

    private fun setArrayView() {
        arrayView = arrayOf(binding!!.bear,binding!!.cat,binding!!.cow,binding!!.dog,binding!!.elephant,binding!!.ferret,
                            binding!!.hippopotamus,binding!!.horse,binding!!.koalaBear,binding!!.lion,binding!!.reindeer,binding!!.wolverine,
                            binding!!.table,binding!!.chair,binding!!.lamp,binding!!.bookshelf,binding!!.odltv)
    }

    override fun onClick(v: View?) {

        when (v!!.id) {
            R.id.bear -> {
                selected = 1
                setBackground(v.id)
            }
            R.id.cat -> {
                selected = 2
                setBackground(v.id)
            }
            R.id.cow -> {
                selected = 3
                setBackground(v.id)
            }
            R.id.dog -> {
                selected = 4
                setBackground(v.id)
            }
            R.id.elephant -> {
                selected = 5
                setBackground(v.id)
            }
            R.id.ferret -> {
                selected = 6
                setBackground(v.id)
            }
            R.id.hippopotamus -> {
                selected = 7
                setBackground(v.id)
            }
            R.id.horse -> {
                selected = 8
                setBackground(v.id)
            }
            R.id.koala_bear -> {
                selected = 9
                setBackground(v.id)
            }
            R.id.lion -> {
                selected = 10
                setBackground(v.id)
            }
            R.id.reindeer -> {
                selected = 11
                setBackground(v.id)
            }
            R.id.wolverine -> {
                selected = 12
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


}