package com.bhsoft.ar3d.ui.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.bhsoft.ar3d.R
import com.bhsoft.ar3d.common.MVVMApplication
import com.bhsoft.ar3d.ui.base.AnimationScreen
import com.bhsoft.ar3d.ui.base.activity.BaseActivity
//******************************
//******************************
//***** Create by cuongpq  *****
//******************************
//******************************

abstract class BaseFragment : Fragment() , ViewFragment {
    protected var mIsDestroyView = true
    protected var mAnimationContinueId: Int = 0


    private var isResume = false
    private var isVisibleView = false
    protected var actionWhenResume: (() -> Unit)? = null
    protected lateinit var mBinding: ViewDataBinding

    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mIsDestroyView = false
        return onCreateViewControl(inflater, container, savedInstanceState)
    }

    override fun onCreateViewControl(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (inflater == null) {
            val creatInflater = LayoutInflater.from(context);
            mBinding = DataBindingUtil.inflate(creatInflater, getLayoutMain(), container, false)
        } else {
            mBinding = DataBindingUtil.inflate(inflater, getLayoutMain(), container, false)
        }
        return mBinding.root
    }


    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewCreatedControl(view, savedInstanceState)
    }

    override fun onViewCreatedControl(view: View, savedInstanceState: Bundle?) {
        setEvents()
        initComponents()
    }


    fun setAnimationContinueId(runAnimationContitue: Int) {
        mAnimationContinueId = runAnimationContitue
    }

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        if (mAnimationContinueId != 0) {
            val animation = AnimationUtils.loadAnimation(context, mAnimationContinueId)
            mAnimationContinueId = 0
            return animation
        }
        return super.onCreateAnimation(transit, enter, nextAnim)
    }

    override fun showMessage(messageId: Int) {
        if (!mIsDestroyView) {
            getBaseActivity().showMessage(messageId)
        }
    }

    override fun showMessage(message: String) {
        if (!mIsDestroyView) {
            getBaseActivity().showMessage(message)
        }
    }


    final override fun onResume() {
        super.onResume()
        isResume = true
        isVisibleView = !isHidden
        if (isVisibleView && actionWhenResume != null) {
            actionWhenResume!!()
            actionWhenResume = null
        }
        onResumeControl()
    }

    override fun onResumeControl() {

    }

    final override fun onPause() {
        isResume = false
        isVisibleView = false
        onPauseControl()
        super.onPause()
    }

    override fun onPauseControl() {

    }

    fun isVisibleView(): Boolean {
        return isVisibleView
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden) {
            isVisibleView = false
        } else {
            isVisibleView = isResume
        }
        if (isVisibleView && actionWhenResume != null) {
            actionWhenResume!!()
            actionWhenResume = null
        }
    }

    override fun onDestroyView() {
        mIsDestroyView = true
        onDestroyViewControl()
        super.onDestroyView()
    }


    override fun onDestroyViewControl() {

    }

    override fun hideKeyBoard(): Boolean {
        return getBaseActivity().hideKeyBoard()
    }

    override fun getBaseActivity(): BaseActivity {
        return activity as BaseActivity
    }

    override fun reload(bundle: Bundle) {

    }

    override fun onBackRoot() {
        getBaseActivity().onBackParent()
    }

    override val isDestroyView: Boolean
        get() = mIsDestroyView

    fun finishActivity(){
        activity?.run {
            finish()
            overridePendingTransition(R.anim.enter_to_left, R.anim.enter_to_right)
        }
    }
    fun appDatabase() = (activity?.applicationContext as MVVMApplication).appDatabase()
    fun interactCommon() = (activity?.applicationContext as MVVMApplication).interactCommon()
    fun schedule() = (activity?.applicationContext as MVVMApplication).schedule()

    companion object {

        @JvmStatic
        fun openFragment(
            manager: FragmentManager,
            transaction: FragmentTransaction,
            clazz: Class<out BaseFragment>,
            bundle: Bundle?,
            hasAddbackstack: Boolean,
            hasCommitTransaction: Boolean,
            animations: AnimationScreen?,
            fragmentContent: Int
        ): Fragment? {
            val tag = clazz.name
            var fragment: Fragment?
            try {
                //if added backstack
                fragment = manager.findFragmentByTag(tag)
                if (hasAddbackstack) {
                    if (fragment == null || !fragment.isAdded) {
                        fragment = clazz.newInstance()
                        fragment!!.arguments = bundle
                        setAnimationFragment(transaction, animations)
                        transaction.add(fragmentContent, fragment, tag)
                    } else {
                        transaction.show(fragment)
                    }
                    transaction.addToBackStack(tag)
                } else {
                    if (fragment != null) {
                        setAnimationFragment(transaction, animations)
                        transaction.show(fragment)
                    } else {
                        fragment = clazz.newInstance()
                        fragment.arguments = bundle
                        setAnimationFragment(transaction, animations)
                        transaction.add(fragmentContent, fragment, tag)
                    }
                }
                if (hasCommitTransaction) {
                    transaction.commit()
                }
                return fragment
            } catch (e: java.lang.InstantiationException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }

            return null
        }

        @JvmStatic
        fun openFragment(
            manager: FragmentManager,
            transaction: FragmentTransaction,
            fragment: BaseFragment,
            bundle: Bundle?,
            hasAddbackstack: Boolean,
            hasCommitTransaction: Boolean,
            animations: AnimationScreen,
            fragmentContent: Int
        ) {
            val tag = fragment.javaClass.name
            fragment.arguments = bundle
            setAnimationFragment(transaction, animations)
            transaction.add(fragmentContent, fragment, tag)
            if (hasAddbackstack) {
                transaction.addToBackStack(tag)
            }
            if (hasCommitTransaction) {
                transaction.commit()
            }
        }

        @JvmStatic
        fun hideFragment(
            manager: FragmentManager,
            transaction: FragmentTransaction, animations: AnimationScreen,
            hasAddBackstack: Boolean, hasCommit: Boolean, tag: String
        ) {
            val fragment = manager.findFragmentByTag(tag) as BaseFragment
            if (fragment.isVisible) {
                setAnimationFragment(transaction, animations)
                transaction.hide(fragment)
                if (hasAddBackstack) {
                    transaction.addToBackStack(tag)
                }
                if (hasCommit) {
                    transaction.commit()
                }
            }
        }

        @JvmStatic
        fun removeFragment(
            manager: FragmentManager, transaction: FragmentTransaction, animations: AnimationScreen,
            hasAddBackStack: Boolean, hasCommit: Boolean, tag: String
        ) {
            val fragment = manager.findFragmentByTag(tag) as BaseFragment
            setAnimationFragment(transaction, animations)
            transaction.remove(fragment)
            if (hasAddBackStack) {
                transaction.addToBackStack(tag)
            }
            if (hasCommit) {
                transaction.commit()
            }
        }

        private fun setAnimationFragment(
            transaction: FragmentTransaction,
            animations: AnimationScreen?
        ) {
            if (animations != null) {
                transaction.setCustomAnimations(
                    animations.enterToLeft,
                    animations.exitToLeft,
                    animations.enterToRight,
                    animations.exitToRight
                )
            }
        }


        @JvmStatic
        fun getCurrentFragment(fragmentManager: FragmentManager): BaseFragment? {
            val frags = fragmentManager.fragments
            for (i in frags.indices.reversed()) {
                val fr = frags[i]
                if (fr != null && fr.isVisible && fr.tag != null) {
                    return fr as BaseFragment
                }
            }
            return null
        }
    }
}