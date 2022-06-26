package com.ByteDance.Gotlin.im.network.netImpl

import android.os.Build
import androidx.annotation.RequiresApi
import com.ByteDance.Gotlin.im.Repository
import com.ByteDance.Gotlin.im.network.base.ServiceCreator
import com.ByteDance.Gotlin.im.network.netInterfaces.*
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Query
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * @Author 唐靖豪
 * @Date 2022/6/10 17:24
 * @Email 762795632@qq.com
 * @Description
 */
@RequiresApi(Build.VERSION_CODES.Q)
object NetWork {
    private val loginService = ServiceCreator.create<LoginService>()
    private val addressBookService = ServiceCreator.create<AddressBookService>()
    private val msgService = ServiceCreator.create<MsgService>()
    private val groupService = ServiceCreator.create<GroupService>()
    private val requestService = ServiceCreator.create<RequestService>()
    private val registerService = ServiceCreator.create<RegisterService>()

    suspend fun login(userName: String, userPass: String) =
        loginService.login(userName, userPass).await()

    suspend fun registerCode(userName: String, email: String) =
        registerService.registerForCode(userName, email).await()

    suspend fun register(
        userName: String,
        userPass: String,
        sex: String,
        email: String,
        code: String
    ) =
        registerService.registerForLogin(userName, userPass, sex, email, code).await()

    suspend fun getGroupList(userId: Int) =
        addressBookService.getGroupList(Repository.mToken, userId).await()

    suspend fun getFriendList(userId: Int) =
        addressBookService.getFriendList(Repository.mToken, userId).await()

    suspend fun getSessionList(userId: Int) =
        msgService.getSessionList(Repository.mToken, userId).await()

    suspend fun getSessionHistoryList(userId: Int, sessionId: Int, page: Int) =
        msgService.getSessionHistoryList(Repository.mToken, userId, sessionId, page).await()

    suspend fun getGroupMemberList(groupId: Int) =
        groupService.getGroupMemberList(Repository.mToken, groupId).await()

    suspend fun newGroup(uid: Int, groupName: String) =
        groupService.postNewGroup(Repository.mToken, uid, groupName).await()

    suspend fun getRequestBadge(userId: Int) =
        requestService.getRequestBadge(Repository.mToken, userId).await()

    suspend fun getRequestList(userId: Int) =
        requestService.getRequestList(Repository.mToken, userId).await()

    suspend fun postRequestFriend(senderId: Int, userId: Int, reqSrc: String, reqRemark: String) =
        requestService.postRequestFriend(Repository.mToken, senderId, userId, reqSrc, reqRemark)
            .await()

    suspend fun postRequestGroup(senderId: Int, groupId: Int, reqSrc: String, reqRemark: String) =
        requestService.postRequestGroup(Repository.mToken, senderId, groupId, reqSrc, reqRemark)
            .await()

    suspend fun patchRequestHandle(reqId: Int, access: Boolean) =
        requestService.patchRequestHandle(Repository.mToken, reqId, access).await()

    suspend fun searchUser(key: String) =
        requestService.searchUser(Repository.mToken, key).await()


    // 获取websocket并链接
    fun getWebSocketAndConnect(request: Request, listener: WebSocketListener): WebSocket {
        val webSocket = ServiceCreator.WebSocketClient.newWebSocket(request, listener)
        ServiceCreator.WebSocketClient.dispatcher().executorService()
        return webSocket
    }

    /**
     *  定义一个Call的扩展函数，Call的上下文是retrofit2,泛型T为interface内部定义好的方法
     */
    private suspend fun <T> Call<T>.await(): T {
        //返回suspendCoroutine函数来挂起协程
        return suspendCoroutine { continuation ->
            enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if (body != null) {
                        continuation.resume(body)
                    } else continuation.resumeWithException(RuntimeException("返回值为NULL，请重试"))
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }
}