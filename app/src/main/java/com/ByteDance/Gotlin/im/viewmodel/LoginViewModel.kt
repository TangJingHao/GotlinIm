package com.ByteDance.Gotlin.im.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
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
@RequiresApi(Build.VERSION_CODES.Q)
class LoginViewModel : ViewModel() {
    //获取输入结果
    private val mLoginLiveData = MutableLiveData<LoginLiveData>()
    //接受返回结果
    val loginObserverData = Transformations.switchMap(mLoginLiveData) {
        Repository.login(it.userName, it.userPass)
    }

    /**
     * 暴露给外部调用
     */
    fun login(loginLiveData: LoginLiveData) {
        mLoginLiveData.postValue(loginLiveData)
    }
}