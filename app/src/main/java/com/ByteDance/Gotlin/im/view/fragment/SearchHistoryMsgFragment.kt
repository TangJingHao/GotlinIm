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
import androidx.recyclerview.widget.RecyclerView
import com.ByteDance.Gotlin.im.adapter.LoadMoreAdapter
import com.ByteDance.Gotlin.im.databinding.DFragmentSearchBinding
import com.ByteDance.Gotlin.im.model.MsgSearchLiveData
import com.ByteDance.Gotlin.im.util.DUtils.DLogUtils
import com.ByteDance.Gotlin.im.viewmodel.SearchViewModel
import java.sql.Date

/**
 * @Author Zhicong Deng
 * @Date 2022/6/24 13:10
 * @Email 1520483847@qq.com
 * @Description 把消息搜素页面分离出来
 */
class SearchHistoryMsgFragment : Fragment() {
    companion object {
        private const val TAG = "SearchHistoryMsgFragment"

        // 传入的参数
        private const val SESSION_ID_PARAM = "session_id_param"

        // 搜索状态的参数
        private const val REFRESH_MODE_DEFAULT = 0
        private const val REFRESH_MODE_LOAD_MORE = 1
        private const val REFRESH_MODE_NEW = 2

        /**
         * 创建实例类
         *
         * @param sessionId 绘画id
         * @return 实例化Fragment.
         */
        @JvmStatic
        fun newInstance(sessionId: Int): SearchHistoryMsgFragment {
            val fragment = SearchHistoryMsgFragment()
            val args = Bundle()
            args.putInt(SESSION_ID_PARAM, sessionId)
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


    // 消息搜索用到的的会话id
    private var sessionIdParam = 0

    // 搜索数据,消息记录搜索条件请修改这个
    private var mMsgSearchData: MsgSearchLiveData? = null

    // 上一次搜索数据的条数，用于判断是否更新
    private var lastDataSize = 0
    private var curDataSize = 0

    // 搜索类型，0为不可搜索，1为加载更多数据，2为搜索新内容
    private var refreshMode = 0

    var mAdapter: LoadMoreAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 初始化参数
        if (arguments != null) {
            sessionIdParam = arguments!!.getInt(SESSION_ID_PARAM)
            // 初始化默认搜索参数
            mMsgSearchData = MsgSearchLiveData(
                sessionIdParam, Date(0),
                Date(System.currentTimeMillis()),
                "", 0
            )
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initViewAndEvent()
        loadMsgWithParam("", null, null)
        return b.root
    }


    /**
     * 初始化消息搜索页面事件
     */
    private fun initViewAndEvent() {

        mAdapter =
            LoadMoreAdapter(requireActivity())

        // 时间选择器相关
        b.timeBar.apply {
            lLayout.visibility = View.VISIBLE
            tvTimeFrom.setOnClickListener { view: View? ->
                // TODO 时间选择器（起始）,传入from中
//                loadMsgWithParam(null, from, null)

            }
            tvTimeTo.setOnClickListener { view: View? ->
                // TODO 时间选择器（结束），传入to中
//                loadMsgWithParam(null, null, to)
            }
        }

        b.rvLayout.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            itemAnimator = DefaultItemAnimator()
            adapter = mAdapter
            // recycleView滑动监听
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(
                    recyclerView: RecyclerView,
                    newState: Int
                ) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        // 滑倒底了,刷新数据
                        DLogUtils.i(
                            TAG,
                            "滑倒底了,刷新数据============================================================================="
                        )
                        DLogUtils.i(
                            TAG,
                            "当前资料大小：" + curDataSize + "\t上次资料大小：" + lastDataSize
                        )
                        if (curDataSize > lastDataSize) {
                            lastDataSize = curDataSize
                            loadMoreMsg()
                        } else {
                            lastDataSize = 0
                            curDataSize = 0
                            mAdapter?.hasMore(false)
                            mAdapter?.notifyDataSetChanged()
                        }
                    } else {
                        // 还未滑到底
                    }
                }
            })
        }

        // 监听返回的livedata,包含了网络请求和存数据库的过程,返回从数据库中的查询结果
        vm.searchResultObserverData.observe(requireActivity()) {
            curDataSize = it.size
            DLogUtils.i(TAG, "数据库查找条数" + it.size)
            when (refreshMode) {
                REFRESH_MODE_NEW -> {
                    if (it.size < 10) {
                        DLogUtils.i(TAG, "搜索结果少于10条")
                        mAdapter?.hasMore(false)
                        mAdapter?.setData(it)
                        mAdapter?.notifyDataSetChanged()
                    } else if (it.size > lastDataSize) {
                        DLogUtils.i(TAG, "发起新搜索")
                        mAdapter?.hasMore(true)
                        mAdapter?.setData(it)
                        // 更新高亮文字
                        mAdapter?.notifyDataSetChanged()
                    } else {
                        mAdapter?.hasMore(false)
                        mAdapter?.notifyDataSetChanged()
                    }
                }
                REFRESH_MODE_LOAD_MORE -> {
                    DLogUtils.i(TAG, "加载更多")
                    mAdapter?.setData(it)
                    mAdapter?.notifyDataSetChanged()
                }
            }
            refreshMode = REFRESH_MODE_DEFAULT
        }

        //回车键监测,获取文字后模糊搜索
        b.searchBar.etInput.apply {
            setOnKeyListener(object : View.OnKeyListener {
                override fun onKey(view: View, keycode: Int, event: KeyEvent): Boolean {
                    if (keycode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                        val inputText = b.searchBar.etInput.text.toString()
                        b.searchBar.etInput.clearFocus()
                        hideKeyboard()
                        // 搜索
                        DLogUtils.i(TAG, "输入：" + inputText)
                        loadMsgWithParam(inputText, null, null)
                        return true
                    }
                    return false
                }
            })
            hint = "请输入搜索关键词"
        }


    }

    /**
     * 传入参数搜索消息记录（用于主动搜索）
     */
    private fun loadMsgWithParam(content: String?, from: Date?, to: Date?) {
        if (refreshMode == REFRESH_MODE_DEFAULT) {
            if (content != null) mMsgSearchData?.content = content
            if (from != null) mMsgSearchData?.from = from
            if (to != null) mMsgSearchData?.to = to
            mMsgSearchData?.page = 0
            refreshMode = REFRESH_MODE_NEW
            curDataSize = 0
            mAdapter?.setHighLightPart(mMsgSearchData?.content)
            vm.searchSessionMsg(mMsgSearchData!!)
        }
    }

    /**
     * 加载更多消息记录（一般用于上拉刷新）
     */
    private fun loadMoreMsg() {
        if (refreshMode == REFRESH_MODE_DEFAULT) {
            refreshMode = REFRESH_MODE_LOAD_MORE
            mMsgSearchData?.page = mMsgSearchData?.page?.plus(1)!!
            vm.searchSessionMsg(mMsgSearchData!!)
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