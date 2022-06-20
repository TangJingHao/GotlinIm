package com.ByteDance.Gotlin.im.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Switch
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ByteDance.Gotlin.im.R
import com.ByteDance.Gotlin.im.databinding.MActivityFriendInfoBinding
import com.ByteDance.Gotlin.im.databinding.MActivityGroupInfoBinding
import com.ByteDance.Gotlin.im.util.Constants
import com.ByteDance.Gotlin.im.util.Constants.OWNER_IS
import com.ByteDance.Gotlin.im.util.Constants.OWNER_NO
import com.ByteDance.Gotlin.im.util.Mutils.MLogUtil
import com.ByteDance.Gotlin.im.util.Mutils.MToastUtil.showToast
import com.ByteDance.Gotlin.im.util.Mutils.startActivity
import com.ByteDance.Gotlin.im.viewmodel.FriendInfoViewModel
import com.ByteDance.Gotlin.im.viewmodel.GroupInfoViewModel
import com.qmuiteam.qmui.kotlin.onClick

/**
 * @Description：群聊信息页面
 * @Author：Suzy.Mo
 * @Date：2022/6/11 22:03
 */

class GroupInfoActivity : AppCompatActivity() {

    private lateinit var mBinding: MActivityGroupInfoBinding
    private val mViewModel by lazy { ViewModelProvider(this).get(GroupInfoViewModel::class.java) }
    private var mOwnerType = 0
    private val groupId by lazy { intent.getIntExtra(Constants.GROUP_ID,0) }
    private lateinit var tvName : TextView
    private lateinit var tvGroupName : TextView
    private lateinit var tvGroupOwner : TextView
    private lateinit var tvGroupId : TextView
    private lateinit var tvGroupMembers : TextView
    private lateinit var tvGroupMyName : TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = MActivityGroupInfoBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        initView()
        setListener()
        setGroupData()
    }

    private fun initView() {
        mOwnerType = intent.getIntExtra(Constants.OWNER_TYPE,0)
        mViewModel.getGroupInfo(groupId)
        mBinding.toolbarGroupInfo.title.text = this.resources.getString(R.string.title_info_group)
        mBinding.tabGroupName.tvItemMainText.text = this.resources.getString(R.string.tab_text_group_name)
        mBinding.tabGroupNumbers.tvItemMainText.text = this.resources.getString(R.string.tab_text_group_members)
        mBinding.tabGroupNickname.tvItemMainText.text = this.resources.getString(R.string.tab_text_group_my_nickname)
        mBinding.tabItemInfoSearch.tvItemMainText.text = this.resources.getString(R.string.tab_text_group_search)
        mBinding.tabItemInfoSearch.tvItemAuxiliaryText.visibility = View.INVISIBLE
        if (mOwnerType == OWNER_IS){
            mBinding.tabDeleteGroup.tvRed.text = this.resources.getString(R.string.tab_red_group_delete)
        }else if(mOwnerType == OWNER_NO){
            mBinding.tabDeleteGroup.tvRed.text = this.resources.getString(R.string.tab_red_group_out)
        }
    }

    private fun setListener() {
        mBinding.toolbarGroupInfo.imgChevronLeft.onClick {
            this.finish()
            MLogUtil.v(Constants.TAG_FRIEND_INFO,"--返回--")
            mViewModel.getGroupInfo(groupId)
        }
        mBinding.tabGroupName.root.onClick {
            MLogUtil.v(Constants.TAG_GROUP_INFO,"--群聊名称--")
            if (mOwnerType == OWNER_IS){
                "群主可修改".showToast(this)
            }else if(mOwnerType == OWNER_NO){
                "不是群主不可修改".showToast(this)
            }
        }
        mBinding.tabGroupNumbers.root.onClick {
            MLogUtil.v(Constants.TAG_GROUP_INFO,"--群聊人数：跳转到成员列表--")
            startActivity<GroupMembersActivity>(this){
                putExtra(Constants.GROUP_ID,groupId)
            }

        }
        mBinding.tabGroupNickname.root.onClick { MLogUtil.v(Constants.TAG_GROUP_INFO,"--我的群昵称（弹窗修改）--") }
        mBinding.tabItemInfoSearch.root.onClick {
            MLogUtil.v(Constants.TAG_GROUP_INFO,"--群聊消息搜索跳转--")
            //TODO:跳转到搜索聊天消息
            startActivity<SearchActivity>(this) {
                putExtra(Constants.SEARCH_FROM_INFO_TYPE, Constants.SEARCH_TYPE_FROM_GROUP)
            }


        }
        mBinding.tabDeleteGroup.root.onClick {
            MLogUtil.v(Constants.TAG_GROUP_INFO,"--删除群聊--")
            if (mOwnerType == OWNER_IS){
                "确定解散群聊".showToast(this)
            }else if(mOwnerType == OWNER_NO){
                "确定退出群聊".showToast(this)
            }
        }
    }

    private fun setGroupData() {
        mViewModel.groupInfoLiveData.observe(this, Observer {
            mBinding.tvGroupId.text = intent.getIntExtra(Constants.GROUP_ID,0).toString()
            mBinding.tvName.text =intent.getStringExtra(Constants.GROUP_NAME).toString()
            mBinding.tabGroupName.tvItemAuxiliaryText.text = intent.getStringExtra(Constants.GROUP_NAME).toString()
            mBinding.tabGroupNumbers.tvItemAuxiliaryText.text =intent.getStringExtra(Constants.GROUP_NUM).toString()
            mBinding.tvBuilder.text =intent.getStringExtra(Constants.GROUP_OWNER).toString()
            mBinding.tabGroupNickname.tvItemAuxiliaryText.text = intent.getStringExtra(Constants.GROUP_MY_NAME).toString()
//            mBinding.tvGroupId.text = it
//            mBinding.tvName.text = "编译原理群"
//            mBinding.tabGroupName.tvItemAuxiliaryText.text = "编译原理群"
//            mBinding.tabGroupNumbers.tvItemAuxiliaryText.text = "50人"
//            mBinding.tvBuilder.text ="李老师"
//            mBinding.tabGroupNickname.tvItemAuxiliaryText.text = "20软卓副班长"
        })
    }

}