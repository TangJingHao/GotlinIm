package com.ByteDance.Gotlin.im.network.netInterfaces

import com.ByteDance.Gotlin.im.info.FriendListDataResponse
import com.ByteDance.Gotlin.im.info.response.GroupMembersDataResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

/**
 * @Description：
 * @Author：Suzy.Mo
 * @Date：2022/6/19 11:24
 */
interface GroupService {

    /**
     * 获取群成员列表
     * 获取当前用户所有的好友
     */
    @GET("group/list/user")
    fun getGroupMemberList(
        @Header("token")token:String,
        @Query("groupId") groupId: Int
    ): Call<GroupMembersDataResponse>
}