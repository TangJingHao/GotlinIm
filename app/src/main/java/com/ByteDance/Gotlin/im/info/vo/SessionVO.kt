package com.ByteDance.Gotlin.im.info.vo

/**
 * @Author Zhicong Deng
 * @Date  2022/6/14 20:32
 * @Email 1520483847@qq.com
 * @Description 后台-会话实体
 */
data class SessionVO(
    /** 会话 ID  */
    val sessionId: Int,

    /** 会话类型  */
    val type: Int,

    /** 会话的名称  */
    val name: String,

    /** 会话的头像  */
    val avatar: String,

    /** 会话的总人数，群聊时生效  */
    val number: Int,

    /** 当前会话的在线人数，会话类型为好友时，0/1 表示在线或离线，类型为群聊则是在线人数  */
    val online: Int,

    /** 当前用户在该会话内的未读消息数  */
    val badgeNum: Int
)
