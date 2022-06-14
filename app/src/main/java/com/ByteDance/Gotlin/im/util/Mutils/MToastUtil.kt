package com.ByteDance.Gotlin.im.util.Mutils

import android.content.Context
import android.widget.Toast

/**
 * @Description：
 * @Author：Suzy.Mo
 * @Date：2022/6/14 12:31
 */
object MToastUtil {
    fun String.showToast(context: Context, duration: Int = Toast.LENGTH_SHORT){
        Toast.makeText(context,this,duration).show()
    }

    fun Int.showToast(context: Context,duration: Int = Toast.LENGTH_SHORT){
        Toast.makeText(context,this,duration).show()
    }
}