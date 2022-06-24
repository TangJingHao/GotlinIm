package com.ByteDance.Gotlin.im.datasource.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ByteDance.Gotlin.im.entity.SessionEntity
import com.ByteDance.Gotlin.im.info.vo.SessionVO

/**
 * @Author Zhicong Deng
 * @Date 2022/6/20 21:11
 * @Email 1520483847@qq.com
 * @Description
 */
@Dao
interface SessionDao {

    @Query("select * from SessionTable")
    fun queryAllSession(): LiveData<List<SessionVO>>


    @Query(
        "select * from SessionTable " +
                "where sessionId = :sessionId"
    )
    fun querySessionById(sessionId: Int): SessionVO

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSession(SessionEntity: SessionVO)

    @Delete
    fun deleteSession(SessionEntity: SessionVO)

    @Update
    fun updateSession(SessionEntity: SessionVO)
}