package com.ByteDance.Gotlin.im.network.netInterfaces

import android.os.Build
import androidx.annotation.RequiresApi
import com.ByteDance.Gotlin.im.Repository
import com.ByteDance.Gotlin.im.info.FriendListDataResponse
import com.ByteDance.Gotlin.im.info.GroupListDataResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

/**
 * @Author Zhicong Deng
 * @Date  2022/6/14 21:09
 * @Email 1520483847@qq.com
 * @Description
 */
@RequiresApi(Build.VERSION_CODES.Q)
interface AddressBookService {
    /**
     * 获取群聊列表
     * 获取当前用户所有的群聊
     */
    @GET("group/list")
    fun getGroupList(
        @Header("token")token:String,
        @Query("userId") userId: Int
    ): Call<GroupListDataResponse>

    /**
     * 获取好友列表
     * 获取当前用户所有的好友
     */
    @GET("friend/list")
    fun getFriendList(
        @Header("token")token:String,
        @Query("userId") userId: Int
    ): Call<FriendListDataResponse>
}