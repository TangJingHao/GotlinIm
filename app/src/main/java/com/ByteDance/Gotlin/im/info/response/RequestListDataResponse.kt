package com.ByteDance.Gotlin.im.info.response

import com.ByteDance.Gotlin.im.info.vo.SessionRequestListVO
import com.google.gson.annotations.SerializedName

/**
 * @Author Zhicong Deng
 * @Date 2022/6/25 18:17
 * @Email 1520483847@qq.com
 * @Description
 */
data class RequestListDataResponse(
    @SerializedName("status")
    var status: Int,

    @SerializedName("msg")
    var msg: String,

    @SerializedName("data")
    var data: SessionRequestListVO
)