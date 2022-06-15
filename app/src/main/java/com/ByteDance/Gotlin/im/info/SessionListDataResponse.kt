package com.ByteDance.Gotlin.im.info

import com.ByteDance.Gotlin.im.info.VO.SessionVO
import com.ByteDance.Gotlin.im.info.VO.UserVO
import com.google.gson.annotations.SerializedName

/**
 * @Author Zhicong Deng
 * @Date 2022/6/14 21:36
 * @Email 1520483847@qq.com
 * @Description 消息列表
 * 在每个接收域中的最后一条聊天记录
 */
data class SessionListDataResponse(
    @SerializedName("status")
    val status: Int,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("data")
    val data: SessionListData
)

data class SessionListData(
    @SerializedName("messageList")
    val messageList: List<MessageList>
)

data class MessageList(
    @SerializedName("session")
    val session: SessionVO,
    @SerializedName("sender")
    val sender: UserVO,
    @SerializedName("type")
    val type: Int,
    @SerializedName("content")
    val content: String,
    @SerializedName("sendTime")
    val sendTime: String,
    @SerializedName("self")
    val self: Boolean

)
