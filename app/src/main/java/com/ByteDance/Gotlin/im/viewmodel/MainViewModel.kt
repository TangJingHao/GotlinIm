package com.ByteDance.Gotlin.im.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.ByteDance.Gotlin.im.Repository
import okhttp3.WebSocketListener

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

    // 交给外部监听
    val friendListObserverData = Transformations.switchMap(mMsgData) {
        Repository.getFriendList(it)
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

}