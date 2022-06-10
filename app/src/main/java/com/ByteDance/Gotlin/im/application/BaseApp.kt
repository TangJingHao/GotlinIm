package com.ByteDance.Gotlin.im.application

import android.app.Application
import android.content.Context

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
    }

}