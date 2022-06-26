package com.ByteDance.Gotlin.im.view.fragment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.ByteDance.Gotlin.im.R
import com.ByteDance.Gotlin.im.Repository
import com.ByteDance.Gotlin.im.application.BaseActivity
import com.ByteDance.Gotlin.im.databinding.TFragmentMyInfomationBinding
import com.ByteDance.Gotlin.im.util.Constants
import com.ByteDance.Gotlin.im.util.DUtils.diy.ConfirmPopupWindow
import com.ByteDance.Gotlin.im.util.DUtils.diy.InputPopupWindow
import com.ByteDance.Gotlin.im.util.DUtils.diy.PopupWindowListener
import com.ByteDance.Gotlin.im.util.DUtils.diy.SingleSelectPopupWindow
import com.ByteDance.Gotlin.im.util.Tutils.TLogUtil
import com.ByteDance.Gotlin.im.util.Tutils.TPhoneUtil
import com.ByteDance.Gotlin.im.util.Tutils.TPictureSelectorUtil.TGlideEngine
import com.ByteDance.Gotlin.im.util.Tutils.TPictureSelectorUtil.TMyEditMediaIListener
import com.ByteDance.Gotlin.im.view.activity.LoginActivity
import com.bumptech.glide.Glide
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.config.SelectModeConfig
import com.luck.picture.lib.style.PictureSelectorStyle

/**
 * @Author 唐靖豪
 * @Date 2022/6/12 14:25
 * @Email 762795632@qq.com
 * @Description
 * 我的
 */
@RequiresApi(Build.VERSION_CODES.Q)
class MyInformationFragment : Fragment() {
    private lateinit var mBinding: TFragmentMyInfomationBinding
    private lateinit var mMyEditMediaIListener: TMyEditMediaIListener
    private lateinit var mLauncherResult: ActivityResultLauncher<Intent>
    private lateinit var mInputPopupWindow: InputPopupWindow
    private lateinit var mSingleSelectPopupWindow: SingleSelectPopupWindow
    private lateinit var mConfirmPopupWindow: ConfirmPopupWindow
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
//        mViewModel=ViewModelProvider(requireActivity()).get(StatusViewModel::class.java)
//        mViewModel.mStatus.value=Repository.getUserStatus()
        initConfig()
    }

    override fun onStart() {
        super.onStart()
        initView()
        initListener()
    }

    /**
     * 配置模式切换监听
     */
    private fun initView() {
        mBinding.toolbarRl.title.text = "我的"
        mBinding.toolbarRl.imgChevronLeft.visibility = View.GONE
        var userData = Repository.getUserData()
        mBinding.nicknameTv.text = Repository.getUserLoginNickname()
        mBinding.emailTv.text =userData.email
        var avatar = userData.avatar
        //判断用户是否有修改模式
        var flag = Repository.getUserChangeAction() != Constants.USER_DEFAULT_MODE
        mBinding.sbIosBtn.isChecked = flag
        if (flag) {
            if (Repository.getUserMode() == Constants.USER_LIGHT_MODE) {
                mBinding.statusChangeIv.setImageResource(R.drawable.ic_24_sun)
            } else {
                mBinding.statusChangeIv.setImageResource(R.drawable.ic_24_moon)
            }
        } else {
            if (TPhoneUtil.getPhoneMode(requireContext()) == Constants.USER_LIGHT_MODE) {
                mBinding.statusChangeIv.setImageResource(R.drawable.ic_24_sun)
            } else {
                mBinding.statusChangeIv.setImageResource(R.drawable.ic_24_moon)
            }
        }
        //头像字符串拼接
        if (avatar != null) {
            var index = avatar.indexOf(".")
            var substring = avatar.substring(index + 1)
            var s = Constants.BASE_AVATAR_URL + substring
            Glide.with(requireContext()).load(s).into(mBinding.iconIv)
        }
    }

    /**
     * 初始化监听
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
            val nicknamePopupWindowListener: PopupWindowListener = object : PopupWindowListener {
                override fun onConfirm(input: String) {
                    mBinding.nicknameTv.text = input
                }

                override fun onCancel() {
                    mInputPopupWindow.dismiss()
                }

                override fun onDismiss() {
                    mInputPopupWindow.dismiss()
                }
            }
            mInputPopupWindow =
                InputPopupWindow(requireContext(), "昵称修改", nicknamePopupWindowListener)
            mInputPopupWindow.mPopupWindow.animationStyle = R.style.t_popup_window_style
            mInputPopupWindow.show()
        }
        //修改性别
        mBinding.sexIv.setOnClickListener {
            val sexPopupWindowListener: PopupWindowListener = object : PopupWindowListener {
                override fun onConfirm(input: String) {

                }

                override fun onCancel() {
                    mSingleSelectPopupWindow.dismiss()
                }

                override fun onDismiss() {
                    mSingleSelectPopupWindow.dismiss()
                }
            }
            mSingleSelectPopupWindow = SingleSelectPopupWindow(
                requireContext(), "选择性别",
                "男", "女", sexPopupWindowListener
            )
            mSingleSelectPopupWindow.setOptions(0)
            mSingleSelectPopupWindow.mPopupWindow.animationStyle = R.style.t_popup_window_style
            mSingleSelectPopupWindow.setConfirmText("确认修改")
            mSingleSelectPopupWindow.setCancelText("取消修改")
            mSingleSelectPopupWindow.show()
        }
        //修改模式
        mBinding.sbIosBtn.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked) {
                TLogUtil.d("选中")
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                Repository.saveUserMode(Constants.DARK_MODE)
                Repository.setUserChangeAction(Constants.USER_CHANGE_MODE)
                TPhoneUtil.showToast(requireContext(), "重启切换夜间模式")
            } else {
                TLogUtil.d("未选中")
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                Repository.saveUserMode(Constants.LIGHT_MODE)
                Repository.setUserChangeAction(Constants.USER_DEFAULT_MODE)
                TPhoneUtil.showToast(requireContext(), "重启切换正常模式")
            }
            Handler(Looper.getMainLooper()).postDelayed({
                requireActivity().startActivity(Intent(requireActivity(), BaseActivity::class.java))
                requireActivity().finish()
                requireActivity().overridePendingTransition(
                    R.anim.t_enter, R.anim.t_close
                )
            }, 1000)
        }
        mBinding.loginConfigIv.setOnClickListener {
            val popupWindowListener: PopupWindowListener = object : PopupWindowListener {
                override fun onConfirm(input: String) {
                    Thread(){
                        Repository.deleteAllTable()
                        Repository.deleteUserId()
                        requireActivity().startActivity(Intent(requireActivity(),LoginActivity::class.java))
                        requireActivity().finish()
                    }.start()
                }
                override fun onCancel() {
                    mConfirmPopupWindow.dismiss()
                }

                override fun onDismiss() {
                    mConfirmPopupWindow.dismiss()
                }
            }
            mConfirmPopupWindow = ConfirmPopupWindow(requireContext(), "确定要退出吗", popupWindowListener)
            mConfirmPopupWindow.setConfirmText("确认")
            mConfirmPopupWindow.setCancelText("我在想想")
            mConfirmPopupWindow.setWarnTextColorType()
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
                val media = selectList[0]
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