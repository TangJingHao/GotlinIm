package com.ByteDance.Gotlin.im.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.PopupWindow
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ByteDance.Gotlin.im.R
import com.ByteDance.Gotlin.im.Repository
import com.ByteDance.Gotlin.im.adapter.TabWithTitleAdapter
import com.ByteDance.Gotlin.im.application.BaseApp
import com.ByteDance.Gotlin.im.databinding.MActivityGroupInviteBinding
import com.ByteDance.Gotlin.im.info.vo.UserVO
import com.ByteDance.Gotlin.im.util.Constants
import com.ByteDance.Gotlin.im.util.Constants.TAG_GROUP_INFO
import com.ByteDance.Gotlin.im.util.DUtils.DSortUtils
import com.ByteDance.Gotlin.im.util.DUtils.diy.ConfirmPopupWindow
import com.ByteDance.Gotlin.im.util.DUtils.diy.PopupWindowListener
import com.ByteDance.Gotlin.im.util.Mutils.MLogUtil.i
import com.ByteDance.Gotlin.im.util.Mutils.MToastUtil.showToast
import com.ByteDance.Gotlin.im.viewmodel.GroupInfoViewModel
import com.qmuiteam.qmui.kotlin.onClick

class GroupInviteActivity : AppCompatActivity() {
    private val mBinding: MActivityGroupInviteBinding by lazy {
        MActivityGroupInviteBinding.inflate(LayoutInflater.from(this))
    }

    private val mViewModel: GroupInfoViewModel by lazy {
        ViewModelProvider(this).get(GroupInfoViewModel::class.java)
    }

    private lateinit var mConfirmPopupWindow: ConfirmPopupWindow

    private val mGroupId :Int by lazy { intent.getIntExtra(Constants.GROUP_ID, 0) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)

        initListener()
        initDate()
        initView()
    }

    private fun initView() {
        mBinding.toolbarGroupInvite.title.text =
            this.resources.getString(R.string.title_info_group_invite)
    }

    private fun initDate() {
        mViewModel.getInviteMembers(mGroupId)
    }

    private fun initListener() {
        mBinding.toolbarGroupInvite.imgChevronLeft.onClick { this.finish() }
        mViewModel.groupInviteLiveData.observe(this) { result ->
            val responseData = result.getOrNull()
            if (responseData == null) {
                i(TAG_GROUP_INFO, "我的好友列表返回值为NULL")
                "我的好友列表返回值为NULL".showToast(this)
            } else {
                // 获取好友列表排序后放入适配器
                val inviteList = ArrayList<UserVO>()
                for (friend in responseData.data.friendList) {
                    inviteList.add(friend.user)
                }
                val titleList = ArrayList<String>();
                val sortFriendList = DSortUtils.sort(inviteList, titleList)
                // 适配器
                val adapter = TabWithTitleAdapter(
                    this,
                    sortFriendList,
                    titleList,
                    TabWithTitleAdapter.TYPE_USER_INFO_SIMPLE
                )

                adapter.setItemOnClickListener { v, groupPosition, relativePosition ->
                    // TODO 跳转事件
//                    TPhoneUtil.showToast(
//                        this,
//                        "group:" + groupPosition + " " + titleList.get(groupPosition) +
//                                "   postion:" + relativePosition +
//                                "   name: " + sortFriendList.get(groupPosition)
//                            .get(relativePosition).nickName
//                    )
                    mConfirmPopupWindow = ConfirmPopupWindow(this,"确定邀请新成员?",object :PopupWindowListener{
                        override fun onConfirm(input: String?) {
                            "已发送邀请".showToast(this@GroupInviteActivity)
                            //Repository.postRequestGroup(mGroupId,"通过群聊添加","")
                        }

                        override fun onCancel() {}

                        override fun onDismiss() {}

                    })
                    mConfirmPopupWindow.show()
                }
                mBinding.rvGroupInvite.layoutManager = LinearLayoutManager(this)
                mBinding.rvGroupInvite.adapter = adapter
                if (sortFriendList.size != 0 && titleList.size != 0)
                    adapter.notifyDataSetChanged()
            }
        }

    }
}