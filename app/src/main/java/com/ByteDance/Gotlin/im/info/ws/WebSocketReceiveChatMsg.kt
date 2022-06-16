package com.ByteDance.Gotlin.im.info

import com.ByteDance.Gotlin.im.info.vo.SessionVO
import com.ByteDance.Gotlin.im.info.vo.UserVO
import com.google.gson.annotations.SerializedName

/**
 * @Author Zhicong Deng
 * @Date 2022/6/15 21:17
 * @Email 1520483847@qq.com
 * @Description WebSockt中用于接收信息的类
 */
data class WebSocketReceiveChatMsg (
    @SerializedName("wsContent")
    val wsContent: WSreceiveContent,

    @SerializedName("wsType")
    val wsType: String
)

data class WSreceiveContent (
    @SerializedName("content")
    val content: String,

    /**
     * 信息源来自自己
     */
    @SerializedName("self")
    val self: Boolean,

    @SerializedName("sendTime")
    val sendTime: String,

    @SerializedName("sender")
    val sender: UserVO,

    @SerializedName("session")
    val session: SessionVO,

    @SerializedName("type")
    val type: Int

)


