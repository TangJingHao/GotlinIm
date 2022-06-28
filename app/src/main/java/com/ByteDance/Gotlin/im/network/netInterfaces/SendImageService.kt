package com.ByteDance.Gotlin.im.network.netInterfaces

import com.ByteDance.Gotlin.im.info.response.ImageData
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

/**
 * @Author 唐靖豪
 * @Date 2022/6/26 22:26
 * @Email 762795632@qq.com
 * @Description
 */

interface SendImageService {
    @Multipart
    @POST("user/avatar/{userId}")
    fun sendImage(
        @Header("token")token:String,
        @Path("userId")userId:Int,
        @Part body:MultipartBody.Part
    ):Call<ImageData>
}