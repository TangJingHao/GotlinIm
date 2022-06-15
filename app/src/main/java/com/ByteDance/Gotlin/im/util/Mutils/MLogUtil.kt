package com.ByteDance.Gotlin.im.util.Mutils

import android.util.Log

/**
 * @Description：打印日志等级的工具类
 * @Author：Suzy.Mo
 * @Date：2022/6/14 12:24
 */
object MLogUtil {
    private const val VERBOSE = 1

    private const val DEBUG = 2

    private const val INFO = 3

    private const val WARN = 4

    private const val ERROR = 5

    //在产品发布的时候把等级改掉即可
    private var level = VERBOSE

    fun v(tag:String ,msg:String){
        if(level<= VERBOSE)
            Log.v(tag, msg)
    }

    fun d(tag:String ,msg:String){
        if(level<= DEBUG)
            Log.d(tag, msg)
    }

    fun i(tag:String ,msg:String){
        if(level<= INFO)
            Log.i(tag, msg)
    }

    fun w(tag:String ,msg:String){
        if(level<= WARN)
            Log.w(tag, msg)
    }

    fun e(tag:String ,msg:String){
        if(level<= ERROR)
            Log.e(tag, msg)
    }
}