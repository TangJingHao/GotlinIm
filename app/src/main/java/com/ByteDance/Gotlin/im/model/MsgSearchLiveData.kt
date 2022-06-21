package com.ByteDance.Gotlin.im.model

import java.sql.Date

/**
 * @Author Zhicong Deng
 * @Date 2022/6/21 10:58
 * @Email 1520483847@qq.com
 * @Description
 */
data class MsgSearchLiveData(
    var sid: Int,
    var from: Date? = null,
    var to: Date? = null,
    var content: String? = null,
    var page: Int // 网络搜索用
)