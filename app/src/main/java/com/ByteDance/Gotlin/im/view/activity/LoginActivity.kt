package com.ByteDance.Gotlin.im.view.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.ByteDance.Gotlin.im.Repository
import com.ByteDance.Gotlin.im.databinding.TActivityLoginBinding
import com.ByteDance.Gotlin.im.model.LoginLiveData
import com.ByteDance.Gotlin.im.util.Constants
import com.ByteDance.Gotlin.im.util.Tutils.TLogUtil
import com.ByteDance.Gotlin.im.util.Tutils.TPermissionUtil
import com.ByteDance.Gotlin.im.util.Tutils.TPhoneUtil
import com.ByteDance.Gotlin.im.util.Tutils.TPictureSelectorUtil.TMyEditMediaIListener
import com.ByteDance.Gotlin.im.util.Tutils.TPictureSelectorUtil.TMyPictureSelectorStyle
import com.ByteDance.Gotlin.im.viewmodel.LoginViewModel
import com.luck.picture.lib.style.PictureSelectorStyle
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import com.xuexiang.xui.XUI

@RequiresApi(Build.VERSION_CODES.Q)
class LoginActivity : AppCompatActivity() {
    private lateinit var mViewModel: LoginViewModel
    private lateinit var mBinding: TActivityLoginBinding
    private lateinit var mContext: Context

    private lateinit var mMyEditMediaIListener: TMyEditMediaIListener
    private var mSelectorStyle = PictureSelectorStyle()

    //用户名
    private lateinit var mUserName: String
    private var mUserNameFlag: Boolean = false

    //密码
    private lateinit var mPassword: String
    private var mPasswordFlag: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        mBinding = TActivityLoginBinding.inflate(layoutInflater)
        mViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        mContext = this
        super.onCreate(savedInstanceState)
        initConfig()
        setContentView(mBinding.root)
        initPermission()
        initViewListener()
        initCallBack()
    }

    /**
     * 检查相应的权限
     */
    private fun initPermission() {
        TPermissionUtil.getPermission(this)
    }


    /**
     * 监听返回值
     */
    private fun initCallBack() {
        mViewModel.loginObserverData.observe(this) { result ->
            var responseData = result.getOrNull()
            if (responseData == null) {
                TLogUtil.d("用户名或者密码错误")
                TPhoneUtil.showToast(mContext, "用户名或者密码错误")
            } else {
                TPhoneUtil.showToast(mContext, responseData.msg)
                if (responseData.msg == "登录成功") {
                    Repository.saveUserId(responseData.data.user.userId)
                    Repository.setUserData(responseData.data.user)
                    var intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    /**
     * 设置界面监听
     */
    private fun initViewListener() {
        mBinding.emailEt.addTextChangedListener {
            mUserName = it.toString()
            mUserNameFlag = mUserName.isNotEmpty()
        }
        mBinding.passwordEt.addTextChangedListener {
            mPassword = it.toString()
            mPasswordFlag = mPassword.isNotEmpty()
        }
        mBinding.loginBtn.setOnClickListener {
            if (!mBinding.loginCb.isChecked) {
                TPhoneUtil.showToast(mContext, "请勾选用户协议!")
                return@setOnClickListener
            }
            if (mPasswordFlag && mUserNameFlag) {
                mViewModel.login(LoginLiveData(mUserName, mPassword))
            } else {
                TPhoneUtil.showToast(mContext, "邮箱或者密码不可以为空")
            }
        }
        mBinding.registerTv.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

    }

    /**
     * 配置相应的界面信息
     */
    private fun initConfig() {
        XUI.initTheme(this)
        QMUIStatusBarHelper.translucent(this)
        initTheme()
        mSelectorStyle = TMyPictureSelectorStyle.getSelectorStyle(this)
        mMyEditMediaIListener = TMyEditMediaIListener(
            TPhoneUtil.getSandboxPath(mContext),
            mContext,
            Constants.DEFAULT_TYPE
        )
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