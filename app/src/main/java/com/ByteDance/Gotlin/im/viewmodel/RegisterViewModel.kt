package com.ByteDance.Gotlin.im.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.ByteDance.Gotlin.im.Repository
import com.ByteDance.Gotlin.im.model.RegisterCodeLiveData
import com.ByteDance.Gotlin.im.model.RegisterForUserLiveData

/**
 * @Author 唐靖豪
 * @Date 2022/6/26 10:36
 * @Email 762795632@qq.com
 * @Description
 */
@RequiresApi(Build.VERSION_CODES.Q)
class RegisterViewModel:ViewModel() {
    //获取输入结果
    private val mRegisterCodeLiveData=MutableLiveData<RegisterCodeLiveData>()
    //接受注册信息
    private val mRegisterUserLiveData=MutableLiveData<RegisterForUserLiveData>()
    //接受返回结果
    val registerObserverCodeData=Transformations.switchMap(mRegisterCodeLiveData){
        Repository.registerForCode(it.userName,it.email)
    }
    //用户注册
    val registerUserObserverData=Transformations.switchMap(mRegisterUserLiveData){
        Repository.register(it.userName,it.userPass,it.sex,it.email,it.code)
    }
    /**
     * 暴露给外部调用
     */
    fun registerForCode(registerCodeLiveData: RegisterCodeLiveData){
        mRegisterCodeLiveData.postValue(registerCodeLiveData)
    }

    fun registerUser(registerForUserLiveData: RegisterForUserLiveData){
        mRegisterUserLiveData.postValue(registerForUserLiveData)
    }

}