package com.ByteDance.Gotlin.im.datasource.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ByteDance.Gotlin.im.entity.SessionGroupEntity

/**
 * @Author Zhicong Deng
 * @Date  2022/6/26 23:50
 * @Email 1520483847@qq.com
 * @Description
 */
@Dao
interface SessionGroupDao {

    /** 存储对应关系  */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSG(su: SessionGroupEntity)

    @Query("delete from SessionGroupTable")
    fun deleteAllSG()

    @Query("select gid  from SessionGroupTable where sid =:sid")
    fun queryGidBySid(sid: Int): Int

    @Query("select sid  from SessionGroupTable where gid =:gid")
    fun querySidByGid(gid: Int): Int

}