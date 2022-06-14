package com.ByteDance.Gotlin.im

import android.provider.ContactsContract
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataScope
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.ByteDance.Gotlin.im.network.netImpl.MyNetWork
import com.ByteDance.Gotlin.im.util.Constants.TAG_FRIEND_INFO
import com.ByteDance.Gotlin.im.util.Mutils.MLogUtil.i
import com.tencent.mmkv.MMKV
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

    // MMKV实例
    private var mmkv: MMKV = MMKV.defaultMMKV()

    // 使用MMKV进行存储示例
    private const val MMKV_CUR_THEME = "key"

    /**
     * MMKV添加/更新当前主题
     */
    fun saveCurTheme(curTheme: String) {
        mmkv.encode(MMKV_CUR_THEME, curTheme)
    }

    /**
     * MMKV获取当前主题
     */
    fun getCurTheme(): String? = mmkv.decodeString(MMKV_CUR_THEME)

    /**
     * MMKV删除当前主题
     * 无返回值
     */
    fun deleteCurTheme() = mmkv.removeValueForKey(MMKV_CUR_THEME)


    fun login(userName: String, userPass: String) = fire(Dispatchers.IO) {

        val loginDataResponse = MyNetWork.login(userName, userPass)
        if (loginDataResponse.status == 0) {
            Result.success(loginDataResponse)
        } else {
            Result.failure(RuntimeException("返回值的status的${loginDataResponse.status}"))
        }
    }

    /**
     * 获取好友信息
     */
    fun getFriendInfo(account :String) = liveData<String> {
        i(TAG_FRIEND_INFO,"---$account---")
        emit(account)
    }

    /**
     * 获取群聊信息
     */
    fun getGroupInfo(groupId:String) = liveData<String> {
        i(TAG_FRIEND_INFO,"---$groupId---")
        emit(groupId)
    }

    /**
     * 保存备注
     */
    fun saveNickName(friendId:String,nickname: String) = liveData<String> {
        i(TAG_FRIEND_INFO,"---保存${friendId}的新备注${nickname}---")
        //emit(groupId)
    }

    /**
     * 获取分组
     */
    fun getAllGrouping(myId:String) = liveData<String> {
        i(TAG_FRIEND_INFO,"---获取分组---")
        emit(myId)
    }

    /**
     * 获取分组
     */
    fun getSelectedGrouping(myId:String) = liveData<String> {
        i(TAG_FRIEND_INFO,"---获取分组---")
        emit(myId)
    }

    /**
     * 保存分组
     */
    fun saveGrouping(myId:String,grouping: List<Map<String,Boolean>>) = liveData<String> {
        i(TAG_FRIEND_INFO,"---保存分组---")
        emit(myId)
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