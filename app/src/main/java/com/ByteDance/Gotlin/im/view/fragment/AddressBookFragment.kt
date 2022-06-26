package com.ByteDance.Gotlin.im.view.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.ByteDance.Gotlin.im.R
import com.ByteDance.Gotlin.im.adapter.TabWithTitleAdapter
import com.ByteDance.Gotlin.im.databinding.TFragmentAddressBookBinding
import com.ByteDance.Gotlin.im.info.vo.TestUser
import com.ByteDance.Gotlin.im.info.vo.UserVO
import com.ByteDance.Gotlin.im.util.Constants
import com.ByteDance.Gotlin.im.util.DUtils.AttrColorUtils
import com.ByteDance.Gotlin.im.util.DUtils.DLogUtils
import com.ByteDance.Gotlin.im.util.DUtils.DSortUtils
import com.ByteDance.Gotlin.im.util.Tutils.TPhoneUtil
import com.ByteDance.Gotlin.im.view.activity.FriendInfoActivity
import com.ByteDance.Gotlin.im.view.activity.MyGroupActivity
import com.ByteDance.Gotlin.im.view.activity.SearchActivity
import com.ByteDance.Gotlin.im.view.activity.TestActivity
import com.ByteDance.Gotlin.im.view.custom.TSideBar
import com.ByteDance.Gotlin.im.viewmodel.MainViewModel
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils

/**
 * @Author 唐靖豪
 * @Date 2022/6/12 14:34
 * @Email 762795632@qq.com
 * @Description
 * 通讯录
 */

