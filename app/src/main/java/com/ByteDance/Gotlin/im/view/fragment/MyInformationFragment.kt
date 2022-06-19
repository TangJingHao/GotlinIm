package com.ByteDance.Gotlin.im.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ByteDance.Gotlin.im.R
import com.ByteDance.Gotlin.im.Repository
import com.ByteDance.Gotlin.im.databinding.TFragmentMyInfomationBinding
import com.ByteDance.Gotlin.im.util.Constants
import com.ByteDance.Gotlin.im.util.DUtils.diy.ConfirmPopupWindow
import com.ByteDance.Gotlin.im.util.DUtils.diy.InputPopupWindow
import com.ByteDance.Gotlin.im.util.Tutils.TLogUtil
import com.ByteDance.Gotlin.im.util.Tutils.TPhoneUtil
import com.ByteDance.Gotlin.im.util.Tutils.TPictureSelectorUtil.TGlideEngine
import com.ByteDance.Gotlin.im.util.Tutils.TPictureSelectorUtil.TMyEditMediaIListener
import com.ByteDance.Gotlin.im.view.activity.LoginActivity
import com.ByteDance.Gotlin.im.viewmodel.MyInformationViewModel
import com.bumptech.glide.Glide
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.config.SelectModeConfig
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.style.PictureSelectorStyle
import com.qmuiteam.qmui.util.QMUIStatusBarHelper

/**
 * @Author 唐靖豪
 * @Date 2022/6/12 14:25
 * @Email 762795632@qq.com
 * @Description
 * 我的
 */

class MyInformationFragment : Fragment() {
    private lateinit var mBinding: TFragmentMyInfomationBinding
    private lateinit var mViewModel:MyInformationViewModel
    private lateinit var mMyEditMediaIListener: TMyEditMediaIListener
    private lateinit var mLauncherResult: ActivityResultLauncher<Intent>
    private lateinit var mInputPopupWindow: InputPopupWindow
    private lateinit var mConfirmPopupWindow:ConfirmPopupWindow
    private var mSelectorStyle = PictureSelectorStyle()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = TFragmentMyInfomationBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        mViewModel=ViewModelProvider(requireActivity()).get(MyInformationViewModel::class.java)
//        mViewModel.mStatus.value=Repository.getUserStatus()
        initConfig()
    }

    override fun onStart() {
        super.onStart()
        initView()
        initListener()
    }

    /**
     * 初始化界面
     */
    private fun initView() {
        if(Repository.getUserStatus()==Constants.USER_LIGHT_MODE){
            val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_24_sun)
            mBinding.statusChangeIv.background = drawable
            mBinding.sbIosBtn.isChecked=false
        }else if(Repository.getUserStatus()==Constants.USER_DARK_MODE){
            val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_24_moon)
            mBinding.statusChangeIv.background = drawable
            mBinding.sbIosBtn.isChecked=true
        }
    }

    /**
     * 所有控件的事件监听
     */
    private fun initListener() {
        //头像监听
        mBinding.iconIv.setOnClickListener {
            PictureSelector.create(this)
                .openGallery(SelectMimeType.ofImage())
                .setSelectorUIStyle(mSelectorStyle)
                .setImageEngine(TGlideEngine.createGlideEngine())
                .setSelectionMode(SelectModeConfig.SINGLE)
                .setEditMediaInterceptListener(mMyEditMediaIListener)
                .forResult(mLauncherResult)
        }
        //修改昵称
        mBinding.nicknameIv.setOnClickListener {
            mInputPopupWindow= InputPopupWindow(requireContext(),"修改昵称")
            mInputPopupWindow.popupWindow.animationStyle=R.style.t_popup_window_style
            mInputPopupWindow.setOnConfirmListener {
                mBinding.nicknameTv.text=it
            }
            mInputPopupWindow.setOnDismissListener {
                mInputPopupWindow.dismiss()
            }
            mInputPopupWindow.show()
        }

        //模式切换监听
        mBinding.sbIosBtn.setOnCheckedChangeListener { compoundButton, ischeck ->
            if(ischeck){
                mConfirmPopupWindow= ConfirmPopupWindow(requireContext(),"设置此项需要重启APP\n是否需要?")
                mConfirmPopupWindow.setConfirmText("重启")
                mConfirmPopupWindow.setCancelText("取消")
                mConfirmPopupWindow.popupWindow.animationStyle=R.style.t_popup_window_style
                mConfirmPopupWindow.setWarnTextColorType()
                mConfirmPopupWindow.setOnConfirmListener {
                    Repository.saveUserStatus(Constants.USER_DARK_MODE)
                    //TODO:后续加上token后要重新模拟登录
                    TLogUtil.d("用户设置暗黑模式")
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    requireActivity().recreate()
                }
                mConfirmPopupWindow.setOnDismissListener {
                    mBinding.sbIosBtn.isChecked=true
                }
                mConfirmPopupWindow.show()
            }else{
                mConfirmPopupWindow= ConfirmPopupWindow(requireContext(),"设置此项需要重启APP\n是否需要?")
                mConfirmPopupWindow.setConfirmText("重启")
                mConfirmPopupWindow.setCancelText("取消")
                mConfirmPopupWindow.popupWindow.animationStyle=R.style.t_popup_window_style
                mConfirmPopupWindow.setWarnTextColorType()
                mConfirmPopupWindow.setOnConfirmListener {
                    Repository.saveUserStatus(Constants.USER_DEFAULT_MODE)
                    //TODO:后续加上token后要重新模拟登录
                    TLogUtil.d("用户设置跟随系统")
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    requireActivity().recreate()
                }
                mConfirmPopupWindow.setOnDismissListener {
                    mBinding.sbIosBtn.isChecked=false
                }
                mConfirmPopupWindow.show()
            }
        }

        mBinding.loginConfigIv.setOnClickListener {
            mConfirmPopupWindow= ConfirmPopupWindow(requireContext(),"是否退出登录")
            mConfirmPopupWindow.setConfirmText("确认")
            mConfirmPopupWindow.setCancelText("取消")
            mConfirmPopupWindow.popupWindow.animationStyle=R.style.t_popup_window_style
            mConfirmPopupWindow.setWarnTextColorType()
            mConfirmPopupWindow.setOnConfirmListener {
                requireActivity().apply {
                    startActivity(Intent(requireActivity(),LoginActivity::class.java))
                    finish()
                }
            }
            mConfirmPopupWindow.setOnDismissListener {
                mConfirmPopupWindow.dismiss()
            }
            mConfirmPopupWindow.show()

        }
    }

    /**
     * 配置头像选择器
     */
    private fun initConfig() {
        //选择头像
        mMyEditMediaIListener = TMyEditMediaIListener(
            TPhoneUtil.getSandboxPath(requireContext()),
            requireContext(),
            Constants.CIRCLE_TYPE
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
            if (resultCode == AppCompatActivity.RESULT_OK) {
                val selectList = PictureSelector.obtainSelectorList(result.data)
                val media:LocalMedia= selectList[0]
                val cut = media.isCut
                if (cut) {
                    //是裁剪过的
                    Glide.with(requireContext()).load(media.cutPath).into(mBinding.iconIv)
                } else { //没有裁剪过的
                    Glide.with(requireContext()).load(media.path).into(mBinding.iconIv)

                }
            } else if (resultCode == AppCompatActivity.RESULT_CANCELED) {
                TLogUtil.d("点击取消")
            }
        }
    }


}