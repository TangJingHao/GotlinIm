package com.ByteDance.Gotlin.im.info.vo

/**
 * @Author Zhicong Deng
 * @Date 2022/6/25 17:32
 * @Email 1520483847@qq.com
 * @Description
 */
data class SessionRequestVO(
    /** 申请序列号  */
    val reqId: Int,

    /** 会话类型（0 好友，1 群聊）  */
    val type: Int,

    /** 请求类型（0 用户收到的申请，1 用户发起的申请）  */
    val kind: Int,

    /** 用户对象  */
    val user: UserVO,

    /** 群聊对象  */
    val group: GroupVO,

    /** 请求来源  */
    val reqSrc: String,

    /** 请求备注  */
    val reqRemark: String,

    /** 请求状态（0 未读，1 已读，2 接受，3 拒绝）对于发送方，0 和 1 都是等待验证状态  */
    val reqStatus: Int,

    /** 请求发送时间  */
    val sendTime: String
)