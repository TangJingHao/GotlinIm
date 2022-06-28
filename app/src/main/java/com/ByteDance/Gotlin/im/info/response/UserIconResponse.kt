package com.ByteDance.Gotlin.im.info.response

data class UserIconResponse(
    val `data`: IconData,
    val msg: String,
    val status: Int
)

data class IconData(
    val avatarName: String
)