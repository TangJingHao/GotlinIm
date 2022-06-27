package com.ByteDance.Gotlin.im.info

import com.ByteDance.Gotlin.im.info.vo.UserVO
import com.google.gson.annotations.SerializedName

/**
 * @Author Zhicong Deng
 * @Date 2022/6/14 21:35
 * @Email 1520483847@qq.com
 * @Description 好友列表
 * 当前用户所有的好友
 */
data class FriendListDataResponse(
    @SerializedName("status")
    val status: Int,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("data")
    val data: FriendListData
)

data class FriendListData(
    @SerializedName("friendList")
    val friendList: List<FriendListBean>
)

data class FriendListBean(
    @SerializedName("user")
    var user: UserVO,

    @SerializedName("sessionId")
    var sessionId: Int,

    @SerializedName("markName")
    var markName: Any,

    @SerializedName("startTime")
    var startTime: String,

    @SerializedName("lastMsgId")
    var lastMsgId: Int

)