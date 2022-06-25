package com.ByteDance.Gotlin.im.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import cn.bingoogolapple.badgeview.BGABadgeView
import cn.bingoogolapple.badgeview.BGABadgeable
import com.ByteDance.Gotlin.im.R
import com.ByteDance.Gotlin.im.adapter.RedPointListener
import com.ByteDance.Gotlin.im.adapter.UserMsgBGAAdapter
import com.ByteDance.Gotlin.im.application.BaseApp
import com.ByteDance.Gotlin.im.databinding.TFragmentMessageBinding
import com.ByteDance.Gotlin.im.info.vo.SessionVO
import com.ByteDance.Gotlin.im.util.DUtils.AttrColorUtils
import com.ByteDance.Gotlin.im.util.DUtils.DLogUtils
import com.ByteDance.Gotlin.im.util.Tutils.TPhoneUtil
import com.ByteDance.Gotlin.im.view.activity.ChatActivity.startChat
import com.ByteDance.Gotlin.im.viewmodel.MainViewModel
import com.google.gson.Gson

/**
 * @Author 唐靖豪
 * @Date 2022/6/12 14:33
 * @Email 762795632@qq.com
 * @Description
 * 消息
 */

class MessageFragment : Fragment() {

    companion object {
        private const val BASE_WS_URL = "ws://chatspace.iceclean.top/space/ws/chat/"
        private const val SEND_MESSAGE = "SEND_MESSAGE"
        private const val TAG = "MessageFragment"
    }

    var gson = Gson()

    private val vm: MainViewModel by lazy {
        ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    }

    private val b: TFragmentMessageBinding by lazy {
        TFragmentMessageBinding.inflate(LayoutInflater.from(activity))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initListener()
        loadData()
        initView()
        vm.getWebSocket()
        return b.root
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initListener() {
        vm.sessionObserverData.observe(requireActivity()) { result ->
            DLogUtils.i(TAG, "刷新列表")
            val responseData = result.getOrNull()
            if (responseData == null) {
                DLogUtils.i(TAG, "我的消息列表返回值为NULL")
                TPhoneUtil.showToast(BaseApp.getContext(), "我的消息列表返回值为NULL")
            } else {
                val messageList = responseData.data.messageList
                val mAdapter = UserMsgBGAAdapter(b.rvLayout)
                // 徽章监听
                val redPointListener: RedPointListener = object : RedPointListener {
                    override fun onDragDismiss(badgeable: BGABadgeable, position: Int) {
                        TPhoneUtil.showToast(BaseApp.getContext(), "item " + position + "的徽章消失")
                    }

                    override fun onClick(view: View, position: Int, badge: BGABadgeView) {
                        //跳转到聊天界面
                        val session: SessionVO = messageList[position].session
                        badge.hiddenBadge()
                        startChat(context, session)
                    }
                }
                mAdapter.data = messageList
                mAdapter.setRedPonitInterface(redPointListener)
                b.rvLayout.adapter = mAdapter
                b.rvLayout.layoutManager = LinearLayoutManager(requireActivity())
                if (messageList.size != 0)
                    mAdapter.notifyDataSetChanged()

                // 小红点数据变化
                var count = 0
                for (msg in messageList) {
                    count += msg.session.badgeNum
                }
                vm.setRedPointNum(count)
            }
        }

        vm.getWsOpenObserverData().observe(requireActivity()) {
            TPhoneUtil.showToast(requireActivity(), "主界面WebSocket开启")
        }

        vm.getWsMessageObserverData().observe(requireActivity()) {
            TPhoneUtil.showToast(requireActivity(), "新消息提醒")
            // 消息页面更新（小红点之类的）
            loadData()
        }

        vm.getFailureObserverData().observe(requireActivity()) {
            TPhoneUtil.showToast(requireActivity(), "主界面WebSocket断开")
            vm.getWebSocket()
        }
    }

    override fun onResume() {
        super.onResume()
        DLogUtils.w("消息列表Fragment", "onResume")
        vm.getSessionList()
    }

    private fun initView() {
        b.myToolbar.imgChevronLeft.visibility = View.GONE;
        b.myToolbar.title.text = "消息列表"
        // 下拉刷新
        b.refreshLayout.apply {
            setColorSchemeColors(
                AttrColorUtils
                    .getValueOfColorAttr(activity, R.attr.accent_default)
            )
            setProgressBackgroundColorSchemeColor(
                AttrColorUtils
                    .getValueOfColorAttr(activity, R.attr.bg_weak)
            )
            setOnRefreshListener(OnRefreshListener {
                loadData()
                b.refreshLayout.isRefreshing = false
            })
        }
    }

    private fun loadData() {
        vm.getSessionList()
    }
}