package com.ByteDance.Gotlin.im.viewmodel

import android.icu.text.SimpleDateFormat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.ByteDance.Gotlin.im.Repository
import com.ByteDance.Gotlin.im.entity.MessageEntity
import com.ByteDance.Gotlin.im.entity.UserEntity
import com.ByteDance.Gotlin.im.info.vo.MessageVO
import com.ByteDance.Gotlin.im.info.vo.UserVO
import kotlinx.coroutines.*
import okhttp3.WebSocketListener
import java.sql.Date

/**
 * @Author Zhicong Deng
 * @Date 2022/6/16 23:34
 * @Email 1520483847@qq.com
 * @Description
 */
class MainViewModel : ViewModel() {
    // 通过暴露的方法来改变mUserIdLiveData，然后更新被监听对象，最后得到反馈
    private val mMsgData = MutableLiveData<Int>()
    private val mSessionData = MutableLiveData<Int>()


    val friendListObserverData = Transformations.switchMap(mMsgData) {
        Repository.getFriendList(it)
    }

    // 交给外部监听,这是数据库获取的好友列表
//    val friendListObserverDB = Transformations.switchMap(friendListObserverData){
//        val response = it.getOrNull()
//        if (response == null) {
//            // 网络请求失败，直接返回
//            null
//        } else {
//            // 使用协程
//            val friendList = response.data.friendList
//            // 协程返回数据的方法
//            runBlocking {
//                val res = async {
//                    // 先插入数据
//                    insertFriendList(friendList)
//                    Repository.queryAllUsers()
//                }.await()
//                // 阻塞等待返回结果
//                return@runBlocking res
//            }
//        }
//    }

    private fun insertFriendList(friendList: List<UserVO>) {
        GlobalScope.launch(Dispatchers.IO) {
            for (friend in friendList) {
                Repository.insertUser(VO2Entity(friend))
            }
        }
    }

    // 交给外部监听，这是消息列表
    val sessionObserverData = Transformations.switchMap(mSessionData) {
        Repository.getSessionList(it)
    }

    fun getSessionList() {
        mSessionData.postValue(Repository.getUserId())
    }

    fun getFriendList() {
        mMsgData.postValue(Repository.getUserId())
    }


    fun getUserId() = Repository.getUserId()

    fun getWebSocketAndConnect(listener: WebSocketListener) =
        Repository.getWebSocketAndConnect(listener)

    // 外部通过修改小红点未读信息条数决定显示的条数
    private val mUnreadMsgLiveDate = MutableLiveData<Int>()

    val msgRedPointNumObserverData = Transformations.switchMap(mUnreadMsgLiveDate) {
        val redPointNum = MutableLiveData<Int>()
        redPointNum.postValue(it)
        redPointNum
    }

    fun setRedPointNum(num: Int) {
        mUnreadMsgLiveDate.postValue(num)
    }

    // VO类型转换为Entity类型存储
    private fun VO2Entity(user: UserVO): UserEntity {
        return UserEntity(
            user.userId,
            user.userName,
            user.sex,
            user.nickName,
            user.email,
            user.avatar,
            user.online
        )
    }

}