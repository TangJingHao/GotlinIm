package com.ByteDance.Gotlin.im.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.ByteDance.Gotlin.im.Repository
import com.ByteDance.Gotlin.im.util.Constants
import com.ByteDance.Gotlin.im.util.Mutils.MLogUtil

/**
 * @Description：群聊信息ViewModel
 * @Author：Suzy.Mo
 * @Date：2022/6/14 15:01
 */
class GroupInfoViewModel:ViewModel() {

    private val groupIdLivaData = MutableLiveData<String>()

    val groupInfoLiveData = Transformations.switchMap(groupIdLivaData){ groupId->
        MLogUtil.i(Constants.TAG_GROUP_INFO, "")
        Repository.getGroupInfo(groupId)
    }

    fun getGroupInfo(groupId:String) {
        MLogUtil.i(Constants.TAG_GROUP_INFO, "---获群聊信息---")
        groupIdLivaData.postValue(groupId)
    }

    private val groupMembersLiveData = MutableLiveData<Int>()

    val groupMemberListLiveData = Transformations.switchMap(groupMembersLiveData){id->
        MLogUtil.i(Constants.TAG_GROUP_INFO, "---获群聊信息---")
        Repository.getGroupMembersList(id)
    }

    fun getGroupMembers(groupId:Int) {
        MLogUtil.i(Constants.TAG_GROUP_INFO, "---获群聊成员列表---")
        groupMembersLiveData.postValue(groupId)
    }
}