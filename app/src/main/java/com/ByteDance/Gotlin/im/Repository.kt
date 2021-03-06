package com.ByteDance.Gotlin.im

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.ByteDance.Gotlin.im.application.BaseApp
import com.ByteDance.Gotlin.im.datasource.database.SQLDatabase
import com.ByteDance.Gotlin.im.entity.MessageEntity
import com.ByteDance.Gotlin.im.info.LoginDataResponse
import com.ByteDance.Gotlin.im.info.User
import com.ByteDance.Gotlin.im.info.vo.SessionVO
import com.ByteDance.Gotlin.im.info.vo.UserVO
import com.ByteDance.Gotlin.im.network.base.ServiceCreator
import com.ByteDance.Gotlin.im.network.netImpl.NetWork
import com.ByteDance.Gotlin.im.network.netInterfaces.LoginService
import com.ByteDance.Gotlin.im.util.Constants
import com.ByteDance.Gotlin.im.util.Constants.TAG_FRIEND_INFO
import com.ByteDance.Gotlin.im.util.DUtils.DLogUtils
import com.ByteDance.Gotlin.im.util.DUtils.DLogUtils.i
import com.ByteDance.Gotlin.im.util.Tutils.TLogUtil
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.*
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import retrofit2.Call
import retrofit2.Callback
import java.sql.Date
import kotlin.coroutines.CoroutineContext


/**
 * @Author 唐靖豪
 * @Date 2022/6/9 19:43
 * @Email 762795632@qq.com
 * @Description
 */

@OptIn(DelicateCoroutinesApi::class)
@RequiresApi(Build.VERSION_CODES.Q)
object Repository {
    //临时存放token
    var mToken = ""

    private const val TAG = "仓库层"

    /*
    * MMKV==========================================================================================
    * */

    // MMKV实例
    private var mmkv: MMKV = MMKV.defaultMMKV()

    private const val MMKV_USER_ID = "userId"
    private const val MMKV_USER_MODE = "user_mode"
    private const val MMKV_USER_CHANGE = "user_change"
    private const val MMKV_USER_NICKNAME = "user_nickName"
    private const val MMKV_USER_AVATAR = "Avatar"
    private const val MMKV_USER_NAME = "user_name"
    private const val MMKV_USER_SEX = "user_sex"
    private const val MMKV_USER_EMAIL = "user_email"
    private const val MMKV_USER_DATA = "user_data"
    private const val MMKV_USER_TOKEN="user_token"

    private const val MMKV_LOGIN_USER_NAME = "login_user_name"//用户账户
    private const val MMKV_LOGIN_PASSWORD = "login_user_password"//用户密码

    //用户数据
    fun getUserData(): User = mmkv.decodeParcelable(MMKV_USER_DATA, User::class.java)
    fun setUserData(user: User) = mmkv.encode(MMKV_USER_DATA, user)
    fun deleteUserData() = mmkv.removeValueForKey(MMKV_USER_DATA)
    fun setToken(token:String)= mmkv.encode(MMKV_USER_TOKEN,token)
    fun getToken():String= mmkv.decodeString(MMKV_USER_TOKEN)
    fun deleteToken()= mmkv.removeValueForKey(MMKV_USER_TOKEN)
    //用户密码和账户(保存在本地的)
    fun getUserLoginUserName(): String = mmkv.decodeString(MMKV_LOGIN_USER_NAME, "")
    fun setUserLoginUserName(loginUserName: String) =
        mmkv.encode(MMKV_LOGIN_USER_NAME, loginUserName)

    fun deleteLoginUserName() = mmkv.removeValueForKey(MMKV_LOGIN_USER_NAME)
    fun getUserLoginPassword(): String = mmkv.decodeString(MMKV_LOGIN_PASSWORD, "")
    fun setUserLoginPassword(loginPassword: String) =
        mmkv.encode(MMKV_LOGIN_PASSWORD, loginPassword)

    fun deleteLoginPassword() = mmkv.removeValueForKey(MMKV_LOGIN_PASSWORD)

    //模式
    fun getUserMode(): Int = mmkv.decodeInt(MMKV_USER_MODE)
    fun saveUserMode(userMode: Int) = mmkv.encode(MMKV_USER_MODE, userMode)
    fun deleteUserMode() = mmkv.removeValueForKey(MMKV_USER_MODE)

    //记录用户是否修改了模式
    fun setUserChangeAction(changeMode: Int) = mmkv.encode(MMKV_USER_CHANGE, changeMode)
    fun getUserChangeAction(): Int = mmkv.decodeInt(MMKV_USER_CHANGE, Constants.USER_DEFAULT_MODE)

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
    fun getUserAvatar() = mmkv.decodeString(MMKV_USER_AVATAR, "../img/home.png")

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
    fun deleteAllSession() = db.sessionDao().deleteAllSession()
    fun querySessionByName(name:String) = db.sessionDao().querySessionByName(name)

