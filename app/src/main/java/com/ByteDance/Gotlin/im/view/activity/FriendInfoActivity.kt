package com.ByteDance.Gotlin.im.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ByteDance.Gotlin.im.R
import com.ByteDance.Gotlin.im.databinding.MActivityFriendInfoBinding
import com.ByteDance.Gotlin.im.util.Constants.FRIEND_TYPE

/**
 * @Description：好友信息页面，由查找好友后点击进去和在好友列表中点击进入
 * @Author：Suzy.Mo
 * @Date：2022/6/11 21:03
 */

class FriendInfoActivity : AppCompatActivity() {

    private lateinit var mBinding: MActivityFriendInfoBinding
    private var mFriendType = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = MActivityFriendInfoBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        initView()

    }

    /**
     * 初始化界面 判断好友类别
     */
    private fun initView() {
        //获取类型并设置
        mFriendType = intent.getIntExtra(FRIEND_TYPE,1)
        mBinding.toolbarInfo.title.text = this.resources.getString(R.string.title_info_friend)
//        if (mFriendType == FRIEND_IS) {
//
//        } else if (mFriendType == FRIEND_NO) {
//
//        }
    }
}