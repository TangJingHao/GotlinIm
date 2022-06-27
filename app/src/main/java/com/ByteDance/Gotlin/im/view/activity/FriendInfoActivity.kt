package com.ByteDance.Gotlin.im.view.activity

import android.content.Context
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ByteDance.Gotlin.im.R
import com.ByteDance.Gotlin.im.Repository
import com.ByteDance.Gotlin.im.databinding.MActivityFriendInfoBinding
import com.ByteDance.Gotlin.im.info.vo.SessionVO
import com.ByteDance.Gotlin.im.util.Constants
import com.ByteDance.Gotlin.im.util.Constants.FRIEND_ID
import com.ByteDance.Gotlin.im.util.Constants.FRIEND_GROUPING
import com.ByteDance.Gotlin.im.util.Constants.FRIEND_IS
import com.ByteDance.Gotlin.im.util.Constants.FRIEND_NAME
import com.ByteDance.Gotlin.im.util.Constants.FRIEND_NICKNAME
import com.ByteDance.Gotlin.im.util.Constants.FRIEND_NO
import com.ByteDance.Gotlin.im.util.Constants.FRIEND_TYPE
import com.ByteDance.Gotlin.im.util.Constants.TAG_FRIEND_INFO
import com.ByteDance.Gotlin.im.util.DUtils.diy.ConfirmPopupWindow
import com.ByteDance.Gotlin.im.util.DUtils.diy.PopupWindowListener
import com.ByteDance.Gotlin.im.util.Mutils.MLogUtil
import com.ByteDance.Gotlin.im.util.Mutils.MLogUtil.i
import com.ByteDance.Gotlin.im.util.Mutils.MLogUtil.v
import com.ByteDance.Gotlin.im.util.Mutils.MToastUtil.showToast
import com.ByteDance.Gotlin.im.util.Mutils.startActivity
import com.ByteDance.Gotlin.im.viewmodel.FriendInfoViewModel
import com.qmuiteam.qmui.kotlin.onClick
import kotlin.concurrent.thread

/**
 * @Description：好友信息页面，由查找好友后点击进去和在好友列表中点击进入
 * @Author：Suzy.Mo
 * @Date：2022/6/11 21:03
 */

class FriendInfoActivity : AppCompatActivity() {

    private lateinit var mBinding: MActivityFriendInfoBinding
    private val mViewModel by lazy { ViewModelProvider(this).get(FriendInfoViewModel::class.java) }
    private var mFriendType = 1
    private lateinit var tvNickname: TextView
    private lateinit var tvSex: TextView
    private lateinit var tvAccount: TextView
    private lateinit var tvGrouping: TextView
    private lateinit var switchIsCared: Switch
    private lateinit var userName:String
    private lateinit var session:SessionVO
    private lateinit var mDeletePopupWindow:ConfirmPopupWindow
    private val mContext by lazy { this }
    private val friendId by lazy { intent.getIntExtra(Constants.FRIEND_ID,0) }

