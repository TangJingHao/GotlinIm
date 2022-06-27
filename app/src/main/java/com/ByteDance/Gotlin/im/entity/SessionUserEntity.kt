package com.ByteDance.Gotlin.im.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

/**
 * @Author Zhicong Deng
 * @Date 2022/6/26 23:44
 * @Email 1520483847@qq.com
 * @Description 存储用户-会话关系表
 */

@Entity(tableName = "SessionUserTable", primaryKeys = ["uid", "sid"])
data class SessionUserEntity(
    /** 会话id  */
    val sid: Int,

    /** 用户id  */
    val uid: Int
)