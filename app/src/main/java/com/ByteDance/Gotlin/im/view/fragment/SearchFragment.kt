package com.ByteDance.Gotlin.im.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.ByteDance.Gotlin.im.adapter.TabWithTitleAdapter
import com.ByteDance.Gotlin.im.databinding.DFragmentSearchBinding
import com.ByteDance.Gotlin.im.info.vo.SessionRequestVO
import com.ByteDance.Gotlin.im.info.vo.UserVO
import com.ByteDance.Gotlin.im.model.MsgSearchLiveData
import com.ByteDance.Gotlin.im.util.DUtils.DLogUtils
import com.ByteDance.Gotlin.im.util.DUtils.diy.ConfirmPopupWindow
import com.ByteDance.Gotlin.im.util.DUtils.diy.PopupWindowListener
import com.ByteDance.Gotlin.im.util.Tutils.TPhoneUtil
import com.ByteDance.Gotlin.im.viewmodel.SearchViewModel
import java.sql.Date

class SearchFragment : Fragment() {

    companion object {
        private const val TAG = "SearchFragment"

        // 传入的参数
        private const val SEARCH_PARAM = "search_param"
        private const val SESSION_ID_PARAM = "session_id_param"

        // 启动fragment的参数
        private const val SEARCH_MAILBOX = 0
        private const val SEARCH_NICKNAME = 1
        private const val REQUEST_FRIEND = 2
        private const val SEARCH_GROUP_CHAT_ID = 3
        private const val SEARCH_GROUP_CHAT_NICKNAME = 4
        private const val REQUEST_GROUP_CHAR = 5
        private const val SEARCH_HISTORY_MESSAGE = 6

        /**
         * 工厂模式创建Fragment实例类
         *
         * @param searchParam 当前页面指示器选项卡
         * @param arg         额外参数，在消息搜索中表示sessionId
         * @return 实例化Fragment.
         */
        @JvmStatic
        fun newInstance(searchParam: Int, arg: Int): SearchFragment {
            val fragment = SearchFragment()
            val args = Bundle()
            args.putInt(SEARCH_PARAM, searchParam)
            args.putInt(SESSION_ID_PARAM, arg)
            fragment.arguments = args
            return fragment
        }
    }

    private val b: DFragmentSearchBinding by lazy {
        DFragmentSearchBinding.inflate(LayoutInflater.from(requireActivity()))
    }

    private val vm: SearchViewModel by lazy {
        ViewModelProvider(requireActivity()).get(SearchViewModel::class.java)
    }

    // 不同启动类型的参数
    private var searchParam = 0

    // 消息搜索用到的的会话id
    private var sessionIdParam = 0

