package com.ByteDance.Gotlin.im.application

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.ByteDance.Gotlin.im.R
import com.ByteDance.Gotlin.im.Repository
import com.ByteDance.Gotlin.im.databinding.TActivityInitBinding
import com.ByteDance.Gotlin.im.util.Constants
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

class BaseActivity : AppCompatActivity() {
    private lateinit var mBinding:TActivityInitBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        mBinding=TActivityInitBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        XUI.initTheme(this)
        setContentView(mBinding.root)
        var phoneMode = TPhoneUtil.getPhoneMode(this)
        if(phoneMode==Constants.USER_LIGHT_MODE){
            QMUIStatusBarHelper.translucent(this)
            QMUIStatusBarHelper.setStatusBarLightMode(this)
        }else{
            QMUIStatusBarHelper.translucent(this)
            QMUIStatusBarHelper.setStatusBarDarkMode(this)
        }
        this.overridePendingTransition(
            R.anim.t_splash_open,R.anim.t_splash_close
        )
        Handler(Looper.getMainLooper()).postDelayed({
            if(Repository.getUserId()!=Constants.USER_DEFAULT_ID){
                val mainIntent = Intent(this,MainActivity::class.java) //前者为跳转前页面，后者为跳转后页面
                startActivity(mainIntent)
                finish()
                this.overridePendingTransition(
                    R.anim.t_splash_open,R.anim.t_splash_close
                )
            }else{
                val mainIntent = Intent(this,LoginActivity::class.java) //前者为跳转前页面，后者为跳转后页面
                startActivity(mainIntent)
                finish()
                this.overridePendingTransition(
                    R.anim.t_splash_open,R.anim.t_splash_close
                )
            }

<<<<<<< HEAD
        Repository.saveUserId(3)

        //判断用户是否登录过，后期可以写在闪屏页面
//        SearchActivity.startMsgSearch(this,6)
        startActivity(Intent(this,MainActivity::class.java))
        finish()
//        if(Repository.getUserId()!=Constants.USER_DEFAULT_ID){
//            startActivity(Intent(this,GroupMembersActivity::class.java).putExtra(Constants.GROUP_ID,1))
//            finish()
//        }else{
//            val intent = Intent(this, LoginActivity::class.java)
//            startActivity(intent)
//            finish()
//        }
=======
        }, 5000) //设置时间，5秒后自动跳转
>>>>>>> tjh

    }
}