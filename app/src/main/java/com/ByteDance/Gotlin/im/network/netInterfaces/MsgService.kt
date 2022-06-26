package com.ByteDance.Gotlin.im.network.netInterfaces

import com.ByteDance.Gotlin.im.info.SessionHistoryDataResponse
import com.ByteDance.Gotlin.im.info.SessionListDataResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

/**
 * @Author Zhicong Deng
 * @Date  2022/6/14 21:27
 * @Email 1520483847@qq.com
 * @Description
 */
interface MsgService {
    /**
     * 获取消息列表
     * 获取目标用户在每个接收域中的最后一条聊天记录
     */
    @GET("session/list")
    fun getSessionList(
        @Header("token")token:String,
        @Query("userId") userId: Int
    ): Call<SessionListDataResponse>

    /**
     * 获取聊天记录
     * 分页获取目标用户在指定接收域中的历史聊天记录
     */
    @GET("session/history")
    fun getSessionHistoryList(
        @Header("token")token:String,
        @Query("userId") userId: Int,
        @Query("sessionId") sessionId: Int,
        @Query("page") page: Int
    ): Call<SessionHistoryDataResponse>
}