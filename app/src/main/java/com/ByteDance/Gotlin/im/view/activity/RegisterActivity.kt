package com.ByteDance.Gotlin.im.view.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.widget.RadioButton
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.ByteDance.Gotlin.im.R
import com.ByteDance.Gotlin.im.Repository
import com.ByteDance.Gotlin.im.databinding.TActivityRegisterBinding
import com.ByteDance.Gotlin.im.model.LoginLiveData
import com.ByteDance.Gotlin.im.model.RegisterCodeLiveData
import com.ByteDance.Gotlin.im.model.RegisterForUserLiveData
import com.ByteDance.Gotlin.im.util.Constants
import com.ByteDance.Gotlin.im.util.Tutils.TLogUtil
import com.ByteDance.Gotlin.im.util.Tutils.TPhoneUtil
import com.ByteDance.Gotlin.im.viewmodel.RegisterViewModel
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import com.xuexiang.xui.XUI


/**
 * @Author 唐靖豪
 * @Date 2022/6/12 9:49
 * @Email 762795632@qq.com
 * @Description
 */
@RequiresApi(Build.VERSION_CODES.Q)
class RegisterActivity : AppCompatActivity() {
    private lateinit var mBinding: TActivityRegisterBinding
    private lateinit var mViewModel: RegisterViewModel

    //判空符号
    private var mEmailFlag = false
    private var mVerificationCodeFlag = false
    private var mUserNameFlag = false
    private var mUserPasswordFlag = false

    //注册部分
    private lateinit var mEmail: String
    private var mUserName = ""
    private var mVerificationCode = ""
    private var mPassword = ""
    private var mSex = "男"
    override fun onCreate(savedInstanceState: Bundle?) {
        XUI.initTheme(this)
        super.onCreate(savedInstanceState)
        mBinding = TActivityRegisterBinding.inflate(layoutInflater)
        mViewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
        initConfig()
        setContentView(mBinding.root)
        initViewListener()
        initEditListener()
        initCallBack()
    }

    /**
     * 监听回调结果
     */
    private fun initCallBack() {
        mViewModel.registerObserverCodeData.observe(this) { result ->
            var responseData = result.getOrNull()
            if (responseData == null) {
                TLogUtil.d("服务器发送验证码异常")
                TPhoneUtil.showToast(this, "发送验证码异常")
            } else {
                TPhoneUtil.showToast(this,responseData.msg)
                if (responseData.msg == "发送验证码成功") {
                    TLogUtil.d("服务器发送验证码成功")
                }
            }
        }
        mViewModel.registerUserObserverData.observe(this){result->
            var responseData=result.getOrNull()
            if(responseData==null){
                TLogUtil.d("注册失败")
                TPhoneUtil.showToast(this, "注册失败")
            }else{
                TPhoneUtil.showToast(this,responseData.msg)
                if(responseData.msg=="注册成功"){
                    //保存到仓库层，方便调用
                    Repository.setUserLoginUserName(mUserName)
                    Repository.setUserLoginPassword(mPassword)
                    mViewModel.login(LoginLiveData(mUserName,mPassword))
                }
            }
        }
        mViewModel.loginObserverData.observe(this){result->
            val responseData = result.getOrNull()
            if(responseData!=null){
                //数据存储
                Repository.saveUserId(responseData.data.user.userId)
                Repository.setUserData(responseData.data.user)
                Repository.setToken(responseData.data.token)
                val avatar=responseData.data.user.avatar
                if(avatar!=null){
                    var index = avatar.indexOf(".")
                    var substring = avatar.substring(index + 1)
                    var s = Constants.BASE_AVATAR_URL + substring
                    Repository.setUserLoginAvatar(s)
                }
                Repository.setUserLoginNickname(responseData.data.user.nickName.toString())
                Repository.setUserLoginSex(responseData.data.user.sex.toString())
                Repository.setUserLoginPassword(mPassword)
                Repository.setUserLoginUserName(mUserName)
                Repository.mToken = responseData.data.token
                TPhoneUtil.showToast(this,"注册成功")
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
                this.overridePendingTransition(
                    R.anim.t_splash_open, R.anim.t_splash_close
                )
                TLogUtil.d("注册成功")
            }
        }
    }

    /**
     * 监听输入框
     */
    private fun initEditListener() {
        //注册邮箱
        mBinding.etInput.addTextChangedListener {
            mEmail = it.toString()
            mEmailFlag = mEmail.isNotEmpty()
        }
        //注册用户名
        mBinding.usernameEt.addTextChangedListener {
            mUserName = it.toString()
            mUserNameFlag = mUserName.isNotEmpty()
        }
        //注册验证码
        mBinding.verificationCodeEt.addTextChangedListener {
            mVerificationCode = it.toString()
            mVerificationCodeFlag = mVerificationCode.isNotEmpty()
        }
        //用户密码
        mBinding.confirmPasswordEt.addTextChangedListener {
            mPassword = it.toString()
            mUserPasswordFlag = mPassword.isNotEmpty()
        }

    }

    /**
     * 设置监听
     */
    private fun initViewListener() {
        mBinding.loginTv.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            this.overridePendingTransition(
                R.anim.t_splash_open, R.anim.t_splash_close
            )
        }
        mBinding.btCountdown.setOnClickListener {
            if (mEmailFlag && mUserNameFlag) {
                mBinding.btCountdown.setEnableCountDown(true)
                TPhoneUtil.showToast(this, "发送成功")
                mViewModel.registerForCode(RegisterCodeLiveData(mUserName, mEmail))
            } else {
                TPhoneUtil.showToast(this, "注册邮箱和用户名不能为空")
                mBinding.btCountdown.setEnableCountDown(false)
            }
        }
        mBinding.registerBtn.setOnClickListener {
            if (mEmailFlag && mUserNameFlag && mUserPasswordFlag && mVerificationCodeFlag) {
                for (i in 0 .. mBinding.sexRg.childCount ){
                    val sex = mBinding.sexRg.getChildAt(i) as RadioButton
                    if (sex.isChecked) {
                        mSex = sex.text.toString().trim()
                        break
                    }
                }
            }
            mViewModel.registerUser(RegisterForUserLiveData(mUserName,mPassword,mSex,mEmail,mVerificationCode))
        }

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            this.overridePendingTransition(
                R.anim.t_splash_open, R.anim.t_splash_close
            )
        }
        return super.onKeyDown(keyCode, event)
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