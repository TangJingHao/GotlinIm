package com.ByteDance.Gotlin.im.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import com.ByteDance.Gotlin.im.R
import com.ByteDance.Gotlin.im.databinding.MActivityFriendSettingBinding
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
        setGroupingData()

    }

    private fun initView() {
        etNickName = mBinding.etRemarks
        etNewGrouping = mBinding.etNewGrouping
        mBinding.toolbarSetFriendInfo.title.text = this.resources.getString(R.string.title_info_friend_setting)
        mBinding.toolbarSetFriendInfo.imgMore.visibility = View.VISIBLE
        etNickName.hint = nickname
        if(newGrouping!=null) etNewGrouping.hint = newGrouping
        else etNewGrouping.hint = this.resources.getString(R.string.edit_hint_new_grouping)

    }

    private fun setListener() {
        //保存按钮 按下则确定保留
        mBinding.toolbarSetFriendInfo.imgMore.onClick {
            nickname = etNickName.text.toString()
        }
        etNickName

    }

    private fun setGroupingData() {
        if(nickname!=null) etNickName.hint = newGrouping
        else etNickName.hint = this.resources.getString(R.string.edit_hint_new_grouping)

    }
}