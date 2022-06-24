package com.ByteDance.Gotlin.im.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity

/**
 * @Author Zhicong Deng
 * @Date 2022/6/21 1:17
 * @Email 1520483847@qq.com
 * @Description //已弃用
 */
//@Entity(tableName = "UserTable", primaryKeys = ["userId"])
data class UserEntity(
    /** 用户 ID */
    val userId: Int,

    /** 用户名 */
    val userName: String,

    /** 用户性别 */
    val sex: String,

    /** 用户昵称 */
    val nickName: String,

    /** 用户邮箱 */
    val email: String,

    /** 用户头像 */
    val avatar: String? = null,

    /** 用户是否在线 */
    val online: Boolean
) {

}