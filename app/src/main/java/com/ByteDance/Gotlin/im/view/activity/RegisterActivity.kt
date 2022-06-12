package com.ByteDance.Gotlin.im.view.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ByteDance.Gotlin.im.databinding.TActivityRegisterBinding
import com.ByteDance.Gotlin.im.util.Constants
import com.ByteDance.Gotlin.im.util.Tutils.TLogUtil
import com.ByteDance.Gotlin.im.util.Tutils.TPhoneUtil
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import com.xuexiang.xui.XUI

/**
 * @Author 唐靖豪
 * @Date 2022/6/12 9:49
 * @Email 762795632@qq.com
 * @Description
 */

class RegisterActivity : AppCompatActivity() {
    private lateinit var mBinding: TActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        mBinding = TActivityRegisterBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        initConfig()
        setContentView(mBinding.root)
        initViewListener()
    }

    private fun initViewListener() {
        mBinding.loginTv.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
        }
    }

    /**
     * 配置相应的界面信息
     */
    private fun initConfig() {
        XUI.initTheme(this)
        QMUIStatusBarHelper.translucent(this)
        val phoneMode = TPhoneUtil.getPhoneMode(this)
        if (phoneMode == Constants.DARK_MODE) {
            TLogUtil.d("暗色模式")
            QMUIStatusBarHelper.setStatusBarDarkMode(this)
        } else if (phoneMode == Constants.LIGHT_MODE) {
            TLogUtil.d("亮色模式")
            QMUIStatusBarHelper.setStatusBarLightMode(this)
        }
    }
}