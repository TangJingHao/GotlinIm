package com.ByteDance.Gotlin.im.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ByteDance.Gotlin.im.adapter.UserMsgAdapter
import com.ByteDance.Gotlin.im.application.BaseApp
import com.ByteDance.Gotlin.im.databinding.TFragmentMessageBinding
import com.ByteDance.Gotlin.im.info.WSsendContent
import com.ByteDance.Gotlin.im.info.WebSocketReceiveChatMsg
import com.ByteDance.Gotlin.im.info.WebSocketSendChatMsg
import com.ByteDance.Gotlin.im.util.DUtils.DLogUtils
import com.ByteDance.Gotlin.im.util.Tutils.TPhoneUtil
import com.ByteDance.Gotlin.im.viewmodel.MainViewModel
import com.google.gson.Gson
import com.luck.picture.lib.thread.PictureThreadUtils.runOnUiThread
import okhttp3.*
import okio.ByteString
import java.util.concurrent.TimeUnit

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

    var webSocket: WebSocket? = null
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
    ): View? {

        initListener()
        initData()
        initView()

        return b.root
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initListener() {
        vm.sessionObserverData.observe(requireActivity()) { result ->
            val responseData = result.getOrNull()
            if (responseData == null) {
                DLogUtils.i(TAG, "我的消息列表返回值为NULL")
                TPhoneUtil.showToast(BaseApp.getContext(), "我的消息列表返回值为NULL")
            } else {
                val messageList = responseData.data.messageList
                val adapter = UserMsgAdapter(requireActivity(), messageList)
                adapter.setItemOnClickListener { v, position ->
                    TPhoneUtil.showToast(requireActivity(), "item = " + position)
                    // TODO 跳转到聊天界面
                }
                b.rvLayout.layoutManager = LinearLayoutManager(activity)
                b.rvLayout.adapter = adapter
                if (messageList.size != 0)
                    adapter.notifyDataSetChanged()
            }
        }
    }

    private fun initView() {
        b.myToolbar.imgChevronLeft.visibility = View.GONE;
        b.myToolbar.title.text = "消息列表"
    }

    private fun initData() {
//        vm.getSessionList() // 第一次刷新，为了初始化页面
        val listener = EchoWebSocketListener()
        webSocket = vm.getWebSocketAndConnect(listener)
//        connect()
    }



    inner class EchoWebSocketListener : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            DLogUtils.i(TAG, "链接开启")
            runOnUiThread(Runnable { vm.getSessionList() }) // 加载数据
        }

        // 回调,展示消息
        override fun onMessage(webSocket: WebSocket, text: String) {
            TPhoneUtil.showToast(requireActivity(),"新消息提醒")
            DLogUtils.i(TAG, "回调$text")
        }

        // 回调
        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            DLogUtils.i(TAG, "回调$bytes")
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            DLogUtils.i(TAG, "链接关闭中")
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            DLogUtils.i(TAG, "链接已关闭")
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            TPhoneUtil.showToast(requireActivity(),"链接异常")
            DLogUtils.i(TAG, "链接失败/发送失败")
        }
    }
}