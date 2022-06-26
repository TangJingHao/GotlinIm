package com.ByteDance.Gotlin.im.info.response

data class RegisterDataResponse(
    val `data`: RegisterData,
    val msg: String,
    val status: Int
)

data class RegisterData(
    val avatar: String,
    val email: String,
    val nickName: String,
    val online: Boolean,
    val sex: String,
    val userId: Int,
    val userName: String
)