    // 搜索数据,消息记录搜索条件请修改这个
    private var mMsgSearchData: MsgSearchLiveData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 初始化参数
        if (arguments != null) {
            searchParam = arguments!!.getInt(SEARCH_PARAM)
            if (searchParam == SEARCH_HISTORY_MESSAGE) {
                sessionIdParam = arguments!!.getInt(SESSION_ID_PARAM)
                // 初始化默认搜索参数
                mMsgSearchData = MsgSearchLiveData(
                    sessionIdParam, Date(0),
                    Date(System.currentTimeMillis()), "", 0
                )
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initViewAndEvent()
        return b.root
    }


    /**
     * 初始化不同搜索类型下的界面和交互时事件
     */
    private fun initViewAndEvent() {
        // 初始化搜索框
        b.searchBar.etInput.apply {
            // 回车监听
            setOnKeyListener(object : View.OnKeyListener {
                override fun onKey(view: View, keycode: Int, event: KeyEvent): Boolean {
                    if (keycode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                        val inputText = b.searchBar.etInput.text.toString()
                        b.searchBar.etInput.clearFocus()
                        hideKeyboard()
                        when (searchParam) {
                            SEARCH_MAILBOX, SEARCH_NICKNAME -> {
                                DLogUtils.i(TAG + "发起查找新好友搜索", inputText)
                                vm.searchNewFriend(inputText)
                            }
                            SEARCH_GROUP_CHAT_ID, SEARCH_GROUP_CHAT_NICKNAME -> {
                                DLogUtils.i(TAG + "发起查找新群聊搜索", inputText)
                                vm.searchNewGroupChat(inputText)
                            }
                        }
                        return true
                    }
                    return false
                }
            })
            hint = "请输入搜索关键词"
        }
        // 初始化滑动列表
        b.rvLayout.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            itemAnimator = DefaultItemAnimator()
        }

        // 不同类型下监听返回数据
        when (searchParam) {
            /** 查找新好友 */
            SEARCH_MAILBOX, SEARCH_NICKNAME -> {
                vm.newFriendSearchObserver.observe(requireActivity()) {
                    val response = it.getOrNull()
                    if (response != null) {
                        val resultUserList = response.data.result
                        val dataList = ArrayList<List<UserVO>>()
                        dataList.add(resultUserList)
                        val title = ArrayList<String>()
                        title.add("搜索到${response.data.total}条匹配结果")
                        val userSearchAdapter = TabWithTitleAdapter(
                            requireActivity(),
                            dataList, title, TabWithTitleAdapter.TYPE_USER_INFO_SIMPLE
                        )
                        userSearchAdapter.setItemOnClickListener(object :
                            TabWithTitleAdapter.OnItemClickListener {
                            override fun onItemClick(
                                v: View?,
                                groupPosition: Int,
                                relativePosition: Int
                            ) {
                                // 搜索到的好友数据
                                val user = resultUserList[relativePosition]
                                TPhoneUtil.showToast(requireActivity(), "点击了用户${user.userName}")
                                // TODO 舒欣，跳转到好友详情页（接上好友申请弹窗）

                            }

                        })
                        b.rvLayout.apply {
                            adapter = userSearchAdapter
                        }
                        userSearchAdapter.notifyDataSetChanged()
                    }
                }
            }
            /** 查找新群聊 */
            SEARCH_GROUP_CHAT_ID, SEARCH_GROUP_CHAT_NICKNAME -> {
                vm.newGroupChatSearchObserver.observe(requireActivity()) {
                    // TODO 新建adapter,传入资料,刷新滑动列表，点击事件
                    DLogUtils.i(TAG, "群聊搜索=====")
                }
            }
            /** 好友申请 */
            REQUEST_FRIEND -> {
                b.searchBar.fLayout.visibility = View.GONE
                vm.mAllRequestObserver.observe(requireActivity()) {
                    // TODO 我的申请页面
                    val response = it.getOrNull()
                    if (response != null) {
                        /** 我要加好友 */
                        val requestFriend = response.data.requestFriend

                        /** 别人想和我交朋友 */
                        val friendRequest = response.data.friendRequest

                        val dataList = ArrayList<List<SessionRequestVO>>()
                        dataList.add(0, requestFriend)
                        dataList.add(1, friendRequest)

                        val titleList = ArrayList<String>()
                        titleList.add(0, "我的好友申请")
                        titleList.add(1, "新朋友邀请")

                        val adapter = TabWithTitleAdapter<SessionRequestVO>(
                            requireActivity(),
                            dataList,
                            titleList,
                            TabWithTitleAdapter.TYPE_USER_INFO_STATUE
                        )
                        adapter.setMoreOnClickListener { v, groupPosition, relativePosition ->
                            // TODO 弹窗确定是否通过申请
                            ConfirmPopupWindow(
                                requireActivity(),
                                "确认添加${friendRequest[relativePosition].user.userName}\n" +
                                        "${friendRequest[relativePosition].user.email}为好友？",
                                object : PopupWindowListener {
                                    val reqId = friendRequest[relativePosition].reqId
                                    override fun onConfirm(input: String?) {
                                        // TODO "确认逻辑，发送确认消息"
                                        vm.patchRequestHandle(reqId, true)
                                        // 发送确认后刷新页面
                                        vm.getAllRequestData()
                                    }

                                    override fun onCancel() {
                                        vm.patchRequestHandle(reqId, false)
                                        // 发送确认后刷新页面
                                        vm.getAllRequestData()
                                    }

                                    override fun onDismiss() {

                                    }
                                }).apply {
                                setConfirmText("确认添加")
                                setCancelText("取消添加")
                                show()
                            }
                            TPhoneUtil.showToast(requireActivity(), "好友申请操作确认弹窗")
                        }

                        b.rvLayout.adapter = adapter
                        b.rvLayout.layoutManager = LinearLayoutManager(requireActivity())

                        adapter.notifyDataSetChanged()

                    }

                    DLogUtils.i(TAG + "好友申请", "MY_APPLICATION")
                }
                vm.getAllRequestData()
            }
            /** 群聊申请 */
            REQUEST_GROUP_CHAR -> {
                b.searchBar.fLayout.visibility = View.GONE
                vm.mAllRequestObserver.observe(requireActivity()) {
                    // TODO 新建adapter,传入资料,刷新滑动列表，点击事件
                    val response = it.getOrNull()
                    if (response != null) {
                        /** 我要加群 */
                        val requestGroup = response.data.requestGroup

                        /** 别人拉我入群 */
                        val groupRequest = response.data.groupRequest

                        val dataList = ArrayList<List<SessionRequestVO>>()
                        dataList.add(0, requestGroup)
                        dataList.add(1, groupRequest)

                        val titleList = ArrayList<String>()
                        titleList.add(0, "我的加群申请")
                        titleList.add(1, "新群聊邀请")

                        val adapter = TabWithTitleAdapter(
                            requireActivity(),
                            dataList,
                            titleList,
                            TabWithTitleAdapter.TYPE_USER_INFO_STATUE
                        )

                        adapter.setMoreOnClickListener { v, groupPosition, relativePosition ->
//                            ConfirmPopupWindow(
//                                requireActivity(),
//                                "确认加入该群聊？",
//                                object : PopupWindowListener {
//                                    val reqId = groupRequest[relativePosition].reqId
//                                    override fun onConfirm(input: String?) {
//                                        // TODO "确认逻辑，发送确认消息"
//                                        vm.patchRequestHandle(reqId, true)
//                                        // 发送确认后刷新页面
//                                        vm.getAllRequestData()
//                                        // 发送确认后刷新页面
//                                        vm.getAllRequestData()
//                                    }
//
//                                    override fun onCancel() {
//                                        vm.patchRequestHandle(reqId, false)
//                                        // 发送确认后刷新页面
//                                        vm.getAllRequestData()
//                                    }
//
//                                    override fun onDismiss() {
//
//                                    }
//                                }).apply {
//                                setConfirmText("确认加入")
//                                setCancelText("取消加入")
//                                show()
//                            }
                            TPhoneUtil.showToast(requireActivity(), "群聊申请操作确认弹窗")
                        }

                        b.rvLayout.adapter = adapter
                        b.rvLayout.layoutManager = LinearLayoutManager(requireActivity())

                        adapter.notifyDataSetChanged()

                    }
                    DLogUtils.i(TAG + "群聊申请", "MY_GROUP_CHAR_APPLICATION")
                }
                vm.getAllRequestData()
            }
        }
    }

    /**
     * 关闭软键盘
     */
    private fun hideKeyboard() {
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val v: View = requireActivity().getWindow().peekDecorView()
        if (null != v) {
            imm.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }
}