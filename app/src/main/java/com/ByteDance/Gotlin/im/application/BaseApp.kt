package com.ByteDance.Gotlin.im.application

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.ByteDance.Gotlin.im.Repository
import com.ByteDance.Gotlin.im.util.Constants
import com.ByteDance.Gotlin.im.util.Tutils.TPhoneUtil.getPhoneMode
import com.tencent.mmkv.MMKV
import java.util.*
import kotlin.system.exitProcess

/**
 * @Author 唐靖豪
 * @Date 2022/6/9 19:33
 * @Email 762795632@qq.com
 * @Description:
 * 用于获取全局的context
 */
@RequiresApi(Build.VERSION_CODES.Q)
class BaseApp : Application() {
    companion object {
        var mContext: Application? = null
        fun getContext(): Context {
            return mContext!!
        }
    }

    override fun onCreate() {
        super.onCreate()
        mContext = this
        // MMKV存储路径
        val dir = filesDir.absolutePath + "/mmkv"
        // MMKV初始化
        val rootDir = MMKV.initialize(dir)
    }


}