package com.ByteDance.Gotlin.im.network.netInterfaces

import com.ByteDance.Gotlin.im.bean.ChangeUserInfoBean
import com.ByteDance.Gotlin.im.info.response.DefaultResponse
import retrofit2.Call
import retrofit2.http.*

/**
 * @Author 唐靖豪
 * @Date 2022/6/27 19:58
 * @Email 762795632@qq.com
 * @Description:修改各种用户信息的接口
 */
interface ChangeUserDataService {
    @PATCH("user/info/{userId}")
    fun changeUserInfo(
        @Header("token")token:String,
        @Path("userId")userId:Int,
        @Query("sex") sex: String,
        @Query("nickname")nickname:String
    ):Call<DefaultResponse>

    @PATCH("user/info/{userId}")
    fun changeFriendNickName(
        @Header("token")token:String,
        @Path("userId")userId:Int,
        @Query("nickname")nickname:String
    ):Call<DefaultResponse>
}