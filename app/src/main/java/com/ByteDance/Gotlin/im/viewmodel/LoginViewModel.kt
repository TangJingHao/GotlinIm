package com.ByteDance.Gotlin.im.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.ByteDance.Gotlin.im.Repository
import com.ByteDance.Gotlin.im.model.LoginLiveData

/**
 * @Author 唐靖豪
 * @Date 2022/6/10 17:38
 * @Email 762795632@qq.com
 * @Description
 */

class LoginViewModel : ViewModel() {
    private val mLoginLiveData = MutableLiveData<LoginLiveData>()
    val loginObserverData = Transformations.switchMap(mLoginLiveData) {
        Repository.login(it.userName, it.userPass)
    }

    /**
     * 暴露给外部调用
     */
    fun login(loginLiveData: LoginLiveData) {
        mLoginLiveData.value = loginLiveData
    }
}