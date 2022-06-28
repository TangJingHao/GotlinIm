package com.ByteDance.Gotlin.im.info.vo

import androidx.room.ColumnInfo
import androidx.room.Entity

/**
 * @Author Zhicong Deng
 * @Date 2022/6/14 20:35
 * @Email 1520483847@qq.com
 * @Description 后台-群聊实体
 */
@Entity(tableName = "GroupTable", primaryKeys = ["groupId"])
data class GroupVO(
    /** 群聊 ID  */
    val groupId: Int,

    /** 群主 ID  */
    val creatorId: Int,

    /** 群聊名称  */
    val groupName: String,

    /** 群聊头像  */
    val avatar: String,

    /** 群聊人数  */
    val number: Int,

    /** 当前群聊公告  */
    val notice: String? = null,

    /** 群聊在线人数  */
    val onlineNum: Int,

    /** 用户群聊备注  */
    val markName: String? = null,

    /** 用户入群时间  */
    val inTime: String,

    /** 用户最后一条已阅的消息 ID  */
    val lastMsgId: Int,

    /** 该群聊的会话 ID */
    val sessionId: Int
)