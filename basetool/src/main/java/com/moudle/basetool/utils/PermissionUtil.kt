package com.moudle.basetool.utils

import android.content.Context
import com.yanzhenjie.permission.AndPermission
import com.moudle.basetool.ui.DialogCallback
import com.moudle.basetool.ui.MyAlertDialog

/**
 * Created by ke_li on 2018/2/9.
 */
class PermissionUtil constructor(val context: Context) {

    fun excuteSelfPermission(yesDone: () -> Unit, vararg permission: String) {
        prepailRequestPermission(yesDone).permission(permission).start()
    }

    fun excuteGroupPermission(permission: Array<String>, yesDone: () -> Unit) {
        prepailRequestPermission(yesDone).permission(permission).start()
    }


    private fun prepailRequestPermission(yesDone: () -> Unit) = AndPermission.with(context).let {
        val request = it
        it.rationale { context, _, executor ->
            MyAlertDialog.Builder()
                    .setTitle("是否重新申请此权限").build(context).excute(object : DialogCallback {
                override fun ok() {
                    executor.execute()
                }

                override fun no() {
                    executor.cancel()
                }
            })
        }.onDenied {
                    if (AndPermission.hasAlwaysDeniedPermission(context, it)) {
                        // 这些权限被用户总是拒绝。
                        MyAlertDialog.Builder()
                                .setTitle("是否重新申请此权限").build(context).excute(object : DialogCallback {
                            override fun ok() {
                                request.permission(it.toTypedArray()).start()
                            }

                            override fun no() {
                            }
                        })
                    }
                }.onGranted {
                    LogUtil.LogI(it.toString())
                    yesDone.invoke()
                }
    }

}