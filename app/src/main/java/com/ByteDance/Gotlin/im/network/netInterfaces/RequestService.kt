package com.ByteDance.Gotlin.im.network.netInterfaces

import com.ByteDance.Gotlin.im.info.response.*
import retrofit2.Call
import retrofit2.http.*
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * @Author Zhicong Deng
 * @Date  2022/6/25 17:40
 * @Email 1520483847@qq.com
 * @Description 各种好友群聊添加申请
 */
interface RequestService {

    /**
     * 用户模糊搜索
     * 可以通过用户 ID、用户名、昵称、邮箱搜索，用户 ID 和邮箱是精确搜索，其他为模糊查询
     */
    @GET("user/search")
    fun searchUser(
        @Header("token") token: String,
        @Query("key") key: String
    ): Call<SearchUserDataResponse>

    /**
     * 获取未读申请数量
     * 获取当前用户未读的好友和群聊申请
     */
    @GET("request/badge")
    fun getRequestBadge(
        @Header("token") token: String,
        @Query("userId") userId: Int
    ): Call<RequestBadgeDataResponse>

    /**
     * 获取申请列表
     * 获取与用户相关的所有申请，分为 4 类
     */
    @GET("request/list")
    fun getRequestList(
        @Header("token") token: String,
        @Query("userId") userId: Int
    ): Call<RequestListDataResponse>

    /**
     * 好友申请
     * 申请添加某用户为好友
     */
    @POST("request/friend ")
    fun postRequestFriend(
        @Header("token") token: String,
        @Query("senderId") senderId: Int,
        @Query("userId") userId: Int,
        @Query("reqSrc") reqSrc: String,
        @Query("reqRemark") reqRemark: String
    ): Call<DefaultResponse>

    /**
     *  申请加入某群聊
     */
    @POST("request/group")
    fun postRequestGroup(
        @Header("token") token: String,
        @Query("senderId") senderId: Int,
        @Query("groupId") groupId: Int,
        @Query("reqSrc") reqSrc: String,
        @Query("reqRemark") reqRemark: String
    ): Call<DefaultResponse>


    /**
     * 处理申请信息
     * 同意或拒绝某用户的申请
     */
    @PATCH("request/handle")
    fun patchRequestHandle(
        @Header("token") token: String,
        @Query("reqId") reqId: Int,
        @Query("access") access: Boolean
    ): Call<DefaultResponse>
}