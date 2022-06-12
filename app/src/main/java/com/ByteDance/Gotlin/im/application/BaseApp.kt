package com.ByteDance.Gotlin.im.application

import android.app.Application
import android.content.Context
import com.tencent.mmkv.MMKV

/**
 * @Author 唐靖豪
 * @Date 2022/6/9 19:33
 * @Email 762795632@qq.com
 * @Description:
 * 用于获取全局的context
 */

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