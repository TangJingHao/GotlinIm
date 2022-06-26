package com.ByteDance.Gotlin.im.info.vo

import java.lang.Deprecated

/**
 * @Author Zhicong Deng
 * @Date  2022/6/12 10:32
 * @Email 1520483847@qq.com
 * @Description 即将报废
 * 在后台接口文档完成前，仅用于私人测试调用
 */
@Deprecated
data class TestUser(
    val userAvatar: String,
    val userMail: String,
    val userName: String,
    val statue: String,
    val msg: String,
)
