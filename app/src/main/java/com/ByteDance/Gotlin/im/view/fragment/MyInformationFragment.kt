package com.ByteDance.Gotlin.im.view.fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.*
import android.provider.MediaStore
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
import androidx.lifecycle.ViewModelProvider
import com.ByteDance.Gotlin.im.R
import com.ByteDance.Gotlin.im.Repository
import com.ByteDance.Gotlin.im.application.BaseActivity
import com.ByteDance.Gotlin.im.bean.ChangeUserInfoBean
import com.ByteDance.Gotlin.im.databinding.TFragmentMyInfomationBinding
import com.ByteDance.Gotlin.im.info.response.DefaultResponse
import com.ByteDance.Gotlin.im.info.response.ImageData
import com.ByteDance.Gotlin.im.model.ChangeUserInfo
import com.ByteDance.Gotlin.im.network.base.ServiceCreator
import com.ByteDance.Gotlin.im.network.netInterfaces.ChangeUserDataService
import com.ByteDance.Gotlin.im.network.netInterfaces.RequestService
import com.ByteDance.Gotlin.im.network.netInterfaces.SendImageService
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
import com.ByteDance.Gotlin.im.viewmodel.ChangeUserDataViewModel
import com.bumptech.glide.Glide
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.config.SelectModeConfig
import com.luck.picture.lib.style.PictureSelectorStyle
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File


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
    private lateinit var mViewModel: ChangeUserDataViewModel
    private var mSelectorStyle = PictureSelectorStyle()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = TFragmentMyInfomationBinding.inflate(inflater, container, false)
        mViewModel = ViewModelProvider(requireActivity()).get(ChangeUserDataViewModel::class.java)
        return mBinding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initConfig()
    }

    override fun onStart() {
        super.onStart()
        initView()
        initListener()
        initCallBack()
    }

    private fun initCallBack() {

    }

    /**
     * 配置模式切换监听
     */
    private fun initView() {
        mBinding.toolbarRl.title.text = "我的"
        mBinding.toolbarRl.imgChevronLeft.visibility = View.GONE
        var userData = Repository.getUserData()
        mBinding.nicknameTv.text = Repository.getUserLoginNickname()
        mBinding.emailTv.text = userData.email

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
        Glide.with(requireContext()).load(Repository.getUserLoginAvatar()).into(mBinding.iconIv)
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
                    val changeUserInfo = ServiceCreator.create<ChangeUserDataService>()
                        .changeUserInfo(
                            Repository.getToken(),
                            Repository.getUserId(),
                            Repository.getUserLoginSex(),
                            input
                        )
                    changeUserInfo.enqueue(object : Callback<DefaultResponse> {
                        override fun onResponse(
                            call: Call<DefaultResponse>,
                            response: Response<DefaultResponse>
                        ) {
                            val body = response.body()
                            if (body != null) {
                                if (body?.status == Constants.SUCCESS_STATUS) {
                                    TPhoneUtil.showToast(requireContext(), "修改成功")
                                    Repository.setUserLoginNickname(input)
                                }
                            }
                        }

                        override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                            t.printStackTrace()
                            TLogUtil.d("修改失败")
                        }

                    })
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
                    val changeUserInfo = ServiceCreator.create<ChangeUserDataService>()
                        .changeUserInfo(
                            Repository.getToken(),
                            Repository.getUserId(),
                            input,
                            Repository.getUserLoginNickname()
                        )
                    changeUserInfo.enqueue(object : Callback<DefaultResponse> {
                        override fun onResponse(
                            call: Call<DefaultResponse>,
                            response: Response<DefaultResponse>
                        ) {
                            val body = response.body()
                            if (body != null) {
                                if (body?.status == Constants.SUCCESS_STATUS) {
                                    TPhoneUtil.showToast(requireContext(), "修改成功")
                                    Repository.setUserLoginSex(input)
                                }
                            }
                        }

                        override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                            t.printStackTrace()
                            TLogUtil.d("修改失败")
                        }

                    })
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
            if (Repository.getUserLoginSex() == "男") {
                mSingleSelectPopupWindow.setOptions(0)
            } else if (Repository.getUserLoginSex() == "女") {
                mSingleSelectPopupWindow.setOptions(1)
            }
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
                    Thread() {
                        Repository.deleteAllTable()
                        Repository.deleteUserId()
                        requireActivity().startActivity(
                            Intent(
                                requireActivity(),
                                LoginActivity::class.java
                            )
                        )
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
            mConfirmPopupWindow =
                ConfirmPopupWindow(requireContext(), "确定要退出吗", popupWindowListener)
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
                TLogUtil.d(media.realPath)
                var filepath = media.availablePath
                if (cut) {
                    //是裁剪过的
                    Glide.with(requireContext()).load(media.cutPath).into(mBinding.iconIv)
                    Repository.setUserLoginAvatar(media.cutPath)
                } else { //没有裁剪过的
                    Glide.with(requireContext()).load(media.path).into(mBinding.iconIv)
                    Repository.setUserLoginAvatar(media.path)
                }
                val retrofitHead = Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                val file = File(Environment.getExternalStorageDirectory().absolutePath + filepath)
                val requestBody = RequestBody.create(MediaType.parse("image/*"), file)
                val createFormData =
                    MultipartBody.Part.createFormData("avatar", file.name, requestBody)
                val mSender = retrofitHead.create(SendImageService::class.java)
                val sendImage =
                    mSender.sendImage(Repository.getToken(), Repository.getUserId(), createFormData)
                sendImage.enqueue(object : Callback<ImageData> {
                    override fun onResponse(call: Call<ImageData>, response: Response<ImageData>) {
                        if (response?.body() != null) {
                            val status = response.body()!!.status
                            if (status == Constants.SUCCESS_STATUS) {
                                TLogUtil.d("头像上传成功")
                                TPhoneUtil.showToast(requireContext(), "头像上传成功")
                            }
                        }
                    }

                    override fun onFailure(call: Call<ImageData>, t: Throwable) {
                        t.printStackTrace()
                        TLogUtil.d("头像上传失败")
                    }
                })
            } else if (resultCode == AppCompatActivity.RESULT_CANCELED) {
                TLogUtil.d("点击取消")
            }
        }
    }

}

