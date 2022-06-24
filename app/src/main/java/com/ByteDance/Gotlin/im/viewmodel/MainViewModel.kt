package com.ByteDance.Gotlin.im.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.ByteDance.Gotlin.im.Repository
import com.ByteDance.Gotlin.im.info.vo.UserVO
import com.ByteDance.Gotlin.im.util.DUtils.DLogUtils
import kotlinx.coroutines.*
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

/**
 * @Author Zhicong Deng
 * @Date 2022/6/16 23:34
 * @Email 1520483847@qq.com
 * @Description
 */
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
                    insertFriendList(friendList)
                    Repository.queryAllUsers()
                }.await()
                // 阻塞等待返回结果
                return@runBlocking res
            }
        }
    }

    private fun insertFriendList(friendList: List<UserVO>) {
        GlobalScope.launch(Dispatchers.IO) {
            for (friend in friendList) {
                Repository.insertUser(friend)
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

    fun getSessionList() {
        mSessionData.postValue(Repository.getUserId())
    }

    // WebSocket===================================================================================

    fun getWebSocket(): WebSocket = Repository.getWebSocket()

    fun getWsOpenObserverData() = Repository.getWsOpenObserverData()
    fun getWsMessageObserverData() = Repository.getWsMessageObserverData()
    fun getFailureObserverData() = Repository.getFailureObserverData()

    // 小红点========================================================================================
    val msgRedPointNumObserverData = MutableLiveData<Int>()

    fun setRedPointNum(num: Int) {
        msgRedPointNumObserverData.postValue(num)
    }


}