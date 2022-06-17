package com.ByteDance.Gotlin.im.info

import com.ByteDance.Gotlin.im.info.vo.GroupVO
import com.google.gson.annotations.SerializedName

/**
 * @Author Zhicong Deng
 * @Date 2022/6/14 21:18
 * @Email 1520483847@qq.com
 * @Description 群聊列表
 * 当前用户所有的群聊
 */
data class GroupListDataResponse(
    @SerializedName("status")
    val status: Int,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("data")
    var data: GroupListData
)

data class GroupListData(
    @SerializedName("groupList")
    var groupList: List<GroupVO>
)
