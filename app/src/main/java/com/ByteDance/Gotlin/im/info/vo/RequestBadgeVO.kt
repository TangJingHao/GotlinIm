package com.ByteDance.Gotlin.im.info.vo

/**
 * @Author Zhicong Deng
 * @Date 2022/6/25 17:36
 * @Email 1520483847@qq.com
 * @Description
 */
data class RequestBadgeVO(
    /** 所有的未读申请  */
    val total: Int,

    /** 未读的好友申请  */
    val friend: Int,

    /** 未读的群聊申请  */
    val group: Int
)