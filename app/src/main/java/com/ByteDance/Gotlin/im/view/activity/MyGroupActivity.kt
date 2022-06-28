package com.ByteDance.Gotlin.im.view.activity

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ByteDance.Gotlin.im.adapter.TabWithTitleAdapter
import com.ByteDance.Gotlin.im.databinding.DActivityMyGroupBinding
import com.ByteDance.Gotlin.im.info.vo.GroupVO
import com.ByteDance.Gotlin.im.util.DUtils.DLogUtils
import com.ByteDance.Gotlin.im.util.DUtils.diy.InputPopupWindow
import com.ByteDance.Gotlin.im.util.DUtils.diy.PopupWindowListener
import com.ByteDance.Gotlin.im.util.Tutils.TPhoneUtil
import com.ByteDance.Gotlin.im.viewmodel.MyGroupViewModel

/**
 * @Author Zhicong Deng
 * @Email 1520483847@qq.com
 * @Description 我的群聊展示页
 */

class MyGroupActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MyGroupActivity"
    }

    private lateinit var mContext: Context

    /** 判断数据库是已经存储完成 */
    private var hasSave = false

    private val b: DActivityMyGroupBinding by lazy {
        DActivityMyGroupBinding.inflate(LayoutInflater.from(this))
    }

    val vm: MyGroupViewModel by lazy {
        ViewModelProvider(this).get(MyGroupViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
        setContentView(b.root)

        initListener()
        initDate()
        initView()
    }

    private fun initDate() {
        vm.getGroupList()
    }

    private fun initView() {
        b.myToolbar.apply {
            title.text = "我的群聊"
            imgChevronLeft.setOnClickListener { onBackPressed() }
            tvSys.apply {
                visibility = View.VISIBLE
                tvSys.text = "新建群聊"
                setOnClickListener {
                    InputPopupWindow(this@MyGroupActivity, "新建群聊名称", object : PopupWindowListener {
                        override fun onConfirm(input: String) {
                            vm.newGroup(input)
                        }

                        override fun onCancel() {
                        }

                        override fun onDismiss() {
                        }

                    }).apply {
                        show()
                    }
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initListener() {
        vm.userIdObserverData.observe(this) { result ->
            val responseData = result.getOrNull()
            if (responseData == null) {
                DLogUtils.i(TAG, "我的群聊列表返回值为NULL")
                TPhoneUtil.showToast(mContext, "我的群聊列表返回值为NULL")
            } else {
                val groupList = responseData.data.groupList
                // TODO (字母顺序)排序
                val dataList: ArrayList<List<GroupVO>> = ArrayList()
                dataList.add(groupList)
                val titleList: ArrayList<String> = ArrayList()
                titleList.add("我的群聊列表")
                val adapter = TabWithTitleAdapter(
                    mContext,
                    dataList,
                    titleList,
                    TabWithTitleAdapter.TYPE_USER_INFO_SIMPLE
                )
                adapter.setItemOnClickListener(TabWithTitleAdapter.OnItemClickListener
                { v, groupPosition, relativePosition ->
                    val groupVO = dataList.get(groupPosition).get(relativePosition)
                    TPhoneUtil.showToast(
                        mContext,
                        groupVO.groupName + " gid:" + groupVO.groupId
                    )
                    // 通知去查询相应Session并跳转
                    vm.getSessionByGroup(groupVO)
                })
                b.rvLayout.adapter = adapter
                b.rvLayout.layoutManager = LinearLayoutManager(mContext)
                if (groupList != null && groupList.size != 0)
                    adapter.notifyDataSetChanged()
            }
        }
        vm.newGroupObserver.observe(this) {
            val response = it.getOrNull()
            if (response != null) {
                TPhoneUtil.showToast(this, response.msg)
                vm.getGroupList()
            }
        }
        vm.startActivityObserver.observe(this) {
            startActivity2GroupInfo(it.sessionId, it.group)
        }
        vm.groupListDB.observe(this) {
            hasSave = it
        }
    }

    fun startActivity2GroupInfo(sessionId: Int, group: GroupVO) {
        if (hasSave) {
            // TODO 跳转到群聊详情页
            DLogUtils.i(TAG, "sid:${sessionId},gid${group.groupId}")
//            startActivity<GroupInfoActivity>(this.mContext) {
//                putExtra(Constants.GROUP_ID, group.groupId)
//                putExtra(Constants.GROUP_NAME, group.groupName)
//                putExtra(Constants.GROUP_NUM, group.number)
//                putExtra(Constants.GROUP_MY_NAME, group.markName)
//                putExtra(Constants.GROUP_OWNER, group.creatorId)
//            }
//            this.overridePendingTransition(R.anim.t_splash_open, R.anim.t_splash_close)
//            this.finish()
        }
    }
}