    companion object{
        /**
         * 好友类型 Constants.FRIEND_IS Constants.FRIEND_NO
         * 好友id，好友名字，好友昵称
         */
        fun startFriendInfoActivity(context: Context, friendId:Int){
            startActivity<FriendInfoActivity>(context){
                putExtra(FRIEND_TYPE,Constants.FRIEND_IS)
                putExtra(FRIEND_ID,friendId)
            }
        }
        fun startFriendInfoFromNotFriend(context: Context, friendId:Int){
            startActivity<FriendInfoActivity>(context){
                putExtra(FRIEND_TYPE,FRIEND_NO)
                putExtra(FRIEND_ID,friendId)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = MActivityFriendInfoBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        initView()
        setListener()
        setPopupWindow()
        setFriendData()
    }

    private fun setPopupWindow() {

        if(mFriendType == FRIEND_IS){
            mDeletePopupWindow = ConfirmPopupWindow(this,"确定删除好友${userName}?",object :PopupWindowListener{
                override fun onConfirm(input: String?) {
                    v(TAG_FRIEND_INFO,"=====确定弹窗监听=====")
                    "暂无后台删除接口".showToast(this@FriendInfoActivity)
                }
                override fun onCancel() { }

                override fun onDismiss() { }
            })
        }else{
            mDeletePopupWindow = ConfirmPopupWindow(this,"确定向${userName}发送好友申请?",object: PopupWindowListener{
                override fun onConfirm(input: String?) {
                    v(TAG_FRIEND_INFO,"=====输入弹窗监听=====")
                    Repository.postRequestFriend(friendId,"通过ID"," ")
                }
                override fun onCancel() {  }

                override fun onDismiss() { }

            })
        }
    }

    /**
     * 设置界面的数据
     */
    private fun setFriendData() {
        mViewModel.friendInfoLiveData.observe(this, Observer { result->
            if(result!=null){
                //tvNickname.text = result
                v(TAG_FRIEND_INFO,"======返回了数据库的好友信息=====")
                //设置好友信息
                userName = result.userName
                mBinding.tvFriendName.text =userName
                mBinding.tvSex.text =result.sex
                mBinding.tvNickname.text=result.nickName
                mBinding.tvAccount.text =result.userId.toString()
                mBinding.tvGrouping.text =this.resources.getString(R.string.grouping_default)
//                mBinding.tvFriendName.text = intent.getStringExtra(Constants.FRIEND_NAME)
//                mBinding.tvNickname.text= intent.getStringExtra(Constants.FRIEND_NICKNAME)
//                mBinding.tvAccount.text = intent.getStringExtra(FRIEND_ACCOUNT)
//                mBinding.tvGrouping.text = intent.getStringExtra(Constants.FRIEND_GROUPING)
            }
        })
    }

    /**
     * 设置点击事件
     */
    private fun setListener() {
        mBinding.toolbarFriendInfo.imgChevronLeft.onClick {
            this.finish()
            v(TAG_FRIEND_INFO,"--返回--")
        }
        mBinding.tabSetRemarks.root.onClick {
            v(TAG_FRIEND_INFO,"--点击设置备注和分组->>SetFriendInfoActivity--")
            startActivity<FriendSettingActivity>(this){
                putExtra(FRIEND_NICKNAME,intent.getStringExtra(Constants.FRIEND_NICKNAME))
                putExtra(FRIEND_GROUPING,intent.getStringExtra(Constants.FRIEND_GROUPING))
            }
        }
        mBinding.tabAddOrStart.root.onClick {
            if (mFriendType == FRIEND_IS){
                //TODO:跳转到聊天页面
                session.let {
                    v(TAG_FRIEND_INFO,"=====获取了Session>>>开始聊天=====")
                    ChatActivity.startChat(this@FriendInfoActivity,it)
                }

            }else if (mFriendType == FRIEND_NO){
                v(TAG_FRIEND_INFO,"=====添加好友=====")
                //TODO:弹窗添加好友
                mDeletePopupWindow.show()
            }
        }
        mBinding.tabDeleteFriend.root.onClick {
            v(TAG_FRIEND_INFO,"--删除好友--")
            //TODO:弹窗删除好友
            mDeletePopupWindow.show()
        }
        mBinding.tabItemInfoSearch.root.onClick {
            v(TAG_FRIEND_INFO,"--消息搜索--")
            //TODO:跳转到搜索聊天消息,需要会话id
            SearchActivity.startMsgSearch(this,session.sessionId)
        }
    }

    /**
     * 初始化界面 判断好友类别
     */
    private fun initView() {
        //绑定
        tvNickname = mBinding.tvNickname
        tvSex = mBinding.tvSex
        tvAccount = mBinding.tvAccount
        tvGrouping = mBinding.tvGrouping
        switchIsCared = mBinding.switchIsCared
        //获取类型并设置账号获取信息
        mFriendType = intent.getIntExtra(FRIEND_TYPE, 0)
        mViewModel.getFriendInfo(intent.getIntExtra(FRIEND_ID,0))
        userName = intent.getStringExtra(FRIEND_NAME).toString()
        //控件初始化
        mBinding.toolbarFriendInfo.title.text = this.resources.getString(R.string.title_info_friend)
        mBinding.tabSetRemarks.tvItemMainText.text =
            this.resources.getString(R.string.tab_text_set_friend_info)
        mBinding.tabItemInfoSearch.tvItemMainText.text =
            this.resources.getString(R.string.tab_text_search_info)
        mBinding.tabItemInfoSearch.tvItemAuxiliaryText.visibility = View.INVISIBLE
        mBinding.tabSetRemarks.tvItemAuxiliaryText.visibility = View.INVISIBLE
        if (mFriendType == FRIEND_IS) {
            mBinding.tabAddOrStart.tvBlue.text = this.resources.getString(R.string.tab_blue_start)
            mBinding.tabDeleteFriend.tvRed.text =
                this.resources.getString(R.string.tab_red_delete_friend)
        } else if (mFriendType == FRIEND_NO) {
            mBinding.tabAddOrStart.tvBlue.text = this.resources.getString(R.string.tab_blue_add)
            mBinding.cvDelete.visibility = View.INVISIBLE
            mBinding.tvCare.text = resources.getString(R.string.tab_text_care_not)
            switchIsCared.isClickable = false
        }
        thread {
            val sessionVO =Repository.querySessionByUid(friendId)
            runOnUiThread {
                session = sessionVO
                v(TAG_FRIEND_INFO,"信息为===${sessionVO.name};${sessionVO.sessionId};${sessionVO.number}")
            }
        }

//        //获取好友session
//        thread{
//            session = Repository.querySessionByName(userName)
//            i(TAG_FRIEND_INFO,"好友会话信息：${session.sessionId.toString()+session.name}")
//
//        }
    }

}