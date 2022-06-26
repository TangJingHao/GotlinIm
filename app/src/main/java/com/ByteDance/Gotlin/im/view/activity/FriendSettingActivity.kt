package com.ByteDance.Gotlin.im.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ByteDance.Gotlin.im.R
import com.ByteDance.Gotlin.im.databinding.MActivityFriendSettingBinding
import com.ByteDance.Gotlin.im.util.Constants
import com.ByteDance.Gotlin.im.util.Mutils.MToastUtil.showToast
import com.ByteDance.Gotlin.im.viewmodel.FriendInfoViewModel
import com.qmuiteam.qmui.kotlin.onClick

/**
 * @Description：
 * @Author：Suzy.Mo
 * @Date：2022/6/12 19:46
 */
class FriendSettingActivity : AppCompatActivity() {

    private lateinit var mBinding: MActivityFriendSettingBinding
    private val mViewModel by lazy { ViewModelProvider(this).get(FriendInfoViewModel::class.java) }
    private var nickname = ""
    private var newGrouping = ""
    private lateinit var etNickName:EditText
    private lateinit var etNewGrouping:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = MActivityFriendSettingBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        initView()
        setListener()
        //setGroupingData()
    }

    private fun initView() {
        etNickName = mBinding.etRemarks
        etNewGrouping = mBinding.etNewGrouping
        mBinding.toolbarSetFriendInfo.title.text = this.resources.getString(R.string.title_info_friend_setting)
        mBinding.toolbarSetFriendInfo.imgMore.visibility = View.VISIBLE
        etNickName.hint = nickname
        if(newGrouping!=null) etNewGrouping.hint = newGrouping
        else etNewGrouping.hint = this.resources.getString(R.string.edit_hint_new_grouping)

        etNickName.hint = intent.getStringExtra(Constants.FRIEND_NICKNAME).toString()
        //etNewGrouping.hint = intent.getStringExtra(Constants.FRIEND_GROUPING).toString()
    }

    private fun setListener() {
        //返回
        mBinding.toolbarSetFriendInfo.imgChevronLeft.onClick {
            this.finish()
        }
        //保存按钮 按下则确定保留
        mBinding.toolbarSetFriendInfo.imgMore.onClick {
            if(nickname != etNickName.text.toString()){
                mViewModel.saveNickName(etNickName.text.toString())
            }else{
                "没有修改不用保存!".showToast(this)
            }
        }
        //数据刷新
        mViewModel.nickNameLiveData.observe(this, Observer {
            "保存修改成功".showToast(this)
            etNickName.hint = it
        })

    }

    private fun setGroupingData() {
        if(nickname!=null) etNickName.hint = newGrouping
        else etNickName.hint = this.resources.getString(R.string.edit_hint_new_grouping)

    }
}