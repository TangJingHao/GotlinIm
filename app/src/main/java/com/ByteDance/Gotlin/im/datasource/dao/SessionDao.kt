package com.ByteDance.Gotlin.im.datasource.dao

import androidx.room.*
import com.ByteDance.Gotlin.im.entity.SessionEntity

/**
 * @Author Zhicong Deng
 * @Date 2022/6/20 21:11
 * @Email 1520483847@qq.com
 * @Description
 */
@Dao
interface SessionDao {

    @Query("select * from SessionTable")
    fun queryAllSession(): List<SessionEntity>


    @Query("select * from SessionTable " +
            "where sessionId = :sessionId")
    fun querySessionById(sessionId: Int): SessionEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSession(SessionEntity: SessionEntity): Long

    @Delete
    fun deleteSession(SessionEntity: SessionEntity)

    @Update
    fun updateSession(SessionEntity: SessionEntity)
}