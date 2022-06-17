package com.ByteDance.Gotlin.im.info.vo

/**
 * @Author Zhicong Deng
 * @Date  2022/6/14 20:24
 * @Email 1520483847@qq.com
 * @Description 后台-用户在线实体
 */
data class UserOnlineVO(
    /** 用户 ID */
    val userId: Integer,

    /** 上线状态 */
    val online: Boolean,

    /** 发生状态变化的会话 */
    val sessionIdList: List<Integer>
)
