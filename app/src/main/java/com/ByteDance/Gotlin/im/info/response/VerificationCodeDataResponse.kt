package com.ByteDance.Gotlin.im.info.response

data class VerificationCodeDataResponse(
    val `data`: InnerData,
    val msg: String,
    val status: Int
)

class Data()