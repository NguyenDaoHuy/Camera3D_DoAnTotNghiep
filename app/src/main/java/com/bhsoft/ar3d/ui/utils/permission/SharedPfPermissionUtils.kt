package com.bhsoft.ar3d.ui.utils.permission

import android.content.Context
//******************************
//******************************
//***** Create by cuongpq  *****
//******************************
//******************************

object SharedPfPermissionUtils {
    val SYSTEM_CONFIG = "SYSTEM_CONFIG"

    @JvmStatic
    fun getNumberDeniedPermission(context: Context, permissionType: String): Int {
        val sf = context.getSharedPreferences(SYSTEM_CONFIG, Context.MODE_PRIVATE)
        return sf.getInt(permissionType, 0)
    }

    @JvmStatic
    fun increaseNumberDeniedPermission(context: Context, permissionType: String, numberIncrease: Int) {
        val sf = context.getSharedPreferences(SYSTEM_CONFIG, Context.MODE_PRIVATE)
        val numberCurrent = sf.getInt(permissionType, 0)
        val editor = sf.edit()
        editor.putInt(permissionType, numberIncrease + numberCurrent)
        editor.apply()
    }

    @JvmStatic
    fun saveNumberDeniedPermission(context: Context, permissionType: String, number: Int) {
        val sf = context.getSharedPreferences(SYSTEM_CONFIG, Context.MODE_PRIVATE)
        val editor = sf.edit()
        editor.putInt(permissionType, number)
        editor.apply()
    }
}