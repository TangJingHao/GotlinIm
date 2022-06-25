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
import com.ByteDance.Gotlin.im.info.WebSocketReceiveUserOnline
import com.ByteDance.Gotlin.im.info.vo.SessionVO
import com.ByteDance.Gotlin.im.info.ws.WebSocketType
import com.ByteDance.Gotlin.im.util.Constants
import com.ByteDance.Gotlin.im.util.DUtils.AttrColorUtils
import com.ByteDance.Gotlin.im.util.DUtils.DLogUtils
import com.ByteDance.Gotlin.im.util.DUtils.JsonUtils.toAny
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
            val responseData = result.getOrNull()
            if (responseData == null) {
                TPhoneUtil.showToast(BaseApp.getContext(), "消息列表返回值为NULL")
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
                        val session: SessionVO = messageList.get(position).session
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
                vm.setMsgRedPointNum(count)
            }
        }

        vm.getWsOpenObserverData().observe(requireActivity()) {
            TPhoneUtil.showToast(requireActivity(), "主界面WebSocket开启")
        }

        // 监听Websocket回调的liveData
        vm.getWsMessageObserverData().observe(requireActivity()) {
            // 导入了json转换的工具类,首先转换出其中的类型
            val wsType = it.toAny(WebSocketType::class.java)?.wsType
            // 根据类型判断
            when (wsType) {
                Constants.WS_USER_ONLINE -> {
                    // 具体处理
                    // 再转换为对应的websocket接收类
                    val wsReceiveUserOnline = it.toAny(WebSocketReceiveUserOnline::class.java)

//                    val userVO =
//                        wsReceiveUserOnline?.wsContent?.userId?.let { it1 ->
//                            Repository.queryUserById(
//                                it1
//                            )
//                        }
//                    val nickName = userVO?.nickName
//                    TPhoneUtil.showToast(requireActivity(), "好友 $nickName 上线了")
                }
                Constants.WS_SEND_MESSAGE -> {
                    TPhoneUtil.showToast(requireActivity(), "新消息通知")
                }
            }
            // 消息页面更新（小红点之类的）
            loadData()
        }

        vm.getFailureObserverData().observe(requireActivity()) {
            TPhoneUtil.showToast(requireActivity(), "主界面WebSocket断开")
            vm.getWebSocket()
        }

        val s: String = "{\n" +
                "    \"wsContent\": {\n" +
                "        \"online\": true,                // 用户最新状态\n" +
                "        \"sessionIdList\": [3, 5, 6],    // 与用户相关的会话\n" +
                "        \"userId\": 4                    // 目前用户 ID\n" +
                "    },\n" +
                "    \"wsType\": \"USER_ONLINE\"\n" +
                "}"

        val any = s.toAny(WebSocketType::class.java)

        DLogUtils.i(TAG+ "类型测试",any?.wsType + "  ")
    }

    override fun onResume() {
        super.onResume()
        vm.getWebSocket()
        vm.getSessionList()
    }

    private fun initView() {
        b.myToolbar.apply {
            imgChevronLeft.visibility = View.GONE
            title.text = "消息列表"
            fLayout.setBackgroundColor(AttrColorUtils.getValueOfColorAttr(requireActivity(),R.attr.bg_default))
        }
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