package com.ByteDance.Gotlin.im.model

import com.ByteDance.Gotlin.im.info.vo.SessionVO
import com.ByteDance.Gotlin.im.info.vo.UserVO

/**
 * @Author Zhicong Deng
 * @Date 2022/6/27 0:52
 * @Email 1520483847@qq.com
 * @Description
 */
data class SessionUserLiveData(
    val session: SessionVO,
    val user: UserVO
)