package com.ByteDance.Gotlin.im.info

data class LoginDataResponse(
    val `data`: Data,
    val msg: String,
    val status: Int
)
data class Data(
    val success: Boolean,
    val user: User
)

data class User(
    val online: Boolean,
    val userAvatar: String,
    val userId: Int,
    val userName: String,
    val userNick: String
)