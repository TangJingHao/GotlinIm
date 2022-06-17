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

    //suzy：好友类型选择
    const val FRIEND_ACCOUNT = "FriendAccount"
    const val FRIEND_TYPE = "FriendType"
    const val FRIEND_IS = 1
    const val FRIEND_NO = 0

    //suzy：是否为群主
    const val GROUP_ID = "GroupId"
    const val OWNER_TYPE = "OwnerType"
    const val OWNER_IS = 1
    const val OWNER_NO = 0

    //suzy：全局TAG
    const val TAG_FRIEND_INFO="FriendInfoActivity"
    const val TAG_GROUP_INFO="GroupInfoActivity"
    const val TAG_SET_FRIEND_INFO="SetFriendInfoActivity"

    //网络类型状态码
    const val SUCCESS_STATUS=0
}