package com.ByteDance.Gotlin.im.info

import com.ByteDance.Gotlin.im.info.vo.SessionVO
import com.ByteDance.Gotlin.im.info.vo.UserVO
import com.google.gson.annotations.SerializedName

/**
 * @Author Zhicong Deng
 * @Date 2022/6/14 21:37
 * @Email 1520483847@qq.com
 * @Description 聊天记录
 * 分页获取目标用户在指定接收域中的历史聊天记录
 */
data class SessionHistoryDataResponse (
    @SerializedName("status")
    val status: Int,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("data")
    val data: SessionHistoryData
)

data class SessionHistoryData (
    @SerializedName("historyList")
    val historyList: List<HistoryListBean>
)

data class HistoryListBean (
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