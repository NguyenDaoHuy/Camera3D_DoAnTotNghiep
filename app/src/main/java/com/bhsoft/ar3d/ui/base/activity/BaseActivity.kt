package com.bhsoft.ar3d.ui.base.activity

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.bhsoft.ar3d.common.MVVMApplication
import com.bhsoft.ar3d.ui.base.fragment.BaseFragment
import com.bhsoft.ar3d.ui.utils.permission.PermissionGrantUtils
import com.google.android.material.snackbar.Snackbar
//******************************
//******************************
//***** Create by cuongpq  *****
//******************************
//******************************

abstract class BaseActivity : AppCompatActivity(), ViewActivity {
    protected var mIsClearMemoryActivity: Boolean = false
    protected var mIsDestroyView = true
    private var mViewRoot: View? = null
    protected var mIsStarted: Boolean = false
    protected lateinit var mBinding: ViewDataBinding
    protected var actionWhenResume: (() -> Unit)? = null
    private var isVisibleView = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mIsDestroyView = false
        beforLoadUI()
        onCreateControl(savedInstanceState)
    }

    override fun onCreateControl(savedInstanceState: Bundle?) {
        if (!mIsClearMemoryActivity) {
            mBinding = DataBindingUtil.setContentView(this, getLayoutMain())
            setEvents()
            initComponents()
        }
    }

    protected fun beforLoadUI() {
        mIsClearMemoryActivity = false
    }

    override fun findFragmentByTag(tag: String): BaseFragment {
        return supportFragmentManager.findFragmentByTag(tag) as BaseFragment
    }

    override fun setViewRoot(viewRoot: View) {
        mViewRoot = viewRoot
    }

    override fun showMessage(message: String) {
        if (!mIsDestroyView) {
            if (mViewRoot == null) {
                runOnUiThread { Toast.makeText(this, message, Toast.LENGTH_SHORT).show() }
                return
            }
            val snackbar = Snackbar.make(mViewRoot!!, message, Snackbar.LENGTH_LONG)
            snackbar.show()
        }
    }

    override fun showMessage(messageId: Int) {
        if (!mIsDestroyView) {
            if (mViewRoot == null) {
                Toast.makeText(this, messageId, Toast.LENGTH_SHORT).show()
                return
            }
            val snackbar = Snackbar.make(mViewRoot!!, messageId, Snackbar.LENGTH_LONG)
            snackbar.show()
        }
    }

    final override fun onBackParent() {
        super.onBackPressed()
    }

    override fun onBackRoot() {
        onBackParent()
    }

    override fun onBackPressed() {
        val baseFragment = BaseFragment.getCurrentFragment(supportFragmentManager)
        if (null == baseFragment) {
            onBackParent()
        } else {
            baseFragment.onBackRoot()
        }
    }

    final override fun onResume() {
        super.onResume()
        if (!mIsClearMemoryActivity) {
            onResumeControl()
            isVisibleView = true
            actionWhenResume?.invoke()
            actionWhenResume = null
        }
    }

    override fun onResumeControl() {

    }

    final override fun onPause() {
        isVisibleView = false
        if (!mIsClearMemoryActivity) {
            onPauseControl()
        }
        super.onPause()
    }

    fun isVisibleView(): Boolean {
        return isVisibleView
    }

    override fun onPauseControl() {

    }

    override fun hideKeyBoard(): Boolean {
        val view = currentFocus ?: return false
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override final fun onStart() {
        super.onStart()
        mIsStarted = true
        onStartControl()
    }

    override fun onStartControl() {

    }

    override final fun onStop() {
        mIsStarted = false
        onStopControl()
        super.onStop()
    }

    override fun onStopControl() {

    }

    final override fun onDestroy() {
        mIsDestroyView = true
        onDestroyControl()
        super.onDestroy()
    }

    override fun onDestroyControl() {
    }
    override val isDestroyView: Boolean
        get() = mIsDestroyView

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        PermissionGrantUtils.checkPermissionFinish(this, permissions, grantResults)
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    fun appDatabase() = (applicationContext as MVVMApplication).appDatabase()
    fun interactCommon() = (applicationContext as MVVMApplication).interactCommon()
    fun schedule() = (applicationContext as MVVMApplication).schedule()
}