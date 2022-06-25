package com.ByteDance.Gotlin.im.viewmodel

import android.icu.text.SimpleDateFormat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.ByteDance.Gotlin.im.Repository
import com.ByteDance.Gotlin.im.application.BaseApp
import com.ByteDance.Gotlin.im.entity.MessageEntity
import com.ByteDance.Gotlin.im.info.vo.MessageVO
import com.ByteDance.Gotlin.im.model.MsgSearchLiveData
import com.ByteDance.Gotlin.im.util.DUtils.DLogUtils
import com.ByteDance.Gotlin.im.util.Tutils.TPhoneUtil
import kotlinx.coroutines.*
import java.sql.Date

/**
 * @Author Zhicong Deng
 * @Date 2022/6/12 10:35
 * @Email 1520483847@qq.com
 * @Description 搜索界面相关的ViewModel
 */
class SearchViewModel : ViewModel() {

    private val TAG: String = "SearchViewModel"

    // 消息记录搜索===================================================================================
    private var sid: Int = 0 // 初始化默认值，无意义
    private var from: Date = Date(0)
    private var to: Date = Date(Long.MAX_VALUE)
    private var content: String = ""
    private var page: Int = 0

    // 外部通过修改该搜索参数进行不同查询
    private val mMsgSearchLiveDate = MutableLiveData<MsgSearchLiveData>()

    // 监听搜索参数
    private val searchResultData = Transformations.switchMap(mMsgSearchLiveDate) {
        // 判断与上一次搜索内容是否一致
        this.page = it.page
        DLogUtils.i(TAG, "搜索内容：" + it.content + "  页数：" + it.page)
        Repository.getSessionHistoryList(getUserId(), it.sid, it.page)
    }

    // 监听网络请求结果，在网络请求变化后更新数据库，同时将自身提供给外部监听，，返回数据库的LiveData
    val searchResultObserverData = Transformations.switchMap(searchResultData) {
        val response = it.getOrNull()
        if (response == null) {
            // 网络请求失败，直接返回
            TPhoneUtil.showToast(BaseApp.getContext(), "搜索失败，返回值为NULl")
            null
        } else {
            // 使用协程
            val historyList = response.data.historyList
            // 协程返回数据的方法
            runBlocking {
                val res = async {
                    // 先插入数据
                    insertMessageList(historyList)
                    // 从数据库中获取，作为返回值，注意sql注入
                    Repository.queryMessage(sid, from, to, content, page * 10 + 10)
                }.await()
                // 阻塞等待返回结果
                return@runBlocking res
            }
        }
    }

    // 外部搜索用,发起搜索
    fun searchSessionMsg(msgSearchLiveData: MsgSearchLiveData) {
        // 更新后搜索
        this.sid = msgSearchLiveData.sid
        this.from = msgSearchLiveData.from
        this.to = msgSearchLiveData.to
        this.content = msgSearchLiveData.content
        mMsgSearchLiveDate.postValue(msgSearchLiveData)
    }

    // 数据库插入数据
    private fun insertMessageList(messageList: List<MessageVO>) {
        GlobalScope.launch(Dispatchers.IO) {
            for (msg in messageList) {
                Repository.insertMessage(VO2Entity(msg))
            }
        }
    }

    // VO类型转换为Entity类型存储
    private fun VO2Entity(msg: MessageVO): MessageEntity {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val date = format.parse(msg.sendTime)
        val sendTime = Date(date.time);
        return MessageEntity(
            msg.session.sessionId,
            msg.sender.userId,
            msg.sender.userName,
            msg.sender.avatar,
            msg.type,
            msg.content, sendTime, msg.self
        )
    }

    // 新好友搜索【邮箱】===============================================================================

    private val mNewFriendSearchByEmailData = MutableLiveData<String>()
    fun searchNewFriendByEmail(input: String) {
        mNewFriendSearchByEmailData.postValue(input)
    }

    val newFriendSearchByEmailObserver = Transformations.switchMap(mNewFriendSearchByEmailData) {
        // TODO 返回网络获取的邮箱搜素结果
        MutableLiveData<String>()
    }

    // 新好友搜索【昵称】===============================================================================

    private val mNewFriendSearchByNameData = MutableLiveData<String>()
    fun searchNewFriendByName(input: String) {
        mNewFriendSearchByNameData.postValue(input)
    }

    val newFriendSearchByNameObserver = Transformations.switchMap(mNewFriendSearchByNameData) {
        // TODO 返回网络获取的昵称搜素结果
        MutableLiveData<String>()
    }

    // 新群聊搜索【群id】==============================================================================

    private val mNewGroupChatSearchByIdData = MutableLiveData<String>()
    fun searchNewGroupChatById(input: String) {
        mNewGroupChatSearchByIdData.postValue(input)
    }

    val newGroupChatSearchByIdObserver = Transformations.switchMap(mNewGroupChatSearchByIdData) {
        // TODO 返回网络获取的群聊id搜素结果
        MutableLiveData<String>()
    }

    // 新群聊搜索【群昵称】==============================================================================

    private val mNewGroupChatSearchByNameData = MutableLiveData<String>()
    fun searchNewGroupChatByName(input: String) {
        mNewGroupChatSearchByNameData.postValue(input)
    }

    val newGroupChatSearchByNameObserver =
        Transformations.switchMap(mNewGroupChatSearchByNameData) {
            // TODO 返回网络获取的群昵称搜素结果
            MutableLiveData<String>()
        }

    // 新好友搜索【我的申请】============================================================================

    private val mMyApplicationData = MutableLiveData<Int>()

    fun getAllRequestData() {
        mMyApplicationData.postValue(0)// 数字无意义
    }

    /**
     * 包括了四种类型的申请
     */
    val mAllRequestObserver = Transformations.switchMap(mMyApplicationData) {
        // TODO 返回网络获取的我的申请
        Repository.getRequestList()
    }

    // 新群聊搜索【群聊申请】============================================================================
    /
//    private val mMyGroupChatApplicationData = MutableLiveData<Int>()
//    fun getMyGroupChatApplicationData() {
//        mMyGroupChatApplicationData.postValue(0)// 数字无意义
//    }
//
//    val mGroupChatApplicationObserver = Transformations.switchMap(mMyGroupChatApplicationData) {
//        // TODO 返回网络获取的我的群聊申请
//        MutableLiveData<String>()
//    }

    // 确认通过申请
    fun patchRequestHandle(reqId: Int, access: Int) = Repository.patchRequestHandle(reqId, access)


    // 其他==========================================================================================
    fun getUserId() = Repository.getUserId()
}

