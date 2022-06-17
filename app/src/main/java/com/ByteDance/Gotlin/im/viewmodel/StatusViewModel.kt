package com.ByteDance.Gotlin.im.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * @Author 唐靖豪
 * @Date 2022/6/17 12:55
 * @Email 762795632@qq.com
 * @Description
 */

class StatusViewModel : ViewModel() {
    public val mStatus=MutableLiveData<Int>()

}