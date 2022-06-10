package com.ByteDance.Gotlin.im.util.Tutils

import android.util.Log

/**
 * @Author 唐靖豪
 * @Date 2022/6/9 21:26
 * @Email 762795632@qq.com
 * @Description:
 * 直接使用这个类进行日志信息的打印debug
 */

object TLogUtil {
    private const val isDebug: Boolean = true
    private const val TAG: String = "===我们的日志信息："

    /**
     *包装log.d日志
     */
    fun d(msg: String) {
        if (isDebug) {
            Log.d(TAG, msg)
        }
    }
    fun d(tag: String,vararg msg:String){
        if (isDebug) {
            var msgS = "";
            for ((index,item) in msg.withIndex()){
                if(index == 0){
                    msgS+= item
                }else {
                    msgS+= (" "+item)
                }
            }
            Log.d(tag, msgS)
        }
    }

    /**
     *包装log.e日志
     */
    fun e(msg: String) {
        if (isDebug) {
            Log.e(TAG, msg)
        }
    }
    fun e(tag: String,vararg msg:String){
        if (isDebug) {
            var msgS = "";
            for ((index,item) in msg.withIndex()){
                if(index == 0){
                    msgS+= item
                }else {
                    msgS+= (" "+item)
                }
            }
            Log.e(tag, msgS)
        }
    }

    /**
     * v类型的log.v日志
     */
    fun v(msg: String) {
        if (isDebug) {
            Log.v(TAG, msg)
        }
    }
    fun v(tag: String,vararg msg:String){
        if (isDebug) {
            var msgS = "";
            for ((index,item) in msg.withIndex()){
                if(index == 0){
                    msgS+= item
                }else {
                    msgS+= (" "+item)
                }
            }
            Log.v(tag, msgS)
        }
    }
}