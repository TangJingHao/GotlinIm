package com.ByteDance.Gotlin.im.info.response

import com.ByteDance.Gotlin.im.info.vo.UserVO
import com.google.gson.annotations.SerializedName

/**
 * @Author Zhicong Deng
 * @Date 2022/6/27 1:43
 * @Email 1520483847@qq.com
 * @Description
 */
data class SearchUserDataResponse(
    @SerializedName("status")
    var status: Int,

    @SerializedName("msg")
    var msg: String,

    @SerializedName("data")
    var data: SearchUserData

)

data class SearchUserData(
    @SerializedName("result")
    var result: List<UserVO>,

    @SerializedName("total")
    var total: Int

)
