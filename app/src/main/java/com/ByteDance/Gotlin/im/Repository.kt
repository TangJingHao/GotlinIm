package com.ByteDance.Gotlin.im

import androidx.lifecycle.liveData
import com.ByteDance.Gotlin.im.network.netImpl.MyNetWork
import com.ByteDance.Gotlin.im.util.Constants
import com.ByteDance.Gotlin.im.util.Constants.TAG_FRIEND_INFO
import com.ByteDance.Gotlin.im.util.DUtils.DLogUtils
import com.ByteDance.Gotlin.im.util.DUtils.DLogUtils.i
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.Dispatchers
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import kotlin.coroutines.CoroutineContext

/**
 * @Author 唐靖豪
 * @Date 2022/6/9 19:43
 * @Email 762795632@qq.com
 * @Description
 */

object Repository {

    private const val TAG = "Repository"

    /*
    * MMKV==========================================================================================
    * */

    // MMKV实例
    private var mmkv: MMKV = MMKV.defaultMMKV()

    private const val MMKV_USER_ID = "userId"
    private const val MMKV_USER_MODE = "user_mode"
    private const val MMKV_USER_NICKNAME = "user_nickName"
    private const val MMKV_USER_AVATAR = "Avatar"
    private const val MMKV_USER_NAME = "user_name"

    fun getUserStatus(): Int = mmkv.decodeInt(MMKV_USER_MODE, Constants.USER_DEFAULT_MODE)
    fun saveUserStatus(userId: Int) = mmkv.encode(MMKV_USER_MODE, userId)

    /**
     * 添加/更新当前用户id
     */
    fun saveUserId(userId: Int) = mmkv.encode(MMKV_USER_ID, userId)

    /**
     * 获取当前用户id
     */
    fun getUserId(): Int = mmkv.decodeInt(MMKV_USER_ID, Constants.USER_DEFAULT_ID)

    /**
     * 删除当前用户id
     */
    fun deleteUserId() = mmkv.removeValueForKey(MMKV_USER_ID)

    /**
     * 获取当前用户nickName
     */
    fun getUsernickName() = mmkv.decodeString(MMKV_USER_NICKNAME, Constants.USER_DEFAULT_NICKNAME)

    /**
     * 获取当前用户头像
     */
    fun getUserAvatar() = mmkv.decodeString(MMKV_USER_AVATAR, Constants.USER_DEFAULT_AVATAR)

    fun getUserName() = mmkv.decodeString(MMKV_USER_NAME, Constants.USER_DEFAULT_NAME)

    /*
    * 数据库=========================================================================================
    */

    // 数据库名
    private const val DB_NAME = "im_chat_db"

    // room数据库，其中im_chat_db为数据库名
//    private val db = Room.databaseBuilder(
//        BaseApp.getContext(),
//        SQLDatabase::class.java, DB_NAME
//    ).build()

    /**
     * 演示用，请勿运行
     */
//    fun getBooks() = {
//        db.book().qeuryAll()
//    }


    /*
    * 网络请求=======================================================================================
    * */

    /**
     * 登录
     */
    fun login(userName: String, userPass: String) = fire(Dispatchers.IO) {
        val loginDataResponse = MyNetWork.login(userName, userPass)
        if (loginDataResponse.status == Constants.SUCCESS_STATUS) {
            Result.success(loginDataResponse)
        } else {
            Result.failure(RuntimeException("返回值的status的${loginDataResponse.status}"))
        }
    }

    /**
     * 获取群聊列表
     */
    fun getGroupList(userId: Int) = fire(Dispatchers.IO) {
        val groupListDataResponse = MyNetWork.getGroupList(userId)
        if (groupListDataResponse.status == Constants.SUCCESS_STATUS) {
            Result.success(groupListDataResponse)
        } else {
            Result.failure(RuntimeException("返回值的status的${groupListDataResponse.status}"))
        }
    }

