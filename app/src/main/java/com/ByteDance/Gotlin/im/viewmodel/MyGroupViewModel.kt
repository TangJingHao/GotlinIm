package com.ByteDance.Gotlin.im.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.ByteDance.Gotlin.im.Repository
import com.ByteDance.Gotlin.im.entity.SessionGroupEntity
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

    val groupListDB = Transformations.switchMap(userIdObserverData){
        val response = it.getOrNull()
        if (response == null) {
            // 网络请求失败，直接返回
            null
        } else {
            // 使用协程
            val groupList = response.data.groupList
            // 协程返回数据的方法
            runBlocking {
                val res = async {
                    // 先插入数据
                    if (groupList != null && groupList.size > 0) {
                        insertSG(groupList)

                        MutableLiveData<Boolean>(true)
                    } else {
                       MutableLiveData<Boolean>(false)
                    }
                }.await()
                // 阻塞等待返回结果
                return@runBlocking res
            }
        }
    }

    private fun insertSG(groupList: List<GroupVO>) :MutableLiveData<Boolean>{
        GlobalScope.launch(Dispatchers.IO) {
            for (group in groupList) {
                // sg 关系表
                Repository.insertSG(SessionGroupEntity(group.sessionId, group.groupId))
            }

        }
       return MutableLiveData<Boolean>(true)
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
   private val startActivityData = MutableLiveData<GroupVO>()

    val startActivityObserver = Transformations.switchMap(startActivityData) {
        // 协程返回数据的方法
        runBlocking {
            val res = async {
                val sessionId: Int = withContext(Dispatchers.IO) {
                    Repository.querySidByGid(it.groupId)
                }
                MutableLiveData(SessionGroupLiveData(sessionId, it))
            }.await()
            // 阻塞等待返回结果
            return@runBlocking res
        }
    }

    fun getSessionByGroup(group: GroupVO) {
        startActivityData.postValue(group)
    }

}

data class SessionGroupLiveData(
    val sessionId: Int,
    val group: GroupVO
)