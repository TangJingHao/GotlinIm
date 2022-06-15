package com.ByteDance.Gotlin.im.network.netInterfaces

import com.ByteDance.Gotlin.im.info.FriendListDataResponse
import com.ByteDance.Gotlin.im.info.GroupListDataResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @Author Zhicong Deng
 * @Date  2022/6/14 21:09
 * @Email 1520483847@qq.com
 * @Description
 */
interface AddressBookService {
    /**
     * 获取群聊列表
     * 获取当前用户所有的群聊
     */
    @GET("group/list")
    fun getGroupList(
        @Query("userId") userId: Int
    ): Call<GroupListDataResponse>

    /**
     * 获取好友列表
     * 获取当前用户所有的好友
     */
    @GET("friend/list")
    fun getFriendList(
        @Query("userId") userId: Int
    ): Call<FriendListDataResponse>
}