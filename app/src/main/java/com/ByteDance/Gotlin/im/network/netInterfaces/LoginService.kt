package com.ByteDance.Gotlin.im.network.netInterfaces

import com.ByteDance.Gotlin.im.info.LoginDataResponse
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * @Author 唐靖豪
 * @Date 2022/6/10 17:10
 * @Email 762795632@qq.com
 * @Description
 */

interface LoginService {
    @POST("user/login")
    fun login(@Query("userName")userName:String, @Query("userPass")userPass:String):Call<LoginDataResponse>
}