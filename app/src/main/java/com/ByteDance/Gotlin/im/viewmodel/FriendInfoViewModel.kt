package com.ByteDance.Gotlin.im.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.ByteDance.Gotlin.im.Repository
import com.ByteDance.Gotlin.im.util.Constants.TAG_FRIEND_INFO
import com.ByteDance.Gotlin.im.util.Mutils.MLogUtil.e
import com.ByteDance.Gotlin.im.util.Mutils.MLogUtil.i

/**
 * @Description：好友信息相关的两个页面共用的ViewModel
 * @Author：Suzy.Mo
 * @Date：2022/6/14 13:20
 */
class FriendInfoViewModel : ViewModel() {

    private val friendAccountLivaData = MutableLiveData<String>()

    val friendInfoLiveData = Transformations.switchMap(friendAccountLivaData){account->
        i(TAG_FRIEND_INFO,"")
        Repository.getFriendInfo(account)
    }

    var nickname = ""
    var grouping = ""

    fun getFriendInfo(friendAccount:String) {
        i(TAG_FRIEND_INFO,"---获取好友信息---")
        friendAccountLivaData.postValue(friendAccount)
    }
    fun setNickName(name:String){

    }
    fun setGrouping(){

    }

}