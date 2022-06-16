package com.ByteDance.Gotlin.im.info.vo

/**
 * @Author Zhicong Deng
 * @Date  2022/6/14 20:34
 * @Email 1520483847@qq.com
 * @Description 后台-好友实体
 */
data class FriendVO(
    /** 朋友的用户信息  */
    val user: UserVO,

    /** 当前用户对该朋友的备注  */
    val markName: String,

    /** 开始成为朋友的时间  */
    val startTime: String,

    /** 用户最后一条已阅消息  */
    val lastMsgId: Int,
)