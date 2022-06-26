package com.ByteDance.Gotlin.im.info.vo

/**
 * @Author Zhicong Deng
 * @Date 2022/6/25 17:34
 * @Email 1520483847@qq.com
 * @Description
 */
data class SessionRequestListVO(
    /** 用户收到的好友申请列表  */
    val friendRequest: List<SessionRequestVO>,

    /** 用户收到的群聊申请列表  */
    val groupRequest: List<SessionRequestVO>,

    /** 用户发起的好友申请列表  */
    val requestFriend: List<SessionRequestVO>,

    /** 用户发起的群聊申请列表  */
    val requestGroup: List<SessionRequestVO>
)