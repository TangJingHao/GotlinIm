package com.ByteDance.Gotlin.im.info.response

import android.service.autofill.UserData
import com.ByteDance.Gotlin.im.info.GroupListData
import com.ByteDance.Gotlin.im.info.User
import com.ByteDance.Gotlin.im.info.vo.GroupVO
import com.ByteDance.Gotlin.im.info.vo.UserVO
import com.google.gson.annotations.SerializedName

/**
 * @Description：
 * @Author：Suzy.Mo
 * @Date：2022/6/19 11:27
 */
data class GroupMembersDataResponse(
    @SerializedName("status")
    val status: Int,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("data")
    var data: GroupUserListData
)

data class GroupUserListData(
    @SerializedName("groupUserList")
    var groupUserList: List<UserVO>
)