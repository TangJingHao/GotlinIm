package com.ByteDance.Gotlin.im.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ByteDance.Gotlin.im.adapter.TabWithTitleAdapter
import com.ByteDance.Gotlin.im.databinding.DFragmentSearchBinding
import com.ByteDance.Gotlin.im.entity.MessageEntity
import com.ByteDance.Gotlin.im.model.MsgSearchLiveData
import com.ByteDance.Gotlin.im.util.DUtils.DLogUtils
import com.ByteDance.Gotlin.im.viewmodel.SearchViewModel
import java.sql.Date
import java.util.*

class SearchFragment : Fragment() {

    companion object {
        private const val TAG = "SearchFragment"

        // 传入的参数
        private const val SEARCH_PARAM = "search_param"
        private const val SESSION_ID_PARAM = "session_id_param"

        // 启动fragment的参数
        private const val SEARCH_MAILBOX = 0
        private const val SEARCH_NICKNAME = 1
        private const val MY_APPLICATION = 2
        private const val SEARCH_GROUP_CHAT_ID = 3
        private const val SEARCH_GROUP_CHAT_NICKNAME = 4
        private const val MY_GROUP_CHAR_APPLICATION = 5
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

    // 交给适配器的标题
    private var littleTitleArray: Array<String?>? = null

    // 搜索数据,消息记录搜索条件请修改这个
    protected var mMsgSearchData: MsgSearchLiveData? = null


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
                    Date(System.currentTimeMillis()), null, 0
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
        // 交给适配器的标题
        when (searchParam) {
            SEARCH_MAILBOX -> {
                DLogUtils.i(TAG + "邮箱搜索", "SEARCH_MAILBOX")
            }
            SEARCH_NICKNAME -> {
                DLogUtils.i(TAG + "昵称搜索", "SEARCH_NICKNAME")
            }
            MY_APPLICATION -> {
                DLogUtils.i(TAG + "我的申请", "MY_APPLICATION")
                b.searchBar.fLayout.visibility = View.GONE
            }
            SEARCH_GROUP_CHAT_ID -> {
                DLogUtils.i(TAG + "群聊id搜索", "SEARCH_GROUP_CHAT_ID")
            }
            SEARCH_GROUP_CHAT_NICKNAME -> {
                DLogUtils.i(TAG + "群聊昵称搜索", "SEARCH_GROUP_CHAT_NICKNAME")
            }
            MY_GROUP_CHAR_APPLICATION -> {
                DLogUtils.i(TAG + "群聊申请", "MY_GROUP_CHAR_APPLICATION")
                b.searchBar.fLayout.visibility = View.GONE
            }
            else -> {   // 消息搜索
                DLogUtils.i(TAG + "消息搜索", "SEARCH_HISTORY_MESSAGE")
                initSearchMsgViewAndEvent()
                refreshSearchMsgData()
            }
        }
    }


    /**
     * 初始化消息搜索页面事件
     */
    private fun initSearchMsgViewAndEvent() {
        // 时间选择器相关
        b.timeBar.apply {
            lLayout.visibility = View.VISIBLE
            tvTimeFrom.setOnClickListener { view: View? ->
                // TODO 时间选择器,将结果存入 mMsgSearchData.from

            }
            tvTimeTo.setOnClickListener { view: View? ->
                // TODO 时间选择器,将结果存入 mMsgSearchData.to

            }
        }
//         监听返回的livedata,包含了网络请求和存数据库的过程,返回从数据库中的查询结果
        vm.searchResultObserverData.observe(requireActivity()) {
            // 数据
            val dataList: ArrayList<List<MessageEntity>> = ArrayList()
            dataList.add(it)
            // 标题
            val titleList: ArrayList<String> = ArrayList()
            titleList.add("搜索结果")
            val mAdapter = TabWithTitleAdapter(
                requireActivity(), dataList, titleList,
                TabWithTitleAdapter.TYPE_USER_MESSAGE
            )
            val mLinearLayoutManager = LinearLayoutManager(requireActivity())
            b.rvLayout.apply {
                adapter = mAdapter
                layoutManager = mLinearLayoutManager
                itemAnimator = DefaultItemAnimator()
//                addOnScrollListener(object : RecyclerView.OnScrollListener() {
//                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                        super.onScrollStateChanged(recyclerView, newState)
//                        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                            // 如果没有隐藏footView，那么最后一个条目的位置就比我们的getItemCount少1，自己可以算一下
//                            if (mAdapter.isFadeTips() === false && lastVisibleItem + 1 === mAdapter.getItemCount()) {
//                                mHandler.postDelayed(Runnable { // 然后调用updateRecyclerview方法更新RecyclerView
//                                    updateRecyclerView(
//                                        mAdapter.getRealLastPosition(),
//                                        mAdapter.getRealLastPosition() + PAGE_COUNT
//                                    )
//                                }, 500)
//                            }
//
//                            // 如果隐藏了提示条，我们又上拉加载时，那么最后一个条目就要比getItemCount要少2
//                            if (mAdapter.isFadeTips() === true && lastVisibleItem + 2 === mAdapter.getItemCount()) {
//                                mHandler.postDelayed(Runnable { // 然后调用updateRecyclerview方法更新RecyclerView
//                                    updateRecyclerView(
//                                        mAdapter.getRealLastPosition(),
//                                        mAdapter.getRealLastPosition() + PAGE_COUNT
//                                    )
//                                }, 500)
//                            }
//                        }
//                    }
//                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                        super.onScrolled(recyclerView, dx, dy)
//                        lastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition();
//                    }
//                })
            }
            if (it.size != 0) mAdapter.notifyDataSetChanged()
        }

        // 下拉刷新事件
        b.searchRefreshLayout.setOnRefreshListener {
            refreshSearchMsgData()
            b.searchRefreshLayout.isRefreshing = false
        }
        //回车键监测,获取文字后模糊搜索
        b.searchBar.etInput.setOnEditorActionListener { view, actionId, event ->
            val inputText = b.searchBar.etInput.text.toString()
            mMsgSearchData?.content = inputText
            refreshSearchMsgData()
            true
        }
    }

    /**
     * 消息搜索更新数据用
     */
    private fun refreshSearchMsgData() {
        vm.searchSessionMsg(mMsgSearchData!!)
    }

}