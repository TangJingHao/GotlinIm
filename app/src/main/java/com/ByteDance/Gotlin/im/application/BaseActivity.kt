package com.ByteDance.Gotlin.im.application

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.ByteDance.Gotlin.im.R
import com.ByteDance.Gotlin.im.Repository
import com.ByteDance.Gotlin.im.databinding.TActivityInitBinding
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
@RequiresApi(Build.VERSION_CODES.Q)
class BaseActivity : AppCompatActivity() {
    private lateinit var mBinding:TActivityInitBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        mBinding=TActivityInitBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        XUI.initTheme(this)
        QMUIStatusBarHelper.translucent(this)
        setContentView(mBinding.root)
        initTheme()
        Handler(Looper.getMainLooper()).postDelayed({
            if(Repository.getUserId()!=Constants.USER_DEFAULT_ID){
                val mainIntent = Intent(this,MainActivity::class.java) //前者为跳转前页面，后者为跳转后页面
                startActivity(mainIntent)
                finish()
//                this.overridePendingTransition(
//                    R.anim.t_splash_open,R.anim.t_splash_close
//                )
            }else{
                val mainIntent = Intent(this,LoginActivity::class.java) //前者为跳转前页面，后者为跳转后页面
                startActivity(mainIntent)
                finish()
//                this.overridePendingTransition(
//                    R.anim.t_splash_open,R.anim.t_splash_close
//                )
            }

        }, 2000) //设置时间，5秒后自动跳转
    }

    /**
     * 设置相应的主题
     */
    private fun initTheme() {
        //用户设置了模式
        if(Repository.getUserChangeAction()==Constants.USER_DEFAULT_MODE){
            var phoneMode = TPhoneUtil.getPhoneMode(this)
            if(phoneMode==Constants.LIGHT_MODE){
                QMUIStatusBarHelper.setStatusBarLightMode(this)
            }else{
                QMUIStatusBarHelper.setStatusBarDarkMode(this)
            }
        }else{
            //用户没有设置模式
            var userStatus = Repository.getUserMode()
            if(userStatus==Constants.USER_LIGHT_MODE){
                QMUIStatusBarHelper.setStatusBarLightMode(this)
            }else{
                QMUIStatusBarHelper.setStatusBarDarkMode(this)
            }
        }
    }


}