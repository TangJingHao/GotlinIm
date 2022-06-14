package com.ByteDance.Gotlin.im.util.Mutils

import android.widget.Toast

/**
 * @Description：
 * @Author：Suzy.Mo
 * @Date：2022/6/14 12:31
 */
object MToastUtil {
    fun String.showToast(duration: Int = Toast.LENGTH_SHORT){
        Toast.makeText(ApplicationUtil.context,this,duration).show()
    }

    fun Int.showToast(duration: Int = Toast.LENGTH_SHORT){
        Toast.makeText(ApplicationUtil.context,this,duration).show()
    }
}