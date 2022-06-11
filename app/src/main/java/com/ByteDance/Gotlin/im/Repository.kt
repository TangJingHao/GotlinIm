package com.ByteDance.Gotlin.im

import androidx.lifecycle.liveData
import com.ByteDance.Gotlin.im.network.netImpl.MyNetWork
import kotlinx.coroutines.Dispatchers
import java.lang.Exception
import java.lang.RuntimeException
import kotlin.coroutines.CoroutineContext

/**
 * @Author 唐靖豪
 * @Date 2022/6/9 19:43
 * @Email 762795632@qq.com
 * @Description
 */

object Repository {
    fun login(userName:String,userPass:String)= fire(Dispatchers.IO){
        val loginDataResponse=MyNetWork.login(userName, userPass)
        if(loginDataResponse.status==0){
            Result.success(loginDataResponse)
        }else{
            Result.failure(RuntimeException("返回值的status的${loginDataResponse.status}"))
        }
    }

    /**
     * 返回一个liveData(统一处理异常信息)
     */
    private fun <T> fire(context: CoroutineContext, block: suspend () -> Result<T>) =
        liveData<Result<T>>(context) {
            val result = try {
                block()
            } catch (e: Exception) {
                Result.failure<T>(e)
            }
            //发射包装结果
            emit(result)
        }
}