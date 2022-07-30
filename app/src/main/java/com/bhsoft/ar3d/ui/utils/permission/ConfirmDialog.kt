package com.bhsoft.ar3d.ui.utils.permission

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.Window
import android.widget.TextView
import androidx.annotation.StringRes
import com.bhsoft.ar3d.R
//******************************
//******************************
//***** Create by cuongpq  *****
//******************************
//******************************

class ConfirmDialog : Dialog, View.OnClickListener{
    private var mInterf: IConfirmDialog? = null

    constructor(context: Context, @StringRes resContent: Int, interf: IConfirmDialog) : super(context) {
        mInterf = interf
        inits(context.getString(resContent))
    }

    constructor(context: Context, content: String, interf: IConfirmDialog) : super(context) {
        inits(content)
        mInterf = interf
    }

    constructor(context: Context, themeResId: Int, @StringRes resContent: Int, interf: IConfirmDialog) : super(context, themeResId) {
        mInterf = interf
        inits(context.getString(resContent))
    }


    constructor(context: Context, themeResId: Int, content: String, interf: IConfirmDialog) : super(context, themeResId) {
        inits(content)
        mInterf = interf
    }

    private fun inits(content: String) {
//        requestWindowFeature(Window.FEATURE_NO_TITLE)
//        setContentView(R.layout.dialog_confirm)
//        findViewById<View>(R.id.btn_cancel).setOnClickListener(this)
//        findViewById<View>(R.id.btn_ok).setOnClickListener(this)
//
//        (findViewById<View>(R.id.tv_content) as TextView).text = content
    }

    override fun onClick(view: View) {
//        when (view.id) {
//            R.id.btn_ok -> {
//                dismiss()
//                mInterf!!.onClickOk()
//            }
//            R.id.btn_cancel -> {
//                dismiss()
//                mInterf!!.onClickCancel()
//            }
//            else -> {
//            }
//        }
    }


    interface IConfirmDialog {
        fun onClickCancel()

        fun onClickOk()
    }
}