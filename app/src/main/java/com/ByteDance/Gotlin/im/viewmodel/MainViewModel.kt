package com.ByteDance.Gotlin.im.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.ByteDance.Gotlin.im.Repository

/**
 * @Author Zhicong Deng
 * @Date 2022/6/16 23:34
 * @Email 1520483847@qq.com
 * @Description
 */
class MainViewModel : ViewModel() {
    // 通过暴露的方法来改变mUserIdLiveData，然后更新被监听对象，最后得到反馈
    private val mUserIdLiveData = MutableLiveData<Int>()

    // 交给外部监听
    val friendListObserverData = Transformations.switchMap(mUserIdLiveData) {
        Repository.getFriendList(it)
    }

    // 交给外部监听，这是消息列表
    val sessionObserverData = Transformations.switchMap(mUserIdLiveData) {
        Repository.getSessionList(it)
    }

    fun getFriendList() {
        mUserIdLiveData.value = Repository.getUserId()
    }


    fun getSessionList() {
        mUserIdLiveData.value = Repository.getUserId()
    }

    fun getUserId(){
        Repository.getUserId()
    }
}