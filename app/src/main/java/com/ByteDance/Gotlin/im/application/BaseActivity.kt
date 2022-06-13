package com.ByteDance.Gotlin.im.application

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ByteDance.Gotlin.im.util.Constants
import com.ByteDance.Gotlin.im.util.Tutils.TLogUtil
import com.ByteDance.Gotlin.im.util.Tutils.TPhoneUtil
import com.ByteDance.Gotlin.im.view.activity.*
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import com.xuexiang.xui.XUI

/**
 * @Author 唐靖豪
 * @Date 2022/6/12 10:35
 * @Email 762795632@qq.com
 * @Description
 * 测试基类（后期在这里修改为闪屏）
 * 直接改这里的文件，不用重复修改manifest
 */

class BaseActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = Intent(this, SetFriendInfoActivity::class.java)
        startActivity(intent)
        finish()
    }
}