package com.ByteDance.Gotlin.im.network.netInterfaces

import com.ByteDance.Gotlin.im.info.response.RegisterDataResponse
import com.ByteDance.Gotlin.im.info.response.VerificationCodeDataResponse
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * @Author 唐靖豪
 * @Date 2022/6/26 10:28
 * @Email 762795632@qq.com
 * @Description
 */

interface RegisterService {
    @POST("user/code")
    fun registerForCode(
        @Query("userName") userName: String,
        @Query("email") email: String
    ): Call<VerificationCodeDataResponse>

    @POST("user/register")
    fun registerForLogin(
        @Query("userName")userName: String,
        @Query("userPass")userPass:String,
        @Query("sex")sex:String,
        @Query("email")email: String,
        @Query("code")code:String
    ):Call<RegisterDataResponse>

}