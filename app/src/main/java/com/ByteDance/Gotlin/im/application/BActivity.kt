package com.ByteDance.Gotlin.im.application

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ByteDance.Gotlin.im.util.DUtils.DLogUtils

/**
 * @Author Zhicong Deng
 * @Date 2022/6/24 21:23
 * @Email 1520483847@qq.com
 * @Description
 */
open class BActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DLogUtils.w(javaClass.simpleName, "onCreate")
    }

    override fun onStart() {
        super.onStart()
        DLogUtils.w(javaClass.simpleName, "onStart")
    }

    override fun onResume() {
        super.onResume()
        DLogUtils.w(javaClass.simpleName, "onResume")
    }

    override fun onRestart() {
        super.onRestart()
        DLogUtils.w(javaClass.simpleName, "onRestart")
    }

    override fun onDestroy() {
        super.onDestroy()
        DLogUtils.w(javaClass.simpleName, "onDestroy")
    }

    override fun onStop() {
        super.onStop()
        DLogUtils.w(javaClass.simpleName, "onStop")
    }

    override fun onPause() {
        super.onPause()
        DLogUtils.w(javaClass.simpleName, "onPause")
    }
}