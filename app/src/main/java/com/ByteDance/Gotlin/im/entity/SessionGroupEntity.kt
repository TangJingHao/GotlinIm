package com.ByteDance.Gotlin.im.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

/**
 * @Author Zhicong Deng
 * @Date 2022/6/26 23:44
 * @Email 1520483847@qq.com
 * @Description 存储群聊-会话关系表
 */

@Entity(tableName = "SessionGroupTable", primaryKeys = ["gid", "sid"])
data class SessionGroupEntity(
    /** 会话id  */
    val sid: Int,

    /** 群id  */
    val gid: Int
)