package com.ByteDance.Gotlin.im.datasource.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ByteDance.Gotlin.im.entity.UserEntity

/**
 * @Description：
 * @Author：Suzy.Mo
 * @Date：2022/6/19 20:41
 */
@Dao
interface UserDao {

    /**
     * 获取所有的好友
     */
    @Query("select * from UserTable")
    fun queryAllUsers(): LiveData<List<UserEntity>>

    @Query("select * from UserTable " +
            "where userId = :userId")
    fun queryUserById(userId: Int): UserEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: UserEntity)

    @Update
    fun upDataUser(user: UserEntity)

    @Delete
    fun deleteUser(user: UserEntity)

}