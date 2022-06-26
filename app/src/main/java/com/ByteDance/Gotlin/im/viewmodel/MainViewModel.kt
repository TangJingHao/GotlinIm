package com.ByteDance.Gotlin.im.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.ByteDance.Gotlin.im.Repository
import com.ByteDance.Gotlin.im.entity.SessionUserEntity
import com.ByteDance.Gotlin.im.info.FriendListBean
import com.ByteDance.Gotlin.im.info.MessageList
import com.ByteDance.Gotlin.im.info.vo.SessionVO
import com.ByteDance.Gotlin.im.info.vo.UserVO
import com.ByteDance.Gotlin.im.model.SessionUserLiveData
import kotlinx.coroutines.*
import okhttp3.WebSocket

/**
 * @Author Zhicong Deng
 * @Date 2022/6/16 23:34
 * @Email 1520483847@qq.com
 * @Description
 */
class MainViewModel : ViewModel() {

    // 通用=========================================================================================
    val TAG = "MainViewModel"

    // 好友列表======================================================================================

    private val mMsgData = MutableLiveData<Int>()

    val friendListObserverData = Transformations.switchMap(mMsgData) {
        Repository.getFriendList(it)
    }

    // 交给外部监听,这是数据库获取的好友列表
    val friendListObserverDB = Transformations.switchMap(friendListObserverData) {
        val response = it.getOrNull()
        if (response == null) {
            // 网络请求失败，直接返回
            null
        } else {
            // 使用协程
            val friendList = response.data.friendList
            // 协程返回数据的方法
            runBlocking {
                val res = async {
                    // 先插入数据
                    insertFriendList(friendList)
                    Repository.queryAllUsers()
                }.await()
                // 阻塞等待返回结果
                return@runBlocking res
            }
        }
    }

    private fun insertFriendList(friendList: List<FriendListBean>) {
        GlobalScope.launch(Dispatchers.IO) {
            for (friend in friendList) {
                // su 关系表
                Repository.insertSU(SessionUserEntity(friend.sessionId, friend.user.userId))
                // 用户表
                Repository.insertUser(friend.user)
            }
        }
    }

    fun getFriendList() {
        mMsgData.postValue(Repository.getUserId())
    }

    // 消息列表======================================================================================
    private val mSessionData = MutableLiveData<Int>()

    val sessionObserverData = Transformations.switchMap(mSessionData) {
        Repository.getSessionList(it)
    }

    val sessionDB = Transformations.switchMap(sessionObserverData) {
        val response = it.getOrNull()
        if (response == null) {
            // 网络请求失败，直接返回
            null
        } else {
            // 使用协程
            val msgList = response.data.messageList
            // 协程返回数据的方法
            runBlocking {
                val res = async {
                    // 先插入数据
                    insertSessionList(msgList)
                    val b = MutableLiveData<Boolean>(true)
                    b
                }.await()
                // 阻塞等待返回结果
                return@runBlocking res
            }
        }
    }

    private fun insertSessionList(msgList: List<MessageList>) {
        GlobalScope.launch(Dispatchers.IO) {
            for (msg in msgList) {
                Repository.insertSession(msg.session)
            }
        }
    }

    fun getSessionList() {
        mSessionData.postValue(Repository.getUserId())
    }

    // WebSocket===================================================================================

    fun getWebSocket(): WebSocket = Repository.getWebSocket()

    fun getWsOpenObserverData() = Repository.getWsOpenObserverData()
    fun getWsMessageObserverData() = Repository.getWsMessageObserverData()
    fun getFailureObserverData() = Repository.getFailureObserverData()

    // 小红点========================================================================================
    // 消息列表小红点
    val msgRedPointObserver = MutableLiveData<Int>()

    fun setMsgRedPointNum(num: Int) {
        msgRedPointObserver.postValue(num)
    }

    // 好友申请小红点
    private val requestRedPointData = MutableLiveData<Int>()

    val requestRedPointObserver = Transformations.switchMap(requestRedPointData) {
        Repository.getRequestBadge()
    }

    /** 更新通讯录页的小红点 */
    fun getMsgRedPointNum() {
        requestRedPointData.postValue(0)
    }

    // 通讯录页面 查找新好友小红点
    val newFriendRedPointObserver = MutableLiveData<Int>()

    fun setNewFriendRedPointNum(num: Int) {
        newFriendRedPointObserver.postValue(num)
    }

    // 通讯录页面 查找新群聊小红点
    val newGroupChatRedPointObserver = MutableLiveData<Int>()

    fun setNewGroupChatRedPointNum(num: Int) {
        newGroupChatRedPointObserver.postValue(num)
    }


    // 跳转页面=======================================================================================
    val startActivityData = MutableLiveData<UserVO>()

    val startActivityObserver = Transformations.switchMap(startActivityData) {
        // 协程返回数据的方法
        runBlocking {
            val res = async {
                val session: SessionVO = withContext(Dispatchers.IO) {
                    Repository.querySessionByUid(it.userId)
                }
                MutableLiveData(SessionUserLiveData(session, it))
            }.await()
            // 阻塞等待返回结果
            return@runBlocking res
        }
    }

    fun getSessionByUid(user: UserVO) {
        startActivityData.postValue(user)
    }


}