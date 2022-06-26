package com.ByteDance.Gotlin.im.view.activity

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ByteDance.Gotlin.im.R
import com.ByteDance.Gotlin.im.adapter.TabWithTitleAdapter
import com.ByteDance.Gotlin.im.databinding.MActivityGroupMembersBinding
import com.ByteDance.Gotlin.im.info.vo.UserVO
import com.ByteDance.Gotlin.im.util.Constants
import com.ByteDance.Gotlin.im.util.Constants.TAG_GROUP_INFO
import com.ByteDance.Gotlin.im.util.DUtils.DLogUtils
import com.ByteDance.Gotlin.im.util.Mutils.startActivity
import com.ByteDance.Gotlin.im.util.Tutils.TPhoneUtil
import com.ByteDance.Gotlin.im.viewmodel.GroupInfoViewModel

/**
 * @Description：群聊好友页面
 * @Author：Suzy.Mo
 * @Date：2022/6/11 22:03
 */
class GroupMembersActivity : AppCompatActivity() {
    private lateinit var mContext: Context
    private val mGroupId by lazy { intent.getIntExtra(Constants.GROUP_ID,0) }

    private val mBinding: MActivityGroupMembersBinding by lazy {
        MActivityGroupMembersBinding.inflate(LayoutInflater.from(this))
    }

    private val mViewModel: GroupInfoViewModel by lazy {
        ViewModelProvider(this).get(GroupInfoViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
        setContentView(mBinding.root)

        initListener()
        initDate()
        initView()
    }

    private fun initDate() {
        mViewModel.getGroupMembers(mGroupId)
    }

    private fun initView() {
        mBinding.toolbarGroupMembers.title.text = this.resources.getString(R.string.title_info_group_members)
        mBinding.toolbarGroupMembers.imgChevronLeft.setOnClickListener { onBackPressed() }
        mBinding.tabInviteNumbers.tvTitleName.text = this.resources.getString(R.string.title_info_group_invite)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initListener() {
        mBinding.tabInviteNumbers.root.setOnClickListener {
            startActivity<GroupInviteActivity>(this){
                putExtra(Constants.GROUP_ID,mGroupId)
            }
            this.overridePendingTransition(R.anim.t_splash_open,R.anim.t_splash_close)
        }
        mViewModel.groupMemberListLiveData.observe(this) { result ->
            val responseData = result.getOrNull()
            if (responseData == null) {
                DLogUtils.i(TAG_GROUP_INFO, "群聊成员列表返回值为NULL")
                TPhoneUtil.showToast(mContext, "群聊成员列表返回值为NULL")
            } else {
                val groupMemberList = responseData.data.groupUserList
                // TODO (字母顺序)排序
                val dataList: ArrayList<List<UserVO>> = ArrayList()
                dataList.add(groupMemberList)
                val titleList: ArrayList<String> = ArrayList()
                titleList.add("群聊成员列表")
                val adapter = TabWithTitleAdapter(
                    mContext,
                    dataList,
                    titleList,
                    TabWithTitleAdapter.TYPE_USER_INFO_SIMPLE
                )
                adapter.setItemOnClickListener(
                    TabWithTitleAdapter.OnItemClickListener
                    { v, groupPosition, relativePosition ->
                        val memberUserVO = dataList.get(groupPosition).get(relativePosition)
                        TPhoneUtil.showToast(
                            mContext,
                            memberUserVO.userName + " userId:" + memberUserVO.userId
                        )
                        // TODO 跳转到好友详情页
                        startActivity<FriendInfoActivity>(this.mContext) {
                            putExtra(Constants.FRIEND_TYPE,Constants.FRIEND_IS)
                            putExtra(Constants.FRIEND_ACCOUNT,
                                memberUserVO.userId)
                            putExtra(Constants.FRIEND_NAME,
                                memberUserVO.userName)
                            putExtra(Constants.FRIEND_NICKNAME,
                                memberUserVO.nickName)
                            putExtra(Constants.FRIEND_GROUPING,
                                "大学同学")
                        }
                        this.overridePendingTransition(R.anim.t_splash_close,R.anim.t_splash_open)

                    })
                mBinding.rvMember.adapter = adapter
                mBinding.rvMember.layoutManager = LinearLayoutManager(mContext)
                if (groupMemberList.size != 0)
                    adapter.notifyDataSetChanged()
            }
        }
    }

}