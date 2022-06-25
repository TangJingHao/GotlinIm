package com.ByteDance.Gotlin.im.util

import com.ByteDance.Gotlin.im.R

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

    //用户默认设置
    const val USER_DEFAULT_ID = -1
    const val USER_DEFAULT_NAME = "name"
    const val USER_DEFAULT_NICKNAME = "nickname"
    const val USER_DEFAULT_EMAIL = "email"
    const val USER_DEFAULT_SEX = "sex"

    //消息类型
    const val MESSAGE_TEXT = 0
    const val MESSAGE_IMG = 1

    //聊天类型
    const val CHAT_PRIVATE = 0
    const val CHAT_GROUP = 1

    //用户的状态设置
    const val USER_DEFAULT_MODE = 2//默认状态则不做处理
    const val USER_DARK_MODE = 1
    const val USER_LIGHT_MODE = 2

    // 网络相关
    const val BASE_URL = "http://chatspace.iceclean.top/space/"
    const val BASE_WS_URL = "ws://chatspace.iceclean.top/space/ws/chat/"
    const val SEND_MESSAGE = "SEND_MESSAGE"
    const val USER_ONLINE = "USER_ONLINE"

    //suzy：好友类型选择
    const val FRIEND_ACCOUNT = "FriendAccount"
    const val FRIEND_TYPE = "FriendType"
    const val FRIEND_NAME = "FriendName"
    const val FRIEND_NICKNAME = "FriendNickname"
    const val FRIEND_GROUPING = "FriendGrouping"
    const val FRIEND_IS = 1
    const val FRIEND_NO = 0

    //suzy：是否为群主
    const val GROUP_ID = "GroupId"
    const val OWNER_TYPE = "OwnerType"
    const val OWNER_IS = 1
    const val OWNER_NO = 0
    const val GROUP_NAME = "GroupName"
    const val GROUP_NUM = "GroupNum"
    const val GROUP_MY_NAME = "GroupMyName"
    const val GROUP_OWNER = "GroupOwner"

    //suzy：全局TAG
    const val TAG_FRIEND_INFO = "FriendInfoActivity"
    const val TAG_GROUP_INFO = "GroupInfoActivity"
    const val TAG_SET_FRIEND_INFO = "SetFriendInfoActivity"

    //suzy：跳转
    const val SEARCH_FROM_INFO_TYPE = "search_type"
    const val SEARCH_TYPE_FROM_FRIEND = 2
    const val SEARCH_TYPE_FROM_GROUP = 2
    const val SEARCH_HISTORY = 6

    //默认图片
    const val DEFAULT_IMG: Int = R.drawable.ic_img_default

    //网络类型状态码
    const val SUCCESS_STATUS = 0
}