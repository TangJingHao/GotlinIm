package com.ByteDance.Gotlin.im.util

/**
 * @Author 唐靖豪
 * @Date 2022/6/9 21:09
 * @Email 762795632@qq.com
 * @Description
 */

object Constants {
    //图像裁剪，默认方形，可以自定义圆形
    const val DEFAULT_TYPE = 1
    const val CIRCLE_TYPE = 2

    //模式选择
    const val DARK_MODE = 1
    const val LIGHT_MODE = 2

    // 网络相关
    const val BASE_URL = "http://chatspace.iceclean.top/space/"
    const val BASE_WS_URL = "ws://chatspace.iceclean.top/space/ws/chat/"
    const val SEND_MESSAGE = "SEND_MESSAGE"
    const val USER_ONLINE = "USER_ONLINE"

    //好友类型选择
    const val FRIEND_TYPE = "FriendType"
    const val FRIEND_IS = 1
    const val FRIEND_NO = 0
}