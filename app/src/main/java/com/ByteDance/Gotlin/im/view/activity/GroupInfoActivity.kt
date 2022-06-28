package com.ByteDance.Gotlin.im.view.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.PopupWindow
import android.widget.Switch
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ByteDance.Gotlin.im.R
import com.ByteDance.Gotlin.im.Repository
import com.ByteDance.Gotlin.im.databinding.MActivityFriendInfoBinding
import com.ByteDance.Gotlin.im.databinding.MActivityGroupInfoBinding
import com.ByteDance.Gotlin.im.info.vo.GroupVO
import com.ByteDance.Gotlin.im.info.vo.SessionVO
import com.ByteDance.Gotlin.im.util.Constants
import com.ByteDance.Gotlin.im.util.Constants.GROUP_IS
import com.ByteDance.Gotlin.im.util.Constants.GROUP_NO
import com.ByteDance.Gotlin.im.util.Constants.GROUP_SESSION_ID
import com.ByteDance.Gotlin.im.util.Constants.GROUP_VO
import com.ByteDance.Gotlin.im.util.Constants.OWNER_IS
import com.ByteDance.Gotlin.im.util.Constants.OWNER_NO
import com.ByteDance.Gotlin.im.util.Constants.OWNER_TYPE
import com.ByteDance.Gotlin.im.util.Constants.TAG_GROUP_INFO
import com.ByteDance.Gotlin.im.util.DUtils.diy.ConfirmPopupWindow
import com.ByteDance.Gotlin.im.util.DUtils.diy.InputPopupWindow
import com.ByteDance.Gotlin.im.util.DUtils.diy.PopupWindowListener
import com.ByteDance.Gotlin.im.util.Mutils.MLogUtil
import com.ByteDance.Gotlin.im.util.Mutils.MLogUtil.v
import com.ByteDance.Gotlin.im.util.Mutils.MToastUtil.showToast
import com.ByteDance.Gotlin.im.util.Mutils.startActivity
import com.ByteDance.Gotlin.im.viewmodel.FriendInfoViewModel
import com.ByteDance.Gotlin.im.viewmodel.GroupInfoViewModel
import com.qmuiteam.qmui.kotlin.onClick
import kotlin.concurrent.thread
import kotlin.properties.Delegates

/**
 * @Description：群聊信息页面
 *               跳转需要群的所有信息
 * @Author：Suzy.Mo
 * @Date：2022/6/11 22:03
 */

class GroupInfoActivity : AppCompatActivity() {

    private lateinit var mBinding: MActivityGroupInfoBinding
    private val mViewModel by lazy { ViewModelProvider(this).get(GroupInfoViewModel::class.java) }
    private val mOwnerType by lazy { intent.getIntExtra(Constants.OWNER_TYPE,0) }
    private var mGroupType by Delegates.notNull<Int>()
    private var groupId by Delegates.notNull<Int>()
    private lateinit var groupName :String
    private lateinit var mGroupVO: GroupVO
    private lateinit var mSession :SessionVO
    private var mSessionId by Delegates.notNull<Int>()
    private lateinit var mConfirmPW: ConfirmPopupWindow
    private lateinit var mInputPW: InputPopupWindow
    private var INPUT_GROUP_NAME = 1
    private var INPUT_NICK_NAME = 0
    private var mInputType :Int = INPUT_GROUP_NAME

