package com.ByteDance.Gotlin.im.info.ws

import com.google.gson.annotations.SerializedName

/**
 * @Author Zhicong Deng
 * @Date  2022/6/25 1:29
 * @Email 1520483847@qq.com
 * @Description
 */
data class WebSocketType(
    @SerializedName("wsType")
    val wsType: String
)
