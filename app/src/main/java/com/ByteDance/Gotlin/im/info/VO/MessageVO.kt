package com.ByteDance.Gotlin.im.info.VO


/**
 * @Author Zhicong Deng
 * @Date 2022/6/14 20:33
 * @Email 1520483847@qq.com
 * @Description 后台-聊天消息实体
 */
data class MessageVO(
    /** 消息所属会话  */
    val session: SessionVO,

    /** 消息发送者  */
    val sender: UserVO,

    /** 消息类型  */
    val type: Int,

    /** 消息内容  */
    val content: String,

    /** 消息发送时间  */
    val sendTime: String,

    /** 当前用户是否为消息发送者  */
    val self: Boolean
)