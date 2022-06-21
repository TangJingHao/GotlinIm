package com.ByteDance.Gotlin.im.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import java.io.Serializable

/**
 * @Author Zhicong Deng
 * @Date 2022/6/21 1:17
 * @Email 1520483847@qq.com
 * @Description
 */
@Entity(tableName = "SessionTable", primaryKeys = ["sessionId"])
data class SessionEntity(
    /** 会话 ID  */
    val sessionId: Int,

    /** 会话类型  */
    val type: Int,

    /** 会话的名称  */
    val name: String,

    /** 会话的头像  */
    @ColumnInfo(name = "session_avatar")
    val avatar: String,

    /** 会话的总人数，群聊时生效  */
    val number: Int,

    /** 当前会话的在线人数，会话类型为好友时，0/1 表示在线或离线，类型为群聊则是在线人数  */
    @ColumnInfo(name = "session_online")
    val online: Int,

    /** 当前用户在该会话内的未读消息数  */
    val badgeNum: Int

)
