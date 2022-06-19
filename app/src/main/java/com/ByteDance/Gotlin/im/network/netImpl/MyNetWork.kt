package com.ByteDance.Gotlin.im.network.netImpl

import com.ByteDance.Gotlin.im.network.base.ServiceCreator
import com.ByteDance.Gotlin.im.network.netInterfaces.AddressBookService
import com.ByteDance.Gotlin.im.network.netInterfaces.GroupService
import com.ByteDance.Gotlin.im.network.netInterfaces.LoginService
import com.ByteDance.Gotlin.im.network.netInterfaces.MsgService
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * @Author 唐靖豪
 * @Date 2022/6/10 17:24
 * @Email 762795632@qq.com
 * @Description
 */

object MyNetWork {
    private val loginService = ServiceCreator.create<LoginService>()
    private val addressBookService = ServiceCreator.create<AddressBookService>()
    private val msgService = ServiceCreator.create<MsgService>()
    private val groupService = ServiceCreator.create<GroupService>()

    suspend fun login(userName: String, userPass: String) =
        loginService.login(userName, userPass).await()

    suspend fun getGroupList(userId: Int) =
        addressBookService.getGroupList(userId).await()

    suspend fun getFriendList(userId: Int) =
        addressBookService.getFriendList(userId).await()

    suspend fun getSessionList(userId: Int) =
        msgService.getSessionList(userId).await()

    suspend fun getSessionHistoryList(userId: Int, sessionId: Int, page: Int) =
        msgService.getSessionHistoryList(userId, sessionId, page).await()

    suspend fun getGroupMemberList(groupId: Int)=
        groupService.getGroupMemberList(groupId).await()


    // 测试用websocket方法
    fun getWebSocketAndConnect(request: Request, listener: WebSocketListener): WebSocket {
        val webSocket = ServiceCreator.WebSocketClient.newWebSocket(request, listener)
        ServiceCreator.WebSocketClient.dispatcher().executorService().shutdown()
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