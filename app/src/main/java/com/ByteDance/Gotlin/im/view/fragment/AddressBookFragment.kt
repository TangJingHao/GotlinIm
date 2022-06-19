package com.ByteDance.Gotlin.im.view.fragment

import android.annotation.SuppressLint
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
import com.ByteDance.Gotlin.im.view.activity.ApplicationInfoActivity
import com.ByteDance.Gotlin.im.view.activity.FriendInfoActivity
import com.ByteDance.Gotlin.im.view.activity.MyGroupActivity
import com.ByteDance.Gotlin.im.view.activity.SearchActivity
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

        // 搜索类型（好友群聊）
        private const val SEARCH_TYPE = "search_type"
        private const val SEARCH_TYPE_FRIEND = 0
        private const val SEARCH_TYPE_GROUP_CHAT = 1
        private const val SEARCH_TYPE_MESSAGE = 2

        // 功能区对应
        private const val SYSTEM_NEW_FRIEND = 0
        private const val SYSTEM_NEW_GROUP_CHAT = 1
        private const val SYSTEM_MY_GROUP_CHAT = 2
        private const val SYSTEM_APPLICATION_INFO = 3

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
                // 假数据充当功能区按键
                var systemList = ArrayList<UserVO>()
                val u0 = UserVO(1024, "查找新好友", "查找新好友", null, false)
                val u1 = UserVO(1024, "查找新群聊", "查找新群聊", null, false)
                val u2 = UserVO(1024, "我的群聊", "我的群聊", null, false)
                val u3 = UserVO(1024, "申请通知", "申请通知", null, false)
                systemList.add(u0)
                systemList.add(u1)
                systemList.add(u2)
                systemList.add(u3)
                sortFriendList.add(0, systemList)
                titleList.add(0, "功能区")
                // 适配器
                val adapter = TabWithTitleAdapter(
                    requireActivity(),
                    sortFriendList,
                    titleList,
                    TabWithTitleAdapter.TYPE_USER_INFO_SIMPLE
                )
                // 跳转事件
                adapter.setItemOnClickListener { v, groupPosition, relativePosition ->
                    TPhoneUtil.showToast(
                        requireActivity(),
                        "group:" + groupPosition + " " +
                                "   postion:" + relativePosition +
                                "   name: " + sortFriendList.get(groupPosition)
                            .get(relativePosition).nickName
                    )
                    // 功能区跳转
                    if (groupPosition == 0) {
                        when (relativePosition) {
                            SYSTEM_NEW_FRIEND -> {
                                // 查找新好友
                                val intent = Intent(requireActivity(), SearchActivity::class.java)
                                intent.putExtra(SEARCH_TYPE, SEARCH_TYPE_FRIEND)
                                startActivity(intent)
                            }
                            SYSTEM_NEW_GROUP_CHAT -> {
                                // 查找新群聊
                                val intent = Intent(requireActivity(), SearchActivity::class.java)
                                intent.putExtra(SEARCH_TYPE, SEARCH_TYPE_GROUP_CHAT)
                                startActivity(intent)
                            }
                            SYSTEM_APPLICATION_INFO -> {
                                // 好友申请
                                startActivity(
                                    Intent(
                                        requireActivity(),
                                        ApplicationInfoActivity::class.java
                                    )
                                )
                            }
                            SYSTEM_MY_GROUP_CHAT -> {
                                // 我的群聊
                                startActivity(
                                    Intent(
                                        requireActivity(),
                                        MyGroupActivity::class.java
                                    )
                                )
                            }
                        }
                    }else{
                        // 跳转到好友信息页面
                        val intent = Intent(this.context, FriendInfoActivity::class.java)
                        intent.putExtra(Constants.FRIEND_TYPE, Constants.FRIEND_IS)
                        intent.putExtra(
                            Constants.FRIEND_ACCOUNT,
                            sortFriendList[groupPosition][relativePosition].userId
                        )
                        intent.putExtra(
                            Constants.FRIEND_NAME,
                            sortFriendList[groupPosition][relativePosition].userName
                        )
                        intent.putExtra(
                            Constants.FRIEND_NICKNAME,
                            sortFriendList[groupPosition][relativePosition].nickName
                        )
                        intent.putExtra(
                            Constants.FRIEND_GROUPING,
                            "大学同学"
                        )
                        startActivity(intent)
                    }

                }
                b.memberRv.layoutManager = LinearLayoutManager(requireActivity())
                b.memberRv.adapter = adapter
                if (sortFriendList.size != 0 && titleList.size != 0)
                    adapter.notifyDataSetChanged()
            }
        }
    }

    private fun initData() {
        vm.getFriendList()
    }

    private fun initView() {
        b.toolbarRl.title.text = "通讯录"
        b.toolbarRl.imgChevronLeft.visibility = View.GONE
    }


}