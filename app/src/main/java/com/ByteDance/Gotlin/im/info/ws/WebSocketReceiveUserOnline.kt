package com.ByteDance.Gotlin.im.info

import com.google.gson.annotations.SerializedName

/**
 * @Author Zhicong Deng
 * @Date 2022/6/15 21:26
 * @Email 1520483847@qq.com
 * @Description
 */
data class WebSocketReceiveUserOnline(
    @SerializedName("wsContent")
    val wsContent: WsUserOnlineContent,

    @SerializedName("wsType")
    val wsType: String

    )

data class WsUserOnlineContent(
    @SerializedName("online")
    val online: Boolean,

    @SerializedName("sessionIdList")
    val sessionIdList: List<Int>,

    @SerializedName("userId")
    val userId: Int
)