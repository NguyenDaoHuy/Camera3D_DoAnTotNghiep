package com.bhsoft.ar3d.ui.utils.permission

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.bhsoft.ar3d.R
import java.util.ArrayList

object PermissionGrantUtils {
    @JvmStatic
    fun checkPermissionReadWriteExternalStore(fragment: Fragment, requestCode: Int, backCancelancelIf: Boolean): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true
        }
        val pers = ArrayList<String>()
        if (ActivityCompat.checkSelfPermission(fragment.requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(fragment.requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                if (SharedPfPermissionUtils.getNumberDeniedPermission(fragment.requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) > 0) {
                    showDialogConfirmOpenSetting(fragment, requestCode, backCancelancelIf)
                    return false
                }
            }
            pers.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        } else {
            SharedPfPermissionUtils.saveNumberDeniedPermission(fragment.requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE, 0)
        }

        if (ActivityCompat.checkSelfPermission(fragment.requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(fragment.requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                if (SharedPfPermissionUtils.getNumberDeniedPermission(fragment.requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) > 0) {
                    showDialogConfirmOpenSetting(fragment, requestCode, backCancelancelIf)
                    return false
                }
            }
            pers.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        } else {
            SharedPfPermissionUtils.saveNumberDeniedPermission(fragment.requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE, 0)
        }

        if (pers.size == 0) {
            return true
        }
        val arrs = arrayOfNulls<String>(pers.size)
        var index = 0
        for (per in pers) {
            arrs[index] = per
            index++
        }
        fragment.requestPermissions(arrs, requestCode)
        return false
    }

    @JvmStatic
    fun checkPermissionReadWriteExternalStore(activity: Activity, requestCode: Int, backCancelancelIf: Boolean): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true
        }
        val pers = ArrayList<String>()
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                if (SharedPfPermissionUtils.getNumberDeniedPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) > 0) {
                    showDialogConfirmOpenSetting(activity, requestCode, backCancelancelIf)
                    return false
                }
            }
            pers.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        } else {
            SharedPfPermissionUtils.saveNumberDeniedPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE, 0)
        }

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                if (SharedPfPermissionUtils.getNumberDeniedPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) > 0) {
                    showDialogConfirmOpenSetting(activity, requestCode, backCancelancelIf)
                    return false
                }
            }
            pers.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        } else {
            SharedPfPermissionUtils.saveNumberDeniedPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE, 0)
        }

        if (pers.size == 0) {
            return true
        }
        val arrs = arrayOfNulls<String>(pers.size)
        var index = 0
        for (per in pers) {
            arrs[index] = per
            index++
        }
        ActivityCompat.requestPermissions(activity, arrs, requestCode)
        return false
    }

    @JvmStatic
    fun checkPermissionReadWriteExternalStore(activity: Activity, perCheck: Array<String>, requestCode: Int, backCancelancelIf: Boolean): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true
        }
        val pers = ArrayList<String>()
        for (per in perCheck) {
            if (ActivityCompat.checkSelfPermission(activity, per) != PackageManager.PERMISSION_GRANTED) {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, per)) {
                    if (SharedPfPermissionUtils.getNumberDeniedPermission(activity, per) > 0) {
                        showDialogConfirmOpenSetting(activity, requestCode, backCancelancelIf)
                        return false
                    }
                }
                pers.add(per)
            } else {
                SharedPfPermissionUtils.saveNumberDeniedPermission(activity, per, 0)
            }
        }

        if (pers.size == 0) {
            return true
        }
        val arrs = arrayOfNulls<String>(pers.size)
        var index = 0
        for (per in pers) {
            arrs[index] = per
            index++
        }
        ActivityCompat.requestPermissions(activity, arrs, requestCode)
        return false
    }

    @JvmStatic
    fun checkPermissionReadWriteExternalStore(fragment: Fragment, perCheck: Array<String>, requestCode: Int, backCancelancelIf: Boolean): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true
        }
        val pers = ArrayList<String>()
        for (per in perCheck) {
            if (ActivityCompat.checkSelfPermission(fragment.requireActivity(), per) != PackageManager.PERMISSION_GRANTED) {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(fragment.requireActivity(), per)) {
                    if (SharedPfPermissionUtils.getNumberDeniedPermission(fragment.requireActivity(), per) > 0) {
                        showDialogConfirmOpenSetting(fragment.requireActivity(), requestCode, backCancelancelIf)
                        return false
                    }
                }
                pers.add(per)
            } else {
                SharedPfPermissionUtils.saveNumberDeniedPermission(fragment.requireActivity(), per, 0)
            }
        }

        if (pers.size == 0) {
            return true
        }
        val arrs = arrayOfNulls<String>(pers.size)
        var index = 0
        for (per in pers) {
            arrs[index] = per
            index++
        }
        fragment.requestPermissions(arrs, requestCode)
        return false
    }


    @JvmStatic
    private fun showDialogConfirmOpenSetting(fragment: Fragment, requestCode: Int, backCancelancelIf: Boolean) {
        val dialog = ConfirmDialog(fragment.requireActivity(), R.string.You_need_open_setting_to_grant_permission, object : ConfirmDialog.IConfirmDialog {
            override fun onClickCancel() {
                if (backCancelancelIf) {
                    fragment.requireActivity().onBackPressed()
                }
            }

            override fun onClickOk() {
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                intent.addCategory(Intent.CATEGORY_DEFAULT)
                intent.data = Uri.parse("package:" + fragment.requireActivity().packageName)
                fragment.startActivityForResult(intent, requestCode)
            }
        })
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()

    }

    @JvmStatic
    private fun showDialogConfirmOpenSetting(activity: Activity, requestCode: Int, backCancelancelIf: Boolean) {
        val dialog = ConfirmDialog(activity, R.string.You_need_open_setting_to_grant_permission, object : ConfirmDialog.IConfirmDialog {
            override fun onClickCancel() {
                if (backCancelancelIf) {
                    activity.onBackPressed()
                }
            }

            override fun onClickOk() {
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                intent.addCategory(Intent.CATEGORY_DEFAULT)
                intent.data = Uri.parse("package:" + activity.packageName)
                activity.startActivityForResult(intent, requestCode)
            }
        })
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()

    }

    @JvmStatic
    fun checkPermissionFinish(activity: Activity, pernissions: Array<out String>, granted: IntArray): Boolean {
        var result = true
        for (i in granted.indices) {
            if (granted[i] == PackageManager.PERMISSION_DENIED) {
                result = false
                if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, pernissions[i])) {
                    SharedPfPermissionUtils.increaseNumberDeniedPermission(activity, pernissions[i], 1)
                }
            } else {
                SharedPfPermissionUtils.saveNumberDeniedPermission(activity, pernissions[i], 0)
            }
        }

        return result
    }

    @JvmStatic
    fun checkPermissionOnResult(activity: Activity, permissions: Array<out String>): Boolean {
        var isSuccess = true
        for (i in permissions.indices) {
            if (ActivityCompat.checkSelfPermission(activity, permissions[i]) == PackageManager.PERMISSION_DENIED) {
                isSuccess = false
            } else {
                SharedPfPermissionUtils.saveNumberDeniedPermission(activity, permissions[i], 0)
            }
        }
        return isSuccess
    }
}