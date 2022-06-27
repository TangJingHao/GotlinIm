package com.ByteDance.Gotlin.im.info.response

data class ImageData(
    val `data`: InnerData,
    val msg: String,
    val status: Int
)
data class InnerData(
    val avatarName: String
)
