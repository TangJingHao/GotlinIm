package com.ByteDance.Gotlin.im.util.Tutils

import android.content.Context
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions

/**
 * @Author 唐靖豪
 * @Date 2022/6/10 19:29
 * @Email 762795632@qq.com
 * @Description
 * 危险权限申请类，后续添加危险权限可以参考:
 * https://blog.csdn.net/dongaddxing/article/details/122874261
 */

object TPermissionUtil {
    fun getPermission(context: Context){
        XXPermissions.with(context)
            .permission(Permission.MANAGE_EXTERNAL_STORAGE)
            .permission(Permission.RECORD_AUDIO)
            .permission(Permission.WRITE_SETTINGS)
            .request(object : OnPermissionCallback {
                override fun onGranted(permissions: MutableList<String>?, all: Boolean) {
                    if (all) {
                        TPhoneUtil.showToast(context,"获取权限成功")
                    } else {
                        TPhoneUtil.showToast(context,"获取部分权限成功，但部分权限未正常授予")
                    }
                }

                override fun onDenied(permissions: MutableList<String>?, never: Boolean) {
                    if (never) {
                        TPhoneUtil.showToast(context,"被永久拒绝授权，请手动授予权限")
                        // 如果是被永久拒绝就跳转到应用权限系统设置页面
                        XXPermissions.startPermissionActivity(context, permissions)
                    } else {
                        TPhoneUtil.showToast(context,"获取权限失败")
                    }
                }
            })
    }

}