    /**
     * 获取群聊成员列表
     */
    fun getGroupMembersList(userId: Int) = fire(Dispatchers.IO) {
        val groupMemberListDataResponse = MyNetWork.getGroupMemberList(userId)
        if (groupMemberListDataResponse.status == Constants.SUCCESS_STATUS) {
            Result.success(groupMemberListDataResponse)
        } else {
            Result.failure(RuntimeException("返回值的status的${groupMemberListDataResponse.status}"))
        }
    }

    /**
     * 获取邀请的群聊成员列表
     */
    fun getGroupInviteList(userId: Int) = fire(Dispatchers.IO) {
        val friendListDataResponse = MyNetWork.getFriendList(userId)
        if (friendListDataResponse.status == Constants.SUCCESS_STATUS) {
            Result.success(friendListDataResponse)
        } else {
            Result.failure(RuntimeException("返回值的status的${friendListDataResponse.status}"))
        }
    }

    /**
     * 获取好友列表
     */
    fun getFriendList(userId: Int) = fire(Dispatchers.IO) {
        val friendListDataResponse = MyNetWork.getFriendList(userId)
        if (friendListDataResponse.status == Constants.SUCCESS_STATUS) {
            Result.success(friendListDataResponse)
        } else {
            Result.failure(RuntimeException("返回值的status的${friendListDataResponse.status}"))
        }
    }

    /**
     * 获取用户在每个接收域中的最后一条聊天记录
     */
    fun getSessionList(userId: Int) = fire(Dispatchers.IO) {
        val sessionListDataResponse = MyNetWork.getSessionList(userId)
        DLogUtils.i(TAG, MyNetWork.getSessionList(userId).toString())
        if (sessionListDataResponse.status == Constants.SUCCESS_STATUS) {
            Result.success(sessionListDataResponse)
        } else {
            Result.failure(RuntimeException("返回值的status的${sessionListDataResponse.status}"))
        }
    }

    /**
     * 分页获取目标用户在指定接收域中的历史聊天记录
     */
    fun getSessionHistoryList(userId: Int, sessionId: Int, page: Int) = fire(Dispatchers.IO) {
        val sessionHistoryDataResponse = MyNetWork.getSessionHistoryList(userId, sessionId, page)
        if (sessionHistoryDataResponse.status == Constants.SUCCESS_STATUS) {
            Result.success(sessionHistoryDataResponse)
        } else {
            Result.failure(RuntimeException("返回值的status的${sessionHistoryDataResponse.status}"))
        }
    }

    /**
     * 保存备注
     */
    fun saveNickName(friendId: String, nickname: String) = liveData<String> {
        i(TAG_FRIEND_INFO, "---保存${friendId}的新备注${nickname}---")
        //emit(groupId)
    }

    /**
     * 获取分组
     */
    fun getAllGrouping(myId: String) = liveData<String> {
        i(TAG_FRIEND_INFO, "---获取分组---")
        emit(myId)
    }

    /**
     * 获取分组
     */
    fun getSelectedGrouping(myId: String) = liveData<String> {
        i(TAG_FRIEND_INFO, "---获取分组---")
        emit(myId)
    }

    /**
     * 保存分组
     */
    fun saveGrouping(myId: String, grouping: List<Map<String, Boolean>>) = liveData<String> {
        i(TAG_FRIEND_INFO, "---保存分组---")
        emit(myId)
    }

//    /**
//     * websocket使用
//     */
//    fun getWebSocketAndConnect(listener: WebSocketListener): WebSocket {
//        val request = Request.Builder()
//            .url(Constants.BASE_WS_URL + getUserId())
//            .build()
//        return MyNetWork.getWebSocketAndConnect(request, listener)
//    }

    /**
     * 获取群聊信息
     */
    fun getGroupInfo(groupId: String) = liveData<String> {
        i(TAG_FRIEND_INFO, "---$groupId---")
        emit(groupId)
    }

    /**
     * 获取好友信息
     */
    fun getFriendInfo(account: String) = liveData<String> {
        i(TAG_FRIEND_INFO, "---$account---")
        emit(account)
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