class AddressBookFragment : Fragment() {
    private lateinit var mFriendList: List<UserVO>
    private lateinit var mAdapter: TabWithTitleAdapter<UserVO>
    private lateinit var mSortFriendList: List<List<UserVO>>
    private lateinit var manager: LinearLayoutManager
    private var mTitleList = ArrayList<String>()

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
        vm.friendListObserverDB.observe(requireActivity()) {
            // 获取好友列表排序后放入适配器
            mFriendList = it
            val titleList = ArrayList<String>()
            val sortFriendList = DSortUtils.sort(mFriendList, titleList)
            mSortFriendList = sortFriendList
            mTitleList = titleList
            // 适配器
            mAdapter = TabWithTitleAdapter(
                requireActivity(),
                sortFriendList,
                titleList,
                TabWithTitleAdapter.TYPE_USER_INFO_SIMPLE
            )
            // 侧边栏
            b.sideBar.setDataResource(mTitleList)
            // 跳转事件
            mAdapter.setItemOnClickListener { v, groupPosition, relativePosition ->
                TPhoneUtil.showToast(
                    requireActivity(),
                    "group:" + groupPosition + " " +
                            "   position:" + relativePosition +
                            "   name: " + sortFriendList[groupPosition][relativePosition].nickName
                )
                // 跳转到好友信息页面
                val intent = Intent(this.context, FriendInfoActivity::class.java)
                intent.apply {
                    putExtra(
                        Constants.FRIEND_TYPE,
                        Constants.FRIEND_IS
                    )
                    putExtra(
                        Constants.FRIEND_ACCOUNT,
                        sortFriendList[groupPosition][relativePosition].userId
                    )
                    putExtra(
                        Constants.FRIEND_NAME,
                        sortFriendList[groupPosition][relativePosition].userName
                    )
                    putExtra(
                        Constants.FRIEND_NICKNAME,
                        sortFriendList[groupPosition][relativePosition].nickName
                    )
                    putExtra(
                        Constants.FRIEND_GROUPING,
                        "大学同学"
                    )
                }
                startActivity(intent)

            }
            manager = LinearLayoutManager(requireContext())
            manager.orientation = LinearLayoutManager.VERTICAL
            b.memberRv.layoutManager = manager
            b.memberRv.adapter = mAdapter
            if (sortFriendList.size != 0 && titleList.size != 0)
                mAdapter.notifyDataSetChanged()
        }
        // 我的新好友小红点
        vm.newFriendRedPointObserver.observe(requireActivity()) {
            if (it > 0)
                b.sysNewFriend.imgAvatar.viewTreeObserver.addOnGlobalLayoutListener(object :
                    ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        BadgeDrawable.create(requireActivity()).apply {
                            badgeGravity = Gravity.END
//                            number = it
                            backgroundColor = AttrColorUtils.getValueOfColorAttr(
                                requireActivity(),
                                R.attr.critical_default
                            )
                            isVisible = true
                            BadgeUtils.attachBadgeDrawable(this, b.sysNewFriend.imgAvatar)
                        }
                        b.sysNewFriend.imgAvatar.viewTreeObserver.removeOnGlobalLayoutListener(
                            this
                        )
                    }

                })
        }
        // 我的新群聊小红点
        vm.newGroupChatRedPointObserver.observe(requireActivity()) {
            if (it > 0)
                b.sysNewGroupChat.imgAvatar.viewTreeObserver.addOnGlobalLayoutListener(object :
                    ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        BadgeDrawable.create(requireActivity()).apply {
                            badgeGravity = Gravity.END
//                            number = it
                            backgroundColor = AttrColorUtils.getValueOfColorAttr(
                                requireActivity(),
                                R.attr.critical_default
                            )
                            isVisible = true
                            BadgeUtils.attachBadgeDrawable(this, b.sysNewGroupChat.imgAvatar)
                        }
                        b.sysNewGroupChat.imgAvatar.viewTreeObserver.removeOnGlobalLayoutListener(
                            this
                        )
                    }

                })
        }
        // 展开功能区文字小红点
        vm.requestRedPointObserver.observe(requireActivity()){
            val response = it.getOrNull()
            if (response != null) {
                val totalUnread = response.data.total
                if (totalUnread > 0)
                    b.toolbarRl.tvSys.viewTreeObserver.addOnGlobalLayoutListener(object :
                        ViewTreeObserver.OnGlobalLayoutListener {
                        override fun onGlobalLayout() {
                            BadgeDrawable.create(requireActivity()).apply {
                                badgeGravity = BadgeDrawable.TOP_END
                                backgroundColor = AttrColorUtils.getValueOfColorAttr(
                                    requireActivity(),
                                    R.attr.critical_default
                                )
                                isVisible = true
                                BadgeUtils.attachBadgeDrawable(this, b.toolbarRl.tvSys)
                            }
                            b.toolbarRl.tvSys.viewTreeObserver.removeOnGlobalLayoutListener(
                                this
                            )
                        }

                    })
            }
        }
    }

    private fun initData() {
        vm.getFriendList()
    }

    private fun initView() {
        b.systemLayout.visibility = View.VISIBLE
        b.toolbarRl.tvSys.visibility = View.VISIBLE
        b.toolbarRl.tvSys.text = "关闭功能区"
        b.toolbarRl.apply {
            imgChevronLeft.visibility = View.GONE
            title.text = "通讯录"
            fLayout.setBackgroundColor(
                AttrColorUtils.getValueOfColorAttr(
                    requireActivity(),
                    R.attr.bg_default
                )
            )
            tvSys.setOnClickListener {
                if (b.systemLayout.visibility == View.VISIBLE) {
                    b.toolbarRl.tvSys.text = "展开功能区"
                    b.systemLayout.visibility = View.GONE
                } else {
                    b.toolbarRl.tvSys.text = "关闭功能区"
                    b.systemLayout.visibility = View.VISIBLE
                }
            }
        }
        b.sideBar.apply {
            setScaleSize(1)
            setScaleItemCount(8)
            setOnStrSelectCallBack(object : TSideBar.ISideBarSelectCallBack {
                override fun onSelectStr(index: Int, selectStr: String) {
                    manager.scrollToPositionWithOffset(mAdapter.mTitleIndexList().get(index), 0)
                }
            })
        }
        b.refreshLayout.apply {
            setColorSchemeColors(
                AttrColorUtils
                    .getValueOfColorAttr(activity, R.attr.accent_default)
            )
            setProgressBackgroundColorSchemeColor(
                AttrColorUtils
                    .getValueOfColorAttr(activity, R.attr.bg_weak)
            )
            setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
                initData()
                b.refreshLayout.isRefreshing = false
            })
        }
        // 查找新好友
        b.sysNewFriend.apply {
            tvTitleName.text = "查找新好友"
            rLayout.setOnClickListener {
                SearchActivity.startSearchNewFriendSearch(requireActivity())
            }
        }

        // 查找新群聊
        b.sysNewGroupChat.apply {
            tvTitleName.text = "查找新群聊"
            rLayout.setOnClickListener {
                SearchActivity.startSearchNewGroupSearch(requireActivity())
            }
        }

        // 我的群聊
        b.sysMyGroup.apply {
            tvTitleName.text = "我的群聊"
            rLayout.setOnClickListener {
                startActivity(Intent(requireActivity(), MyGroupActivity::class.java))
            }
        }


        // 测试用，添加新好友
        b.sysTestAddNew.apply {
            tvTitleName.text = "添加新好友测试"
            rLayout.setOnClickListener {
                startActivity(Intent(requireActivity(), TestActivity::class.java))
            }
        }
    }


}