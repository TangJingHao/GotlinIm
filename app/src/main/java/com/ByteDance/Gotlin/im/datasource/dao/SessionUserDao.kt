package com.ByteDance.Gotlin.im.datasource.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ByteDance.Gotlin.im.entity.SessionUserEntity

/**
 * @Author Zhicong Deng
 * @Date  2022/6/26 23:50
 * @Email 1520483847@qq.com
 * @Description
 */
@Dao
interface SessionUserDao {

    /** 存储对应关系  */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSU(su: SessionUserEntity)

    @Query("delete from SessionUserTable")
    fun deleteAllSU()

    @Query("select uid  from SessionUserTable where sid =:sid")
    fun queryUidBySid(sid: Int): Int

    @Query("select sid  from SessionUserTable where uid =:uid")
    fun querySidByUid(uid: Int): Int

}