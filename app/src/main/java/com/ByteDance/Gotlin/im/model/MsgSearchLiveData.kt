package com.ByteDance.Gotlin.im.model

import java.sql.Date

/**
 * @Author Zhicong Deng
 * @Date 2022/6/21 10:58
 * @Email 1520483847@qq.com
 * @Description
 */
data class MsgSearchLiveData(
    var sid: Int = 0,
    var from: Date = Date(0),
    var to: Date = Date(System.currentTimeMillis()),
    var content: String = "",
    var page: Int // 网络搜索用
)