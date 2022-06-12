package com.ByteDance.Gotlin.im.info

/**
 * @Author Zhicong Deng
 * @Date  2022/6/12 10:32
 * @Email 1520483847@qq.com
 * @Description
 * 在后台接口文档完成前，仅用于私人测试调用,
 * 用于搜索新好友/群聊界面
 */
data class SearchUser(
    val userAvatar: String,
    val userMail: String,
    val userName: String,
    val statue: String
)
