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
import androidx.fragment.app.Fragment
import com.ByteDance.Gotlin.im.databinding.TFragmentMyInfomationBinding
import com.ByteDance.Gotlin.im.util.Constants
import com.ByteDance.Gotlin.im.util.DUtils.diy.InputPopupWindow
import com.ByteDance.Gotlin.im.util.Tutils.TLogUtil
import com.ByteDance.Gotlin.im.util.Tutils.TPhoneUtil
import com.ByteDance.Gotlin.im.util.Tutils.TPictureSelectorUtil.TGlideEngine
import com.ByteDance.Gotlin.im.util.Tutils.TPictureSelectorUtil.TMyEditMediaIListener
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

class MyInformationFragment : Fragment() {
    private lateinit var mBinding: TFragmentMyInfomationBinding
    private lateinit var mMyEditMediaIListener: TMyEditMediaIListener
    private lateinit var mLauncherResult: ActivityResultLauncher<Intent>
    private lateinit var mInputPopupWindow: InputPopupWindow
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

    }

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
        //模式切换监听
        mBinding.sbIosBtn.setOnClickListener {

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