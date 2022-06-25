package com.ByteDance.Gotlin.im.info.response

import com.google.gson.annotations.SerializedName

/**
 * @Author Zhicong Deng
 * @Date 2022/6/25 18:08
 * @Email 1520483847@qq.com
 * @Description POST/PATCH 后返回信息,data为空
 */
data class DefaultResponse(
    @SerializedName("status")
    val status: Int,
    @SerializedName("msg")
    val msg: String
)