package com.ByteDance.Gotlin.im.datasource.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
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
}