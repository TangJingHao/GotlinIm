package com.ByteDance.Gotlin.im.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
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
import com.ByteDance.Gotlin.im.util.DUtils.DLogUtils
import com.ByteDance.Gotlin.im.view.fragment.MessageFragment
import kotlinx.coroutines.*
import okhttp3.WebSocket

/**
 * @Author Zhicong Deng
 * @Date 2022/6/16 23:34
 * @Email 1520483847@qq.com
 * @Description
 */
@RequiresApi(Build.VERSION_CODES.Q)
class MainViewModel : ViewModel() {

    // 通用=========================================================================================
    val TAG = "MainViewModel"


    fun getUserId() = Repository.getUserId()

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
                    if (friendList != null && friendList.size > 0)
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
        DLogUtils.i(TAG,"消息列表网络更新")
        Repository.getSessionList(it)
    }

    val sessionDB = Transformations.switchMap(sessionObserverData) {
        DLogUtils.i(TAG,"消息列表数据库更新")
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
                    if (msgList != null && msgList.size > 0) {
                        insertSessionList(msgList)
                        val b = MutableLiveData<Boolean>(true)
                        b
                    } else {
                        val b = MutableLiveData<Boolean>(false)
                        b
                    }
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
        DLogUtils.i(TAG,"消息列表刷新")
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
        DLogUtils.i(TAG,"消息列表小红点更新")
        msgRedPointObserver.postValue(num)
    }

    // 好友申请小红点
    private val requestRedPointData = MutableLiveData<Int>()

    val requestRedPointObserver = Transformations.switchMap(requestRedPointData) {
        DLogUtils.i(TAG,"网络请求：消息列表小红点更新")
        Repository.getRequestBadge()
    }

    /** 更新通讯录页的小红点 */
    fun getMsgRedPointNum() {
        DLogUtils.i(TAG,"请求更新通讯录小红点")
        requestRedPointData.postValue(0)
    }

    // 通讯录页面 查找新好友小红点
    val newFriendRedPointObserver = MutableLiveData<Int>()

    fun setNewFriendRedPointNum(num: Int) {
        DLogUtils.i(TAG,"查找新好友小红点更新")
        newFriendRedPointObserver.postValue(num)
    }

    // 通讯录页面 查找新群聊小红点
    val newGroupChatRedPointObserver = MutableLiveData<Int>()

    fun setNewGroupChatRedPointNum(num: Int) {
        DLogUtils.i(TAG,"查找新群聊小红点更新")
        newGroupChatRedPointObserver.postValue(num)
    }


    // 跳转页面=======================================================================================
    val startActivityData = MutableLiveData<UserVO>()

    val startActivityObserver = Transformations.switchMap(startActivityData) {
        // 协程返回数据的方法
        runBlocking {
            val res = async {
                val session: SessionVO = withContext(Dispatchers.IO) {
                    Repository.querySessionByUserId(it.userId)
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