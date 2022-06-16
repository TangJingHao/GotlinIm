package com.ByteDance.Gotlin.im.network.base

import com.ByteDance.Gotlin.im.util.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @Author 唐靖豪
 * @Date 2022/6/9 21:04
 * @Email 762795632@qq.com
 * @Description
 */

object ServiceCreator {
    private val retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val webSocketRetrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)

    //指定对象类型关键字reified让泛型实例化可以获得相应的class文件
    //泛型实例化关键字（inline和reified）
    inline fun <reified T> create(): T = create(T::class.java)
}