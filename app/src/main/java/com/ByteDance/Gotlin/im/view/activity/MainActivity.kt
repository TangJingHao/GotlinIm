package com.ByteDance.Gotlin.im.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.ByteDance.Gotlin.im.Repository
import com.ByteDance.Gotlin.im.databinding.TActivityMainBinding
import com.ByteDance.Gotlin.im.util.Constants
import com.ByteDance.Gotlin.im.util.Tutils.TLogUtil
import com.ByteDance.Gotlin.im.util.Tutils.TPhoneUtil
import com.ByteDance.Gotlin.im.view.custom.MainBnvVp2Mediator
import com.ByteDance.Gotlin.im.view.custom.MainViewPagerAdapter
import com.ByteDance.Gotlin.im.view.fragment.AddressBookFragment
import com.ByteDance.Gotlin.im.view.fragment.MessageFragment
import com.ByteDance.Gotlin.im.view.fragment.MyInformationFragment
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import com.xuexiang.xui.XUI

/**
 * @Author 唐靖豪
 * @Date 2022/6/12 13:58
 * @Email 762795632@qq.com
 * @Description
 */

class MainActivity:AppCompatActivity() {
    private lateinit var mBinding:TActivityMainBinding
    //项目Fragment集合
    private val mFragments = mapOf<Int, Fragment>(
        INDEX_MESSAGE to MessageFragment(),
        INDEX_ADDRESS_BOOK to AddressBookFragment(),
        INDEX_MY_INFORMATION to MyInformationFragment(),
    )
    companion object{
        const val INDEX_MESSAGE = 0
        const val INDEX_ADDRESS_BOOK = 1
        const val INDEX_MY_INFORMATION = 2
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding=TActivityMainBinding.inflate(layoutInflater)
        initConfig()
        initView()
        setContentView(mBinding.root)
        mBinding.apply {
            vp2Main.adapter = MainViewPagerAdapter(this@MainActivity, mFragments)
            MainBnvVp2Mediator(bnvMain,vp2Main){ bnv, vp2 ->
                vp2.isUserInputEnabled = true//设置是否响应用户滑动
            }.attach()
        }
    }

    private fun initView() {

    }

    /**
     * 配置相应的界面信息
     */
    private fun initConfig() {
        XUI.initTheme(this)
        QMUIStatusBarHelper.translucent(this)
        val phoneMode = TPhoneUtil.getPhoneMode(this)
        initTheme(phoneMode)
    }

    private fun initTheme(phoneMode: Int) {
        if (Repository.getUserStatus() == Constants.USER_DEFAULT_MODE) {
            //用户没有设置状态
            if (phoneMode == Constants.DARK_MODE) {
                TLogUtil.d("暗色模式")
                QMUIStatusBarHelper.setStatusBarDarkMode(this)
            } else if (phoneMode == Constants.LIGHT_MODE) {
                TLogUtil.d("亮色模式")
                QMUIStatusBarHelper.setStatusBarLightMode(this)
            }
        } else {
            //用户有设置状态
            val userStatus = Repository.getUserStatus()
            if (userStatus == Constants.USER_LIGHT_MODE) {
                QMUIStatusBarHelper.setStatusBarLightMode(this)
            } else if (userStatus == Constants.USER_DARK_MODE) {
                QMUIStatusBarHelper.setStatusBarDarkMode(this)
            }
        }
    }

}