    // 用户数据表
    fun queryAllUsers() = db.userDao().queryAllUsers()
    fun queryUserById(userId: Int) = db.userDao().queryUserById(userId)
    fun insertUser(user: UserVO) = db.userDao().insertUser(user)
    fun upDataUser(user: UserVO) = db.userDao().upDataUser(user)
    fun deleteUser(user: UserVO) = db.userDao().deleteUser(user)
    fun deleteAllUser() = db.userDao().deleteAllUser()

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
    fun deleteAllMessage() = db.messageDao().deleteAllMessage()


    fun deleteAllTable() {
        deleteAllUser()
        deleteAllSession()
        deleteAllMessage()
    }

    /*
    * 网络请求=======================================================================================
    * */

    /**
     * 登录
     */
    fun login(userName: String, userPass: String) = fire(Dispatchers.IO) {
        val loginDataResponse = NetWork.login(userName, userPass)
        val status = loginDataResponse.status
        if (status == Constants.SUCCESS_STATUS) {
            Result.success(loginDataResponse)
        } else {
            Result.failure(RuntimeException("返回值的status的${loginDataResponse.status}"))
        }
    }

    /**
     * 获取验证码
     */
    fun registerForCode(userName: String, email: String) = fire(Dispatchers.IO) {
        val registerForCodeResponse = NetWork.registerCode(userName, email)
        val status = registerForCodeResponse.status
        if (status == Constants.SUCCESS_STATUS) {
            Result.success(registerForCodeResponse)
        } else {
            TLogUtil.d("${registerForCodeResponse.status}")
            Result.failure(RuntimeException("返回值的status的${registerForCodeResponse.status}"))
        }
    }

    /**
     * token过期获取token的方法
     */
    fun getAndSetToken() {
        val tokenResponse = ServiceCreator.create<LoginService>()
            .login(Repository.MMKV_LOGIN_USER_NAME, Repository.MMKV_LOGIN_PASSWORD)
        tokenResponse.enqueue(object : Callback<LoginDataResponse> {
            override fun onResponse(
                call: Call<LoginDataResponse>,
                response: retrofit2.Response<LoginDataResponse>
            ) {
                val responseBody = response.body()
                if(responseBody!=null){
                    mToken =responseBody.data.token
                }
            }

            override fun onFailure(call: Call<LoginDataResponse>, t: Throwable) {
                TLogUtil.d("数据请求失败")
            }
        })

    }


    /**
     * 注册
     */
    fun register(userName: String, userPass: String, sex: String, email: String, code: String) =
        fire(Dispatchers.IO) {
            val registerResponse = NetWork.register(userName, userPass, sex, email, code)
            val status = registerResponse.status
            if (status == Constants.SUCCESS_STATUS) {
                Result.success(registerResponse)
            } else {
                TLogUtil.d("${registerResponse.status}")
                Result.failure(RuntimeException("返回值的status的${registerResponse.status}"))
            }
        }

    /**
     * 获取群聊列表
     */
    fun getGroupList(userId: Int) = fire(Dispatchers.IO) {
        val groupListDataResponse = NetWork.getGroupList(userId)
        val status = groupListDataResponse.status
        if (status == Constants.SUCCESS_STATUS || status == Constants.TOKEN_EXPIRED) {
            Result.success(groupListDataResponse)
        } else {
            Result.failure(RuntimeException("返回值的status的${groupListDataResponse.status}"))
        }
    }

