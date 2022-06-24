package com.ByteDance.Gotlin.im

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.ByteDance.Gotlin.im.application.BaseApp
import com.ByteDance.Gotlin.im.datasource.database.SQLDatabase
import com.ByteDance.Gotlin.im.entity.MessageEntity
import com.ByteDance.Gotlin.im.entity.SessionEntity
import com.ByteDance.Gotlin.im.entity.UserEntity
import com.ByteDance.Gotlin.im.info.vo.SessionVO
import com.ByteDance.Gotlin.im.info.vo.UserVO
import com.ByteDance.Gotlin.im.network.netImpl.NetWork
import com.ByteDance.Gotlin.im.util.Constants
import com.ByteDance.Gotlin.im.util.Constants.TAG_FRIEND_INFO
import com.ByteDance.Gotlin.im.util.DUtils.DLogUtils
import com.ByteDance.Gotlin.im.util.DUtils.DLogUtils.i
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.sql.Date
import kotlin.coroutines.CoroutineContext

/**
 * @Author 唐靖豪
 * @Date 2022/6/9 19:43
 * @Email 762795632@qq.com
 * @Description
 */

@OptIn(DelicateCoroutinesApi::class)
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
    private const val MMKV_USER_SEX = "user_sex"
    private const val MMKV_USER_EMAIL = "user_email"

    //模式
    fun getUserStatus(): Int = mmkv.decodeInt(MMKV_USER_MODE, Constants.USER_DEFAULT_MODE)
    fun saveUserStatus(userId: Int) = mmkv.encode(MMKV_USER_MODE, userId)
    fun deleteUserStatus() = mmkv.removeValueForKey(MMKV_USER_MODE)

    //用户id
    fun saveUserId(userId: Int) = mmkv.encode(MMKV_USER_ID, userId)
    fun getUserId(): Int = mmkv.decodeInt(MMKV_USER_ID, Constants.USER_DEFAULT_ID)
    fun deleteUserId() = mmkv.removeValueForKey(MMKV_USER_ID)

    /**
     * 获取当前用户nickName
     */
    fun getUsernickName() = mmkv.decodeString(MMKV_USER_NICKNAME, Constants.USER_DEFAULT_NICKNAME)

    /**
     * 获取当前用户头像
     */
    fun getUserAvatar() = mmkv.decodeInt(MMKV_USER_AVATAR, Constants.DEFAULT_IMG)

    fun getUserName() = mmkv.decodeString(MMKV_USER_NAME, Constants.USER_DEFAULT_NAME)

    fun getUserSex() = mmkv.decodeString(MMKV_USER_NAME, Constants.USER_DEFAULT_SEX)

    fun getUserEmail() = mmkv.decodeString(MMKV_USER_NAME, Constants.USER_DEFAULT_EMAIL)

    /*
    * 数据库=========================================================================================
    */

    // room数据库，其中im_chat_db为数据库名
    private val db = SQLDatabase.getDatabase(BaseApp.getContext())

    // 会话数据表
    fun queryAllSessions() = db.sessionDao().queryAllSession()
    fun querySessionById(sessionId: Int) = db.sessionDao().querySessionById(sessionId)
    fun insertSession(session: SessionVO) = db.sessionDao().insertSession(session)
    fun updateSession(session: SessionVO) = db.sessionDao().updateSession(session)
    fun deleteSession(session: SessionVO) = db.sessionDao().deleteSession(session)

    // 用户数据表
    fun queryAllUsers() = db.userDao().queryAllUsers()
    fun queryUserById(userId: Int) = db.userDao().queryUserById(userId)
    fun insertUser(user: UserVO) = db.userDao().insertUser(user)
    fun upDataUser(user: UserVO) = db.userDao().upDataUser(user)
    fun deleteUser(user: UserVO) = db.userDao().deleteUser(user)

    // 消息数据表
    /**
     * 根据会话id查找
     */
    fun queryMsgBySid(sid: Int) = db.messageDao().queryMsgBySid(sid)

    /**
     * 根据会话id，发送者id,时间范围以及消息模糊查找
     */
    fun queryMessage(sid: Int, from: Date, to: Date, content: String, limit: Int) =
        db.messageDao().queryMessage(sid, from, to, content, limit)

    fun queryAllMessages() = db.messageDao().queryAllMessages()
    fun insertMessage(msg: MessageEntity) = db.messageDao().insertMessage(msg)
    fun upDataMessage(msg: MessageEntity) = db.messageDao().upDataMessage(msg)
    fun deleteMessage(msg: MessageEntity) = db.messageDao().deleteMessage(msg)

    /*
    * 网络请求=======================================================================================
    * */

    /**
     * 登录
     */
    fun login(userName: String, userPass: String) = fire(Dispatchers.IO) {
        val loginDataResponse = NetWork.login(userName, userPass)
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
        val groupListDataResponse = NetWork.getGroupList(userId)
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
        val groupMemberListDataResponse = NetWork.getGroupMemberList(userId)
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
        val friendListDataResponse = NetWork.getFriendList(userId)
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
        val friendListDataResponse = NetWork.getFriendList(userId)
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
        val sessionListDataResponse = NetWork.getSessionList(userId)
        DLogUtils.i(TAG, NetWork.getSessionList(userId).toString())
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
        val sessionHistoryDataResponse = NetWork.getSessionHistoryList(userId, sessionId, page)
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

    /**
     * webSocket使用
     */
    fun getWebSocketAndConnect(listener: WebSocketListener): WebSocket {
        return runBlocking {
            val websocket = async {
                NetWork.getWebSocketAndConnect(
                    Request.Builder().url(Constants.BASE_WS_URL + getUserId()).build(), listener
                )
            }.await()
            return@runBlocking websocket
        }
    }

    /**
     * 获取群聊信息
     */
    fun getGroupInfo(groupId: Int) = liveData<Int> {
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