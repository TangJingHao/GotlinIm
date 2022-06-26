package com.ByteDance.Gotlin.im.model

/**
 * @Author 唐靖豪
 * @Date 2022/6/26 13:57
 * @Email 762795632@qq.com
 * @Description
 */

data class RegisterForUserLiveData(
    val userName: String,
    val userPass: String,
    val sex: String,
    val email: String,
    val code: String
)