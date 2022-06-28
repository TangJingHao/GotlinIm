package com.ByteDance.Gotlin.im.view.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
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
import com.ByteDance.Gotlin.im.databinding.TFragmentMyInfomationBinding
import com.ByteDance.Gotlin.im.info.response.DefaultResponse
import com.ByteDance.Gotlin.im.info.response.ImageData
import com.ByteDance.Gotlin.im.network.base.ServiceCreator
import com.ByteDance.Gotlin.im.network.netInterfaces.ChangeUserDataService
import com.ByteDance.Gotlin.im.network.netInterfaces.SendImageService
import com.ByteDance.Gotlin.im.util.Constants
import com.ByteDance.Gotlin.im.util.DUtils.diy.ConfirmPopupWindow
import com.ByteDance.Gotlin.im.util.DUtils.diy.InputPopupWindow
import com.ByteDance.Gotlin.im.util.DUtils.diy.PopupWindowListener
import com.ByteDance.Gotlin.im.util.DUtils.diy.SingleSelectPopupWindow
import com.ByteDance.Gotlin.im.util.Tutils.TLogUtil
import com.ByteDance.Gotlin.im.util.Tutils.TPhoneUtil
import com.ByteDance.Gotlin.im.util.Tutils.TPictureSelectorUtil.TMyEditMediaIListener
import com.ByteDance.Gotlin.im.view.activity.LoginActivity
import com.ByteDance.Gotlin.im.viewmodel.ChangeUserDataViewModel
import com.bumptech.glide.Glide
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.style.PictureSelectorStyle
import okhttp3.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


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
            val intent = Intent(Intent.ACTION_PICK, null)
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            mPictureLauncher.launch(intent)
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


    // 调用系统的裁剪
    private fun cropPhoto(uri: Uri?) {
        val intent = Intent("com.android.camera.action.CROP")
        intent.setDataAndType(uri, "image/*")
        intent.putExtra("crop", "true")
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1)
        intent.putExtra("aspectY", 1)
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 127)
        intent.putExtra("outputY", 127)
        intent.putExtra("scale", true)
        intent.putExtra("noFaceDetection", false) //不启用人脸识别
        intent.putExtra("return-data", true)
        mCropLauncher.launch(intent)
    }

    /**
     * 头像选择
     */
    private val mPictureLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
            if (activityResult.resultCode == Activity.RESULT_OK) {
                val data = activityResult.data?.data
                cropPhoto(data)
            }
        }

    private val mCropLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
            if (activityResult.resultCode == Activity.RESULT_OK) {
                val data = activityResult.data?.extras
                if (data != null) {
                    val imageBitmap = data.getParcelable<Bitmap>("data")
                    val file = imageBitmap?.let { compressImage(it) }
                    Glide.with(requireContext()).load(imageBitmap).into(mBinding.iconIv)

                    val retrofitHead = Retrofit.Builder()
                        .baseUrl(Constants.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                    val requestBody = RequestBody.create(MediaType.parse("image/*"), file)
                    val createFormData =
                        MultipartBody.Part.createFormData("avatar", file?.name, requestBody)
                    val mSender = retrofitHead.create(SendImageService::class.java)
                    val sendImage = mSender.sendImage(
                        Repository.getToken(),
                        Repository.getUserId(),
                        createFormData
                    )
                    sendImage.enqueue(object : Callback<ImageData> {
                        override fun onResponse(
                            call: Call<ImageData>,
                            response: Response<ImageData>
                        ) {
                            if (response?.body() != null) {
                                val status = response.body()!!.status
                                val avatarName = response.body()!!.data.avatarName
                                imageBitmap?.let { setPicToView(it, avatarName) }
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
                }
            }
        }

    /**
     * bitmap转File
     */
    private fun compressImage(bitmap: Bitmap): File? {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos) //质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        var options = 100
        while (baos.toByteArray().size / 1024 > 20) {  //循环判断如果压缩后图片是否大于20kb,大于继续压缩 友盟缩略图要求不大于18kb
            baos.reset() //重置baos即清空baos
            options -= 10 //每次都减少10
            bitmap.compress(
                Bitmap.CompressFormat.JPEG,
                options,
                baos
            ) //这里压缩options%，把压缩后的数据存放到baos中
            val length: Long = baos.toByteArray().size as Long
        }
        val format = SimpleDateFormat("yyyyMMddHHmmss")
        val date = Date(System.currentTimeMillis())
        //图片名
        val filename: String = format.format(date)
        val file = File(
            Environment.getExternalStorageDirectory(),
            "$filename.png"
        )
        try {
            val fos = FileOutputStream(file)
            try {
                fos.write(baos.toByteArray())
                fos.flush()
                fos.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        return file
    }


    //储存到sd卡的方法
    private fun setPicToView(mBitmap: Bitmap, tempImage: String) {
        val sdStatus = Environment.getExternalStorageState()
        if (sdStatus != Environment.MEDIA_MOUNTED) { // 检测sd是否可用
            return
        }
        val temp = "head"
        val path = Environment.getExternalStorageDirectory().path + "/" + temp
        val tempFile = File(path)
        if (!tempFile.exists()) {
            tempFile.mkdirs()
        }
        var b: FileOutputStream? = null
        val fileName = "$path/$tempImage" //图片名字
        Repository.setUserLoginAvatar(fileName)
        try {
            b = FileOutputStream(fileName)
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, b) // 把数据写入文件
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } finally {
            try {
                //关闭流
                b!!.flush()
                b.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

}

