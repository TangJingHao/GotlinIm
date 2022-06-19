package com.ByteDance.Gotlin.im.info.vo

/**
 * @Author Zhicong Deng
 * @Date  2022/6/14 20:27
 * @Email 1520483847@qq.com
 * @Description 后台-用户实体
 */
data class UserVO(
    /** 用户 ID */
    val userId: Int,

    /** 用户名 */
    val userName: String,

    /** 用户昵称 */
    val nickName: String,

    /** 用户头像 */
    val avatar: String? = null,

    /** 用户是否在线 */
    val online: Boolean
)
