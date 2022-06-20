package com.ByteDance.Gotlin.im.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import java.sql.Date

/**
 * @Author Zhicong Deng
 * @Date 2022/6/20 21:58
 * @Email 1520483847@qq.com
 * @Description
 */

@Entity(tableName = "MessageTable", primaryKeys = ["sessionId", "senderId"])
data class MessageEntity(

    /** 消息所属会话  */
    val sessionId: Int,

    /** 消息发送者  */
    val senderId: Int,

    /** 消息类型  */
    @ColumnInfo(name = "msg_type")
    val type: Int,

    /** 消息内容  */
    val content: String,

    /** 消息发送时间  */
    val sendTime: Date,

    /** 当前用户是否为消息发送者  */
    val self: Boolean
)
