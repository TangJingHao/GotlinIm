package com.ByteDance.Gotlin.im.view.fragment

import android.annotation.SuppressLint
import android.opengl.Visibility
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ByteDance.Gotlin.im.adapter.TabWithTitleAdapter
import com.ByteDance.Gotlin.im.application.BaseApp
import com.ByteDance.Gotlin.im.databinding.TFragmentAddressBookBinding
import com.ByteDance.Gotlin.im.info.vo.UserVO
import com.ByteDance.Gotlin.im.util.Constants
import com.ByteDance.Gotlin.im.util.DUtils.DLogUtils
import com.ByteDance.Gotlin.im.util.DUtils.DSortUtils
import com.ByteDance.Gotlin.im.util.Tutils.TPhoneUtil
import com.ByteDance.Gotlin.im.view.activity.FriendInfoActivity
import com.ByteDance.Gotlin.im.view.activity.MyGroupActivity
import com.ByteDance.Gotlin.im.viewmodel.MainViewModel

/**
 * @Author 唐靖豪
 * @Date 2022/6/12 14:34
 * @Email 762795632@qq.com
 * @Description
 * 通讯录
 */

class AddressBookFragment : Fragment() {
    companion object {
        private const val TAG = "AddressBookFragment"
    }

    private val vm: MainViewModel by lazy {
        ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    }

    private val b: TFragmentAddressBookBinding by lazy {
        TFragmentAddressBookBinding.inflate(LayoutInflater.from(activity))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initListener()
        initData()
        initView()
        return b.root
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initListener() {
        vm.friendListObserverData.observe(requireActivity()) { result ->
            val responseData = result.getOrNull()
            if (responseData == null) {
                DLogUtils.i(TAG, "我的好友列表返回值为NULL")
                TPhoneUtil.showToast(BaseApp.getContext(), "我的好友列表返回值为NULL")
            } else {
                // 获取好友列表排序后放入适配器
                val friendList = responseData.data.friendList
                val titleList = ArrayList<String>();
                val sortFriendList = DSortUtils.sort(friendList, titleList)
                // 适配器
                val adapter = TabWithTitleAdapter(
                    requireActivity(),
                    sortFriendList,
                    titleList,
                    TabWithTitleAdapter.TYPE_USER_INFO_SIMPLE
                )
                //子项点击事件
                adapter.setItemOnClickListener { v, groupPosition, relativePosition ->
                    // TODO 跳转事件
                    TPhoneUtil.showToast(
                        requireActivity(),
                        "group:" + groupPosition + " " + titleList.get(groupPosition) +
                                "   postion:" + relativePosition +
                                "   name: " + sortFriendList.get(groupPosition)
                            .get(relativePosition).nickName
                    )
                    //跳转到好友信息页面
                    val intent = Intent(this.context,FriendInfoActivity::class.java)
                    intent.putExtra(Constants.FRIEND_TYPE,Constants.FRIEND_IS)
                    intent.putExtra(Constants.FRIEND_ACCOUNT,
                        sortFriendList[groupPosition][relativePosition].userId)
                    intent.putExtra(Constants.FRIEND_NAME,
                        sortFriendList[groupPosition][relativePosition].userName)
                    intent.putExtra(Constants.FRIEND_NICKNAME,
                        sortFriendList[groupPosition][relativePosition].nickName)
                    intent.putExtra(Constants.FRIEND_GROUPING,
                        "大学同学")
                    startActivity(intent)
                }
                b.memberRv.layoutManager = LinearLayoutManager(requireActivity())
                b.memberRv.adapter = adapter
                if (sortFriendList.size != 0 && titleList.size != 0)
                    adapter.notifyDataSetChanged()
            }
        }
        //功能区界面
        b.configSettingTv.setOnClickListener {
            if(b.configSettingTv.text=="关闭功能区"){
                b.topRl.visibility=View.GONE
                b.configSettingTv.text="开启功能区"
            }else if(b.configSettingTv.text=="开启功能区"){
                b.topRl.visibility=View.VISIBLE
                b.configSettingTv.text="关闭功能区"
            }
        }
        //调转到我的群聊界面
        b.myGroupChatRl.setOnClickListener {
            val intent = Intent(this.context,MyGroupActivity::class.java)
            startActivity(intent)
        }

    }

    private fun initData() {
        vm.getFriendList()
    }

    private fun initView() {

    }


}