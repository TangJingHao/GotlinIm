package com.ByteDance.Gotlin.im.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.ByteDance.Gotlin.im.Repository
import com.ByteDance.Gotlin.im.entity.SessionUserEntity
import com.ByteDance.Gotlin.im.info.vo.GroupVO
import com.ByteDance.Gotlin.im.info.vo.SessionVO
import kotlinx.coroutines.*

/**
 * @Author Zhicong Deng
 * @Date 2022/6/15 13:04
 * @Email 1520483847@qq.com
 * @Description
 */
class MyGroupViewModel : ViewModel() {

    // 群聊列表=======================================================================================
    private val mUserIdLiveData = MutableLiveData<Int>()

    val userIdObserverData = Transformations.switchMap(mUserIdLiveData) {
        Repository.getGroupList(it)
    }

    fun getGroupList() {
        mUserIdLiveData.postValue(Repository.getUserId())
    }

    // 创建新群=======================================================================================
    private val groupNameLiveData = MutableLiveData<String>()

    val newGroupObserver = Transformations.switchMap(groupNameLiveData) {
        Repository.newGroup(it)
    }

    fun newGroup(input: String) {
        groupNameLiveData.postValue(input)
    }

    // 跳转监听=======================================================================================
//    val startActivityData = MutableLiveData<GroupVO>()
//
//    val startActivityObserver = Transformations.switchMap(startActivityData) {
//        // 协程返回数据的方法
//        runBlocking {
//            val res = async {
//                val session: SessionVO = withContext(Dispatchers.IO) {
//                    Repository.querySessionById(it.groupId)
//                }
//                MutableLiveData(SessionGroupLiveData(session, it))
//            }.await()
//            // 阻塞等待返回结果
//            return@runBlocking res
//        }
//    }
//
//    fun getSessionByGroup(group: GroupVO) {
//        startActivityData.postValue(group)
//    }

}

data class SessionGroupLiveData(
    val session: SessionVO,
    val group: GroupVO
)