    /**
     * 获取群聊成员列表
     */
    fun getGroupMembersList(userId: Int) = fire(Dispatchers.IO) {
        val groupMemberListDataResponse = NetWork.patchRequestHandle(userId)
        val status = groupMemberListDataResponse.status
        if (status == Constants.SUCCESS_STATUS || status == Constants.TOKEN_EXPIRED) {
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
        val status = friendListDataResponse.status
        if (status == Constants.SUCCESS_STATUS || status == Constants.TOKEN_EXPIRED) {
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
        val status = friendListDataResponse.status
        if (status == Constants.SUCCESS_STATUS || status == Constants.TOKEN_EXPIRED) {
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
        val status = sessionListDataResponse.status
        if (status == Constants.SUCCESS_STATUS || status == Constants.TOKEN_EXPIRED) {
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
        val status = sessionHistoryDataResponse.status
        if (status == Constants.SUCCESS_STATUS || status == Constants.TOKEN_EXPIRED) {
            Result.success(sessionHistoryDataResponse)
        } else {
            Result.failure(RuntimeException("返回值的status的${sessionHistoryDataResponse.status}"))
        }
    }

    /**
     * 获取当前用户未读的好友和群聊申请
     */
    fun getRequestBadge() = fire(Dispatchers.IO) {
        val requestBadge = NetWork.getRequestBadge(getUserId())
        val status = requestBadge.status
        if (status == Constants.SUCCESS_STATUS || status == Constants.TOKEN_EXPIRED) {
            Result.success(requestBadge)
        } else {
            Result.failure(RuntimeException("返回值的status的${requestBadge.status}"))
        }
    }

    /**
     * 获取与用户相关的所有申请，分为 4 类
     */
    fun getRequestList() = fire(Dispatchers.IO) {
        val requestList = NetWork.getRequestList(getUserId())
        val status = requestList.status
        if (status == Constants.SUCCESS_STATUS || status == Constants.TOKEN_EXPIRED) {
            Result.success(requestList)
        } else {
            Result.failure(RuntimeException("返回值的status的${requestList.status}"))
        }
    }

    /**
     * 申请添加某用户为好友
     */
    fun postRequestFriend(userId: Int, reqSrc: String, reqRemark: String) = fire(Dispatchers.IO) {
        val defaultResponse = NetWork.postRequestFriend(getUserId(), userId, reqSrc, reqRemark)
        val status = defaultResponse.status
        if (status == Constants.SUCCESS_STATUS || status == Constants.TOKEN_EXPIRED) {

            Result.success(defaultResponse)
        } else {
            Result.failure(RuntimeException("返回值的status的${defaultResponse.status}"))
        }
    }

    /**
     *  申请加入某群聊
     */
    fun postRequestGroup(groupId: Int, reqSrc: String, reqRemark: String) = fire(Dispatchers.IO) {
        val defaultResponse = NetWork.postRequestGroup(getUserId(), groupId, reqSrc, reqRemark)
        val status = defaultResponse.status
        if (status == Constants.SUCCESS_STATUS || status == Constants.TOKEN_EXPIRED) {
            Result.success(defaultResponse)
        } else {
            Result.failure(RuntimeException("返回值的status的${defaultResponse.status}"))
        }
    }

    /**
     * 同意或拒绝某用户的申请
     */
    fun patchRequestHandle(reqId: Int, access: Boolean) = fire(Dispatchers.IO) {
        val defaultResponse = NetWork.patchRequestHandle(reqId, access)
        val status = defaultResponse.status
        if (status == Constants.SUCCESS_STATUS || status == Constants.TOKEN_EXPIRED) {
            Result.success(defaultResponse)
        } else {
            Result.failure(RuntimeException("返回值的status的${defaultResponse.status}"))
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
     * 获取好友信息
     */
    fun getNickNameSave(nickname: String) = liveData<String> {
        i(TAG_FRIEND_INFO, "---$nickname---")
        //调后台接口上传

        //搜索数据库

        emit(nickname)
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

// WebSocket尝试解决方案==========================================================================
    /**
     * webSocket使用(即将弃用)
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

    // 开局创建WebSocket
    private fun getWebSocketAndConnect(): WebSocket {
        return runBlocking {
            val webSocket = withContext(Dispatchers.Default) {
                NetWork.getWebSocketAndConnect(
                    Request.Builder().url(Constants.BASE_WS_URL + getUserId()).build(),
                    EchoWebSocketListener()
                )
            }
            return@runBlocking webSocket
        }
    }

    private var webSocket: WebSocket? = null
    private val onWsOpenObserverData = MutableLiveData<Response>()
    private val onWsMessageObserverData = MutableLiveData<String>()
    private val onWsFailureObserverData = MutableLiveData<Throwable>()

    // 用于WebSocket观察返回数据，自行判断回调
    fun getWsOpenObserverData() = this.onWsOpenObserverData
    fun getWsMessageObserverData() = this.onWsMessageObserverData
    fun getFailureObserverData() = this.onWsFailureObserverData
    fun getWebSocket(): WebSocket {
        if (this.webSocket == null) {
            this.webSocket = getWebSocketAndConnect()
        }
        return webSocket!!
    }

    class EchoWebSocketListener : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            DLogUtils.i(TAG, "WebSocket链接开启$webSocket\n$response")
            onWsOpenObserverData.postValue(response)
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            DLogUtils.i(TAG, "回调$webSocket\n$text")
            onWsMessageObserverData.postValue(text)
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            DLogUtils.i(TAG, "回调$bytes")
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            DLogUtils.i(TAG, "链接关闭中")
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            DLogUtils.i(TAG, "链接已关闭")
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            DLogUtils.i(TAG, "链接失败\t$t\n$response")
            onWsFailureObserverData.postValue(t)
        }
    }
}