package com.bhsoft.ar3d.ui.utils

import androidx.fragment.app.FragmentManager
import com.bhsoft.ar3d.R
import com.bhsoft.ar3d.ui.base.AnimationScreen
import com.bhsoft.ar3d.ui.base.fragment.BaseFragment
import com.bhsoft.ar3d.ui.main.user.UserFragment
//******************************
//******************************
//***** Create by cuongpq  *****
//******************************
//******************************

object OpenFragmentUtils {
    @JvmStatic
    fun getAnimationScreenFullOpen(): AnimationScreen {
        return AnimationScreen(
            R.anim.enter_to_left,
            R.anim.exit_to_left,
            R.anim.enter_to_right,
            R.anim.exit_to_right
        )
    }

    @JvmStatic
    fun openUserFragment(manager: FragmentManager) {
        val transaction = manager.beginTransaction()
        BaseFragment.openFragment(
            manager,
            transaction,
            UserFragment::class.java,
            null,
            false,
            true,
            null,
            R.id.content
        )
    }
}