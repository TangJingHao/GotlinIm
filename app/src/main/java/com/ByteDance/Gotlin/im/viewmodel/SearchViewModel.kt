package com.ByteDance.Gotlin.im.viewmodel

import android.icu.text.SimpleDateFormat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.ByteDance.Gotlin.im.Repository
import com.ByteDance.Gotlin.im.entity.MessageEntity
import com.ByteDance.Gotlin.im.info.vo.MessageVO
import com.ByteDance.Gotlin.im.model.MsgSearchLiveData
import kotlinx.coroutines.*
import java.sql.Date

/**
 * @Author Zhicong Deng
 * @Date 2022/6/12 10:35
 * @Email 1520483847@qq.com
 * @Description 搜索界面相关的ViewModel
 */
class SearchViewModel : ViewModel() {

    // 外部通过修改该对象进行不同查询
    private val mDBMsgLiveDate = MutableLiveData<MsgSearchLiveData>()

//    // 外部对此进行修改
//    private val mSearchHistoryMsgLiveDate = MutableLiveData<MsgSearchLiveData>()

    // 网络请求
    private val searchResultData = Transformations.switchMap(mDBMsgLiveDate) {
        Repository.getSessionHistoryList(getUserId(), it.sid, it.page)
    }

    /**
     * 外部搜索用
     */
    fun searchSessionMsg(msgSearchLiveData: MsgSearchLiveData) {
        // TODO 可以在此处做搜索类型判断
        mDBMsgLiveDate.value = msgSearchLiveData
    }

    fun getUserId() = Repository.getUserId()

    //     提供给外部监听，在网络请求变化后更新数据库，返回数据库的LiveData
    val searchResultObserverData = Transformations.switchMap(searchResultData) {
        val response = it.getOrNull()
        if (response == null) {
            // 网络请求失败，直接返回
            null
        } else {
            // 使用协程
            val historyList = response.data.historyList
            // 协程返回数据的方法
            runBlocking {
                val res = async {
                    // 先插入数据
                    insertMessageList(historyList)
                    // 从数据库中获取，作为返回值
                    // TODO 传入值
                    Repository.queryMessage(6, Date(0), Date(System.currentTimeMillis()), "聊天")
                }.await()
                // 阻塞等待返回结果
                return@runBlocking res
            }
        }
    }

    // 数据库插入数据
    private fun insertMessageList(messageList: List<MessageVO>) {
        GlobalScope.launch(Dispatchers.IO) {
            for (msg in messageList) {
                Repository.insertMessage(VO2Entity(msg))
            }
        }
    }

    // VO类型转换为Entity类型存储
    private fun VO2Entity(msg: MessageVO): MessageEntity {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val date = format.parse(msg.sendTime)
        val sendTime = Date(date.time);
        return MessageEntity(
            msg.session.sessionId,
            msg.sender.userId,
            msg.sender.userName,
            msg.sender.avatar,
            msg.type,
            msg.content, sendTime, msg.self
        )
    }

    data class SidAndPage(
        val sid: Int,
        val page: Int
    )


}

