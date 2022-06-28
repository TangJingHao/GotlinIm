package com.ByteDance.Gotlin.im.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ByteDance.Gotlin.im.R
import com.ByteDance.Gotlin.im.Repository
import com.ByteDance.Gotlin.im.databinding.MActivityFriendSettingBinding
import com.ByteDance.Gotlin.im.info.response.DefaultResponse
import com.ByteDance.Gotlin.im.info.vo.UserVO
import com.ByteDance.Gotlin.im.network.base.ServiceCreator
import com.ByteDance.Gotlin.im.network.netInterfaces.ChangeUserDataService
import com.ByteDance.Gotlin.im.util.Constants
import com.ByteDance.Gotlin.im.util.Constants.TAG_FRIEND_INFO
import com.ByteDance.Gotlin.im.util.Mutils.MLogUtil.v
import com.ByteDance.Gotlin.im.util.Mutils.MToastUtil.showToast
import com.ByteDance.Gotlin.im.util.Tutils.TPhoneUtil
import com.ByteDance.Gotlin.im.viewmodel.FriendInfoViewModel
import com.qmuiteam.qmui.kotlin.onClick
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Serializable
import kotlin.concurrent.thread

/**
 * @Description：
 * @Author：Suzy.Mo
 * @Date：2022/6/12 19:46
 */
class FriendSettingActivity : AppCompatActivity() {

    private lateinit var mBinding: MActivityFriendSettingBinding
    private val mViewModel by lazy { ViewModelProvider(this).get(FriendInfoViewModel::class.java) }
    private lateinit var userVO:UserVO
    private val nickname by lazy { userVO.nickName}
    private val mFriendId by lazy {userVO.userId}
    private var newGrouping = ""
    private lateinit var etNickName:EditText
    private lateinit var etNewGrouping:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = MActivityFriendSettingBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        userVO = intent.getSerializableExtra(Constants.FRIEND_USER_VO) as UserVO

        initView()
        setListener()
        //setGroupingData()
    }

    private fun initView() {
        etNickName = mBinding.etRemarks
        etNewGrouping = mBinding.etNewGrouping
        mBinding.toolbarSetFriendInfo.title.text = this.resources.getString(R.string.title_info_friend_setting)
        mBinding.toolbarSetFriendInfo.imgMore.visibility = View.VISIBLE
        etNickName.hint = nickname
        if(newGrouping!=null) etNewGrouping.hint = newGrouping
        else etNewGrouping.hint = this.resources.getString(R.string.edit_hint_new_grouping)

        etNickName.hint = intent.getStringExtra(Constants.FRIEND_NICKNAME).toString()
        //etNewGrouping.hint = intent.getStringExtra(Constants.FRIEND_GROUPING).toString()
    }

    private fun setListener() {
        //返回
        mBinding.toolbarSetFriendInfo.imgChevronLeft.onClick {
            this.finish()
        }
        //保存按钮 按下则确定保留
        mBinding.toolbarSetFriendInfo.imgMore.onClick {
            v(TAG_FRIEND_INFO,"=====点击了保存=====")
            if(nickname != etNickName.text.toString()){
                val input:String = etNickName.text.toString()
                val changeUserInfo = ServiceCreator.create<ChangeUserDataService>()
                    .changeFriendNickName(
                        Repository.getToken(),
                        mFriendId,
                        input
                    )
                changeUserInfo.enqueue(object : Callback<DefaultResponse> {
                    override fun onResponse(
                        call: Call<DefaultResponse>,
                        response: Response<DefaultResponse>
                    ) {
                        val body = response.body()
                        if (body != null) {
                            if (body?.status == Constants.SUCCESS_STATUS) {
                                //TODO 存到数据库
                                v(TAG_FRIEND_INFO,"=====${input}修改成功=====")
                                thread {
                                    userVO.nickName = input
                                    Repository.upDataUser(userVO)
                                    runOnUiThread{
                                        "修改成功".showToast(this@FriendSettingActivity)
                                        etNickName.hint = input
                                    }
                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                        "修改失败".showToast(this@FriendSettingActivity)
                    }
                })
            }else{
                v(TAG_FRIEND_INFO,"=====没有修改不用保存=====")
                "没有修改不用保存!".showToast(this)
            }
        }
    }

    private fun setGroupingData() {
        if(nickname!=null) etNickName.hint = newGrouping
        else etNickName.hint = this.resources.getString(R.string.edit_hint_new_grouping)

    }
}