package com.ByteDance.Gotlin.im.view.activity

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.ByteDance.Gotlin.im.adapter.TabWithTitleAdapter
import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ByteDance.Gotlin.im.R
import com.ByteDance.Gotlin.im.databinding.DActivityMyGroupBinding
import com.ByteDance.Gotlin.im.info.vo.GroupVO
import com.ByteDance.Gotlin.im.util.Constants
import com.ByteDance.Gotlin.im.util.DUtils.DLogUtils
import com.ByteDance.Gotlin.im.util.Mutils.startActivity
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
        b.myToolbar.title.text = "我的群聊"
        b.myToolbar.imgChevronLeft.setOnClickListener { onBackPressed() }

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
                    // TODO 跳转到群聊详情页
                    startActivity<GroupInfoActivity>(this.mContext){
                        putExtra(Constants.GROUP_ID,groupVO.groupId)
                        putExtra(Constants.GROUP_NAME,groupVO.groupName)
                        putExtra(Constants.GROUP_NUM,groupVO.number)
                        putExtra(Constants.GROUP_MY_NAME,groupVO.markName)
                        putExtra(Constants.GROUP_OWNER,groupVO.creatorId)
                    }
                    this.overridePendingTransition(R.anim.t_splash_open, R.anim.t_splash_close)

                })
                b.rvLayout.adapter = adapter
                b.rvLayout.layoutManager = LinearLayoutManager(mContext)
                if (groupList.size != 0)
                    adapter.notifyDataSetChanged()
            }
        }
    }


}