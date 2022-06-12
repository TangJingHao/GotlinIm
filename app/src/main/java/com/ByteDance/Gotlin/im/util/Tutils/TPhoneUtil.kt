package com.ByteDance.Gotlin.im.util.Tutils

import android.content.Context
import android.widget.Toast
import com.ByteDance.Gotlin.im.util.Constants
import com.xuexiang.xui.XUI
import java.io.File

/**
 * @Author 唐靖豪
 * @Date 2022/6/10 16:54
 * @Email 762795632@qq.com
 * @Description
 */

object TPhoneUtil {
    /**
     * 判断手机处于什么模式
     */
    fun getPhoneMode(context: Context):Int{
        return if(context.applicationContext.resources.configuration.uiMode==0x21){
            Constants.DARK_MODE
        }else{
            Constants.LIGHT_MODE
        }
    }

    /**
     * 用来发送吐司的工具类
     */
    fun showToast(context: Context,msg:String){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    /**
     * 创建图片自定义输出目录:
     */
    fun getSandboxPath(context: Context): String{
        val externalFilesDir = context.getExternalFilesDir("")
        val customFile = File(externalFilesDir!!.absolutePath, "Sandbox")
        if (!customFile.exists()) {
            customFile.mkdirs()
        }
        return customFile.absolutePath + File.separator
    }

    /**
     * 单位换算
     */
    fun dp2px(context: Context, pxValue: Float): Int {
        val density = context.resources.displayMetrics.density
        return (pxValue * density + 0.5f).toInt()
    }

}