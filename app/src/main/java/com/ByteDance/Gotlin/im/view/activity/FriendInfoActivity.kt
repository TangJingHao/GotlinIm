package com.ByteDance.Gotlin.im.view.activity

import android.accounts.Account
import android.os.Bundle
import android.view.View
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ByteDance.Gotlin.im.R
import com.ByteDance.Gotlin.im.databinding.MActivityFriendInfoBinding
import com.ByteDance.Gotlin.im.util.Constants.FRIEND_ACCOUNT
import com.ByteDance.Gotlin.im.util.Constants.FRIEND_IS
import com.ByteDance.Gotlin.im.util.Constants.FRIEND_NO
import com.ByteDance.Gotlin.im.util.Constants.FRIEND_TYPE
import com.ByteDance.Gotlin.im.util.Constants.TAG_FRIEND_INFO
import com.ByteDance.Gotlin.im.util.Mutils.MLogUtil
import com.ByteDance.Gotlin.im.viewmodel.FriendInfoViewModel
import com.qmuiteam.qmui.kotlin.onClick

/**
 * @Description：好友信息页面，由查找好友后点击进去和在好友列表中点击进入
 *              需要用户唯一标识（账号）和是否为好友
 * @Author：Suzy.Mo
 * @Date：2022/6/11 21:03
 */

class FriendInfoActivity : AppCompatActivity(){

    private lateinit var mBinding: MActivityFriendInfoBinding
    private val mViewModel by lazy { ViewModelProvider(this).get(FriendInfoViewModel::class.java) }
    private var mFriendType = 0
    private lateinit var tvNickname :TextView
    private lateinit var tvSex :TextView
    private lateinit var tvAccount :TextView
    private lateinit var tvGrouping :TextView
    private lateinit var switchIsCared :Switch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = MActivityFriendInfoBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        initView()
        setListener()
        setFriendData()
    }

    /**
     * 设置界面的数据
     */
    private fun setFriendData() {
        mViewModel.friendInfoLiveData.observe(this, Observer { result->
            if(result!=null){
                tvNickname.text = result
                //设置好友信息
                //mBinding.tvFriendName.text =
                //mBinding.tvSex.text =
                //mBinding.tvNickname.text=
                //mBinding.tvAccount.text =
                //mBinding.tvGrouping.text =
            }
        })
    }


    /**
     * 设置点击事件
     */
    private fun setListener() {
        mBinding.toolbarFriendInfo.imgChevronLeft.onClick {
            //this.finish()
            MLogUtil.v(TAG_FRIEND_INFO,"--返回--")
        }
        mBinding.tabSetRemarks.root.onClick { MLogUtil.v(TAG_FRIEND_INFO,"--点击设置备注和分组->>SetFriendInfoActivity--") }
        mBinding.tabAddOrStart.root.onClick {
            if (mFriendType == FRIEND_IS){
                MLogUtil.v(TAG_FRIEND_INFO,"--开始聊天--")
            }else if (mFriendType == FRIEND_NO){
                MLogUtil.v(TAG_FRIEND_INFO,"--添加好友--")
            } }
        mBinding.tabDeleteFriend.root.onClick { MLogUtil.v(TAG_FRIEND_INFO,"--删除好友--") }
        mBinding.tabItemInfoSearch.root.onClick { MLogUtil.v(TAG_FRIEND_INFO,"--消息搜索--")  }

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
        mFriendType = intent.getIntExtra(FRIEND_TYPE,0)
        mViewModel.getFriendInfo(intent.getStringExtra(FRIEND_ACCOUNT).toString())
        //控件初始化
        mBinding.toolbarFriendInfo.title.text = this.resources.getString(R.string.title_info_friend)
        mBinding.tabSetRemarks.tvItemMainText.text = this.resources.getString(R.string.tab_text_set_friend_info)
        mBinding.tabItemInfoSearch.tvItemMainText.text = this.resources.getString(R.string.tab_text_search_info)
        mBinding.tabItemInfoSearch.tvItemAuxiliaryText.visibility = View.INVISIBLE
        mBinding.tabSetRemarks.tvItemAuxiliaryText.visibility = View.INVISIBLE
        if (mFriendType == FRIEND_IS) {
            mBinding.tabAddOrStart.tvBlue.text = this.resources.getString(R.string.tab_blue_start)
            mBinding.tabDeleteFriend.tvRed.text = this.resources.getString(R.string.tab_red_delete_friend)
        } else if (mFriendType == FRIEND_NO) {
            mBinding.tabAddOrStart.tvBlue.text = this.resources.getString(R.string.tab_blue_add)
            mBinding.cvDelete.visibility = View.INVISIBLE
            mBinding.tvCare.text = resources.getString(R.string.tab_text_care_not)
            switchIsCared.isClickable = false
        }

    }

}