    companion object{

        fun startGroupInfoActivity(context: Context, groupVO: GroupVO){
            var groupType = if (groupVO.creatorId==Repository.getUserId()) OWNER_IS
            else OWNER_NO
            startActivity<GroupInfoActivity>(context){
                putExtra(GROUP_VO,groupVO)
                putExtra(OWNER_TYPE,groupType)
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = MActivityGroupInfoBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        mGroupVO = intent.getSerializableExtra(GROUP_VO) as GroupVO
        mSessionId = mGroupVO.sessionId
        mGroupType = GROUP_IS
        groupId = mGroupVO.groupId
        groupName = mGroupVO.groupName

        initView()
        setListener()
        setPopupWindow()
        setGroupData()
    }

    private fun setPopupWindow() {
        //是群里面的 分为是群主还是不是群主
        if(mGroupType== GROUP_IS){
            val groupTye :String = if (mOwnerType == OWNER_IS){
                this.resources.getString(R.string.tab_red_group_delete)
            }else {
                this.resources.getString(R.string.tab_red_group_out)
            }
            mConfirmPW = ConfirmPopupWindow(this,"确定$groupTye${groupName}?",object :
                PopupWindowListener {
                override fun onConfirm(input: String?) {
                    "退群成功".showToast(this@GroupInfoActivity)
                }
                override fun onCancel() { }

                override fun onDismiss() { }
            })
        }else {
            mConfirmPW = ConfirmPopupWindow(this,"确定向${groupName}发送群聊申请?",object :
                PopupWindowListener {
                override fun onConfirm(input: String?) {
                    Repository.postRequestGroup(groupId,"","")
                }
                override fun onCancel() { }

                override fun onDismiss() { }
            })
        }

        mInputPW = InputPopupWindow(this,"请输入新的名称",object:PopupWindowListener{
            override fun onConfirm(input: String?) {
                if(mInputType==INPUT_GROUP_NAME){
                    mBinding.tabGroupName.tvItemAuxiliaryText.text = input
                    mBinding.tvName.text = input
                    //TODO:修改名称
                }else{
                    mBinding.tabGroupNickname.tvItemAuxiliaryText.text = input
                }
            }

            override fun onDismiss() {}

            override fun onCancel() {}

        })
    }

    private fun initView() {
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
        if (mGroupType == GROUP_IS){
            mBinding.tabAddStartGroup.tvBlue.text = this.resources.getString(R.string.tab_red_group_start)
            mBinding.tabItemInfoSearch.root.visibility = View.VISIBLE
        }else if(mGroupType == GROUP_NO){
            mBinding.tabAddStartGroup.tvBlue.text = this.resources.getString(R.string.tab_red_group_add)
            mBinding.tabLayoutGroup.visibility = View.INVISIBLE
        }

    }

    private fun setListener() {
        mBinding.toolbarGroupInfo.imgChevronLeft.onClick {
            this.finish()
            v(Constants.TAG_FRIEND_INFO,"--返回--")
            mViewModel.getGroupInfo(groupId)
        }
        mBinding.tabGroupName.root.onClick {
            v(Constants.TAG_GROUP_INFO,"--群聊名称--")
            if (mOwnerType == OWNER_IS){
                "群主可修改".showToast(this)
                //TODO:弹窗修改
                mInputType = INPUT_GROUP_NAME
                mInputPW.show()

            }else if(mOwnerType == OWNER_NO){
                "不是群主不可修改".showToast(this)
            }
        }
        mBinding.tabGroupNumbers.root.onClick {
            v(Constants.TAG_GROUP_INFO,"--群聊人数：跳转到成员列表--")
            startActivity<GroupMembersActivity>(this){
                putExtra(Constants.GROUP_ID,groupId)
            }
            this.overridePendingTransition(R.anim.t_splash_open,R.anim.t_splash_close)
        }
        mBinding.tabGroupNickname.root.onClick {
            v(Constants.TAG_GROUP_INFO,"--我的群昵称（弹窗修改）--")
            //TODO:弹窗修改界面
            mInputType = INPUT_NICK_NAME
            mInputPW.show()
        }
        mBinding.tabItemInfoSearch.root.onClick {
            v(Constants.TAG_GROUP_INFO,"--群聊消息搜索跳转--")
            //TODO:跳转到搜索聊天消息,需要会话id
            SearchActivity.startMsgSearch(this,mSession.sessionId )
        }
        mBinding.tabDeleteGroup.root.onClick {
            v(Constants.TAG_GROUP_INFO,"--删除群聊:解散群聊or退出群聊--")
            mConfirmPW.show()
        }
        mBinding.tabAddStartGroup.root.onClick {
            if (mGroupType == GROUP_IS){
                v(Constants.TAG_GROUP_INFO,"----开始聊天----")
                //TODO:跳转到聊天
                ChatActivity.startChat(this,mSession)
            }else if(mGroupType == GROUP_NO){
                //TODO:弹窗进行申请加入
                mConfirmPW.show()
            }
        }
    }

    private fun setGroupData() {
        mViewModel.groupInfoLiveData.observe(this, Observer {result->
            mBinding.tvGroupId.text = mGroupVO.groupId.toString()
            mBinding.tvName.text =mGroupVO.groupName
            mBinding.tabGroupName.tvItemAuxiliaryText.text = mGroupVO.groupName
            mBinding.tabGroupNumbers.tvItemAuxiliaryText.text =mGroupVO.number.toString()
            thread {
                val userVO = Repository.queryUserByIdReturnName(mGroupVO.creatorId)
                runOnUiThread {
                    if (userVO!=null){
                        mBinding.tvBuilder.text =userVO.userName
                    }else{
                        mBinding.tvBuilder.text = mGroupVO.creatorId.toString()
                    }
                }
            }
            mBinding.tabGroupNickname.tvItemAuxiliaryText.text = Repository.getUserLoginNickname()
//            mBinding.tvGroupId.text = it
//            mBinding.tvName.text = "编译原理群"
//            mBinding.tabGroupName.tvItemAuxiliaryText.text = "编译原理群"
//            mBinding.tabGroupNumbers.tvItemAuxiliaryText.text = "50人"
//            mBinding.tvBuilder.text ="李老师"
//            mBinding.tabGroupNickname.tvItemAuxiliaryText.text = "20软卓副班长"
        })

        thread {
            v(TAG_GROUP_INFO,"===获取session===")
            val session = Repository.querySessionByGroupId(mGroupVO.groupId)
            runOnUiThread {
                v(TAG_GROUP_INFO,"===成功得到session===")
                mSession = session
            }
        }
    }

}