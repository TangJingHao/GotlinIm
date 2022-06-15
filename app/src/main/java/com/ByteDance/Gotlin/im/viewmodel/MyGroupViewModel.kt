package com.ByteDance.Gotlin.im.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.ByteDance.Gotlin.im.Repository.getGroupList
import androidx.lifecycle.ViewModel
import com.ByteDance.Gotlin.im.Repository

/**
 * @Author Zhicong Deng
 * @Date 2022/6/15 13:04
 * @Email 1520483847@qq.com
 * @Description
 */
class MyGroupViewModel : ViewModel() {
    // 通过暴露的方法来改变mUserIdLiveData，然后更新被监听对象，最后得到反馈
    private val mUserIdLiveData = MutableLiveData<Int>()

    // 交给外部监听
    val userIdObserverData = Transformations.switchMap(mUserIdLiveData) {
        Repository.getGroupList(it)
    }

    fun getGroupList() {
        mUserIdLiveData.value = Repository.getUserId()
    }

}