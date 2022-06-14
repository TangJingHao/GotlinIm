package com.ByteDance.Gotlin.im.util.Mutils

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

/**
 * @Description：
 * @Author：Suzy.Mo
 * @Date：2022/6/14 12:33
 */
open class ApplicationUtil() : Application(){

    companion object{
        //静态内部类单例模式实例化Activity
        val instance = ApplicationHolder.holder

        //全局中只会有一个application的Context实例且在app生命周期内不会被回收，不存在内存泄露
        //用SuppressLint忽略警告
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

    private object ApplicationHolder{
        val holder = ApplicationUtil()
    }

}