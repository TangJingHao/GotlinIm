package com.ByteDance.Gotlin.im.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.ByteDance.Gotlin.im.Repository
import com.ByteDance.Gotlin.im.util.Constants.TAG_FRIEND_INFO
import com.ByteDance.Gotlin.im.util.Mutils.MLogUtil.i

/**
 * @Description：好友信息相关的两个页面共用的ViewModel
 * @Author：Suzy.Mo
 * @Date：2022/6/14 13:20
 */
class FriendInfoViewModel : ViewModel() {

    //好友账号：用于搜索相关信息
    private val friendAccountLivaData = MutableLiveData<String>()

    //获取到的好友信息
    val friendInfoLiveData = Transformations.switchMap(friendAccountLivaData){account->
        i(TAG_FRIEND_INFO,"===中转：向仓库层获取好友信息===")
        Repository.getFriendInfo(account)
    }

    //修改的昵称
    private val modifyNicknameLiveData = MutableLiveData<String>()

    //申请修改后的数据
//    val nickNameLiveData =Transformations.switchMap(modifyNicknameLiveData){ newNickname->
//        i(TAG_FRIEND_INFO,"===中转：向仓库层申请修改===")
//        Repository.getFriendInfo(newNickname)
//    }

    //修改分组 参数还有变
//    var modifyGroupingLiveData = Transformations.switchMap(friendAccountLivaData){account->
//        i(TAG_FRIEND_INFO,"")
//        Repository.getFriendInfo(account)
//    }

    fun getFriendInfo(friendAccount:String) {
        i(TAG_FRIEND_INFO,"---获取好友信息---")
        friendAccountLivaData.postValue(friendAccount)
    }

    fun saveNickName(nickName:String){

    }

}