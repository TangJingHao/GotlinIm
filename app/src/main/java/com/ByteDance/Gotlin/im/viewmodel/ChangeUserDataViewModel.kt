package com.ByteDance.Gotlin.im.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.ByteDance.Gotlin.im.Repository
import com.ByteDance.Gotlin.im.model.ChangeUserInfo

/**
 * @Author 唐靖豪
 * @Date 2022/6/27 20:08
 * @Email 762795632@qq.com
 * @Description
 */
@RequiresApi(Build.VERSION_CODES.Q)
class ChangeUserDataViewModel:ViewModel() {
    //获取输入结果
    private val mChangeUserInfo=MutableLiveData<ChangeUserInfo>()
    val changeUserInfoObserverData=Transformations.switchMap(mChangeUserInfo){
        Repository.changeUserInfoObserverData(it.userId,it.sex,it.nickname)
    }


    /**
     * 暴露
     */
    fun changeUserInfo(changeUserInfo: ChangeUserInfo){
        mChangeUserInfo.postValue(changeUserInfo)
    }
}