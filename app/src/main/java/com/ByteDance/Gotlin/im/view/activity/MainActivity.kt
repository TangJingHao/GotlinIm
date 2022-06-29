package com.ByteDance.Gotlin.im.view.activity

import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ByteDance.Gotlin.im.R
import com.ByteDance.Gotlin.im.Repository
import com.ByteDance.Gotlin.im.application.BaseActivity
import com.ByteDance.Gotlin.im.application.BaseApp
import com.ByteDance.Gotlin.im.broadcast.NetWorkReceiver
import com.ByteDance.Gotlin.im.databinding.TActivityMainBinding
import com.ByteDance.Gotlin.im.util.Constants
import com.ByteDance.Gotlin.im.util.DUtils.DLogUtils
import com.ByteDance.Gotlin.im.util.Tutils.TLogUtil
import com.ByteDance.Gotlin.im.util.Tutils.TPhoneUtil
import com.ByteDance.Gotlin.im.view.custom.MainBnvVp2Mediator
import com.ByteDance.Gotlin.im.view.custom.MainViewPagerAdapter
import com.ByteDance.Gotlin.im.view.fragment.AddressBookFragment
import com.ByteDance.Gotlin.im.view.fragment.MessageFragment
import com.ByteDance.Gotlin.im.view.fragment.MyInformationFragment
import com.ByteDance.Gotlin.im.viewmodel.MainViewModel
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import com.xuexiang.xui.XUI
import java.util.*
import kotlin.system.exitProcess

/**
 * @Author 唐靖豪
 * @Date 2022/6/12 13:58
 * @Email 762795632@qq.com
 * @Description
 */
@RequiresApi(Build.VERSION_CODES.Q)
class MainActivity : AppCompatActivity() {
    private lateinit var mBinding: TActivityMainBinding
    private val timer = Timer()
    private var mode: Int = -1
    private val mNetWorkReceiver = NetWorkReceiver()//监听网络状态
    private val viewModelMain: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    //项目Fragment集合
    private val mFragments = mapOf<Int, Fragment>(
        INDEX_MESSAGE to MessageFragment(),
        INDEX_ADDRESS_BOOK to AddressBookFragment(),
        INDEX_MY_INFORMATION to MyInformationFragment(),
    )

    companion object {
        const val INDEX_MESSAGE = 0
        const val INDEX_ADDRESS_BOOK = 1
        const val INDEX_MY_INFORMATION = 2
        const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = TActivityMainBinding.inflate(layoutInflater)
        initConfig()
        initView()
        initListener()
        setContentView(mBinding.root)
        mBinding.apply {
            vp2Main.adapter = MainViewPagerAdapter(this@MainActivity, mFragments)
            MainBnvVp2Mediator(bnvMain, vp2Main) { bnv, vp2 ->
                bnv.itemIconTintList = null
                vp2.isUserInputEnabled = true//设置是否响应用户滑动
            }.attach()
        }
        // 开启WebSocket
    }

    private fun initView() {
        // 消息列表底部小红点监听
        viewModelMain.msgRedPointObserver.observe(this) {
            if (it > 0)
                mBinding.bnvMain.getOrCreateBadge(R.id.navigation_message_item).apply {
                    number = it
                    isVisible = true
                    horizontalOffset = 20
                    verticalOffset = 10
                }
        }

        // 通讯录底部小红点监听
        viewModelMain.requestRedPointObserver.observe(this) {
            val response = it.getOrNull()
            if (response != null) {
                val totalUnread = response.data.total
                DLogUtils.i(TAG, "通讯录未读数：${totalUnread}")
                if (totalUnread > 0)
                    mBinding.bnvMain.getOrCreateBadge(R.id.navigation_address_book_item).apply {
                        isVisible = true
                        horizontalOffset = 20
                        verticalOffset = 10
                    }
                viewModelMain.setNewFriendRedPointNum(response.data.friend)
                viewModelMain.setNewGroupChatRedPointNum(response.data.group)
            }
        }
        // 通知更新小红点
        viewModelMain.getMsgRedPointNum()

    }

    /**
     * 配置相应的界面信息
     */
    private fun initConfig() {
        XUI.initTheme(this)
        QMUIStatusBarHelper.translucent(this)
        val phoneMode = TPhoneUtil.getPhoneMode(this)
        initTheme(phoneMode)
        //网络状态变化就会出发这个action
        val filter = IntentFilter()
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(mNetWorkReceiver, filter)
    }

    private fun initTheme(phoneMode: Int) {
        if (Repository.getUserMode() == Constants.USER_DEFAULT_MODE) {
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
            val userStatus = Repository.getUserMode()
            if (userStatus == Constants.USER_LIGHT_MODE) {
                QMUIStatusBarHelper.setStatusBarLightMode(this)
            } else if (userStatus == Constants.USER_DARK_MODE) {
                QMUIStatusBarHelper.setStatusBarDarkMode(this)
            }
        }
    }

    private fun initListener() {
        timer.schedule(timerTask, 0, 1000)
    }


    private val timerTask: TimerTask = object : TimerTask() {
        override fun run() {
            val phoneMode = TPhoneUtil.getPhoneMode(BaseApp.getContext())
            if (mode == -1) {
                mode = phoneMode
            } else if (mode != phoneMode) {
                startActivity(Intent(this@MainActivity, BaseActivity::class.java))
                mode = phoneMode
                exitProcess(0)
                overridePendingTransition(0, 0)
//                if (Repository.getUserChangeAction() == Constants.USER_DEFAULT_MODE) {
//                    startActivity(Intent(this@MainActivity, BaseActivity::class.java))
//                    mode = phoneMode
//                    exitProcess(0)
//                    overridePendingTransition(0, 0)
//                }
            }
        }
    }

}