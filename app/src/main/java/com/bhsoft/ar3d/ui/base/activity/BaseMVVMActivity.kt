package com.bhsoft.ar3d.ui.base.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bhsoft.ar3d.BR
import com.bhsoft.ar3d.common.Constants
import com.bhsoft.ar3d.ui.base.callback.BaseCallBack
import com.bhsoft.ar3d.ui.base.viewmodel.BaseViewModel
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import java.util.*
import javax.inject.Inject
//******************************
//******************************
//***** Create by cuongpq  *****
//******************************
//******************************

abstract class BaseMVVMActivity<
        CallBack : BaseCallBack,
        Model : BaseViewModel<CallBack>> : BaseActivity(), HasAndroidInjector {
    @Inject
    internal lateinit var fragmentAndroidInjector: DispatchingAndroidInjector<Any>
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    protected lateinit var mModel: Model
    protected var mFirstLoad: Long = 0

    protected abstract fun getViewModel(): Class<Model>

    override fun onCreateControl(savedInstanceState: Bundle?) {
        if (!mIsClearMemoryActivity) {
            mFirstLoad = Date().time
            AndroidInjection.inject(this)
            mBinding = DataBindingUtil.setContentView(this, getLayoutMain())
            mModel = ViewModelProvider(this, viewModelFactory).get(getViewModel())
            performDataBinding()
            setEvents()
            initComponents()
        }
        super.onCreateControl(savedInstanceState)
    }
    fun getBindingVariable() = BR.viewModel
    private fun performDataBinding() {
        mBinding.setVariable(getBindingVariable(), mModel)
        mBinding.executePendingBindings()
    }

    protected fun <T> finishLoad(t: T, action: (T) -> Unit) {
        if (mIsDestroyView) {
            return
        }
        if (mFirstLoad == (-1).toLong()) {
            action(t)
        } else {
            val currentTime = Date().time
            if (currentTime - mFirstLoad >= Constants.DURATION_ANIMATION) {
                action(t)
            } else {
                Handler(Looper.getMainLooper()).postDelayed({
                    if (mIsDestroyView) {
                        return@postDelayed
                    }
                    action(t)
                }, Constants.DURATION_ANIMATION - (currentTime - mFirstLoad))
            }
            mFirstLoad = -1
        }
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return fragmentAndroidInjector
    }
}