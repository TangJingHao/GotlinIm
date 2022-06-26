package com.ByteDance.Gotlin.im.info.response

import com.google.gson.annotations.SerializedName

/**
 * @Author Zhicong Deng
 * @Date 2022/6/27 2:42
 * @Email 1520483847@qq.com
 * @Description
 */
data class GroupCreateDataResponse(
    @SerializedName("status")
    var status: Int,

    @SerializedName("msg")
    var msg: String,

    @SerializedName("data")
    var data: NewGroupData
)

data class NewGroupData(
    @SerializedName("inTime")
    var inTime: String,

    @SerializedName("number")
    var number: Int,

    @SerializedName("groupName")
    var groupName: String,

    @SerializedName("lastMsgId")
    var lastMsgId: Int,

    @SerializedName("markName")
    var markName: Any,

    @SerializedName("groupId")
    var groupId: Int,

    @SerializedName("creatorId")
    var creatorId: Int,

    @SerializedName("avatar")
    var avatar: String,

    @SerializedName("onlineNum")
    var onlineNum: Int,

    @SerializedName("sessionId")
    var sessionId: Int,

    @SerializedName("notice")
    var notice: String
)