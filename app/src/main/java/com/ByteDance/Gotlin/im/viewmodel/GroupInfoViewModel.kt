package com.ByteDance.Gotlin.im.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.ByteDance.Gotlin.im.Repository
import com.ByteDance.Gotlin.im.util.Constants
import com.ByteDance.Gotlin.im.util.Mutils.MLogUtil
import com.ByteDance.Gotlin.im.util.Mutils.MLogUtil.v

/**
 * @Description：群聊信息ViewModel
 * @Author：Suzy.Mo
 * @Date：2022/6/14 15:01
 */
class GroupInfoViewModel : ViewModel() {

    private val groupIdLivaData = MutableLiveData<Int>()

    val groupInfoLiveData = Transformations.switchMap(groupIdLivaData) { groupId ->
        v(Constants.TAG_GROUP_INFO, "=====转向仓库层：获取群聊信息=====")
        Repository.getGroupList(groupId)
    }

    fun getGroupInfo(groupId: Int) {
        v(Constants.TAG_GROUP_INFO, "---获群聊信息---")
        groupIdLivaData.postValue(groupId)
    }

    private val groupMembersLiveData = MutableLiveData<Int>()

    val groupMemberListLiveData = Transformations.switchMap(groupMembersLiveData) { id ->
        v(Constants.TAG_GROUP_INFO, "=====转向仓库层：获取聊成员列表=====")
        Repository.getGroupMembersList(id)
    }

    fun getGroupMembers(groupId: Int) {
        MLogUtil.i(Constants.TAG_GROUP_INFO, "---获取群聊成员列表---")
        groupMembersLiveData.postValue(groupId)
    }

    private val getGroupInviteLivaData = MutableLiveData<Int>()

    val groupInviteLiveData = Transformations.switchMap(getGroupInviteLivaData) { id ->
        v(Constants.TAG_GROUP_INFO, "=====转向仓库层：获取邀请群成员=====")
        Repository.getGroupInviteList(id)
    }

    fun getInviteMembers(groupId: Int) {
        MLogUtil.i(Constants.TAG_GROUP_INFO, "---邀请群聊成员---")
        getGroupInviteLivaData.postValue(groupId)
    }

}