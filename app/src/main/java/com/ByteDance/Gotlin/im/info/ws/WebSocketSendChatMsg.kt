package com.ByteDance.Gotlin.im.info

import com.google.gson.annotations.SerializedName

/**
 * @Author Zhicong Deng
 * @Date 2022/6/15 21:06
 * @Email 1520483847@qq.com
 * @Description WebSockt 中用于发送信息的类
 */
data class WebSocketSendChatMsg (

    /**
     * 固定写法，标识发送消息
     */
    @SerializedName("wsType")
    val wsType: String,

    @SerializedName("wsContent")
    val wsContent: WSsendContent
)

data class WSsendContent (
    /**
     * 会话目标id
     */
    @SerializedName("sessionId")
    val sessionId: Int,

    /**
     * 发送者ID
     */
    @SerializedName("senderId")
    val senderId: Int,

    /**
     * 消息类型
     */
    @SerializedName("type")
    val type: Int,

    /**
     * 消息主体
     */
    @SerializedName("content")
    val content: String
)