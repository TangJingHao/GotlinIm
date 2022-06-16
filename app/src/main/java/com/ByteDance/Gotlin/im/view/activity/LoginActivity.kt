package com.ByteDance.Gotlin.im.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.ByteDance.Gotlin.im.databinding.TActivityLoginBinding
import com.ByteDance.Gotlin.im.model.LoginLiveData
import com.ByteDance.Gotlin.im.util.Constants
import com.ByteDance.Gotlin.im.util.Tutils.TLogUtil
import com.ByteDance.Gotlin.im.util.Tutils.TPermissionUtil
import com.ByteDance.Gotlin.im.util.Tutils.TPhoneUtil
import com.ByteDance.Gotlin.im.util.Tutils.TPictureSelectorUtil.TGlideEngine
import com.ByteDance.Gotlin.im.util.Tutils.TPictureSelectorUtil.TMyEditMediaIListener
import com.ByteDance.Gotlin.im.util.Tutils.TPictureSelectorUtil.TMyPictureSelectorStyle
import com.ByteDance.Gotlin.im.viewmodel.LoginViewModel
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.config.SelectModeConfig
import com.luck.picture.lib.style.PictureSelectorStyle
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import com.xuexiang.xui.XUI


class LoginActivity : AppCompatActivity() {
    private lateinit var mViewModel: LoginViewModel
    private lateinit var mBinding: TActivityLoginBinding
    private lateinit var mContext: Context

    private lateinit var mLauncherResult: ActivityResultLauncher<Intent>
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
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
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
            if (mPasswordFlag && mUserNameFlag) {
                mViewModel.login(LoginLiveData(mUserName, mPassword))
            } else {
                TPhoneUtil.showToast(mContext, "邮箱或者密码不可以为空")
            }
        }
        mBinding.registerTv.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        //测试用：
        mBinding.userLoginReadTv.setOnClickListener {
            PictureSelector.create(this)
                .openGallery(SelectMimeType.ofImage())
                .setSelectorUIStyle(mSelectorStyle)
                .setImageEngine(TGlideEngine.createGlideEngine())
                .setSelectionMode(SelectModeConfig.MULTIPLE)
                .setEditMediaInterceptListener(mMyEditMediaIListener)
                .forResult(mLauncherResult)
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

        mSelectorStyle = TMyPictureSelectorStyle.getSelectorStyle(this)
        mMyEditMediaIListener = TMyEditMediaIListener(
            TPhoneUtil.getSandboxPath(mContext),
            mContext,
            Constants.DEFAULT_TYPE
        )
        mLauncherResult = createActivityResultLauncher()
    }

    /**
     * 创建一个ActivityResultLauncher
     *
     * @return
     */
    private fun createActivityResultLauncher(): ActivityResultLauncher<Intent> {
        return registerForActivityResult<Intent?, ActivityResult>(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            val resultCode = result.resultCode
            if (resultCode == RESULT_OK) {
                val selectList = PictureSelector.obtainSelectorList(result.data)
                Log.i("===", selectList.size.toString() + "")
                val media = selectList[0]
                val cut = media.isCut
                if (cut) {
                    //是裁剪过的
                } else { //没有裁剪过的
                }
            } else if (resultCode == RESULT_CANCELED) {
                TLogUtil.d("点击取消")
            }
        }
    }

}