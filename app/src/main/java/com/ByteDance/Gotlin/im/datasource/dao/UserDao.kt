package com.ByteDance.Gotlin.im.datasource.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ByteDance.Gotlin.im.info.vo.UserVO

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
    fun queryAllUsers(): LiveData<List<UserVO>>

    @Query(
        "select * from UserTable " +
                "where userId = :userId and userId"
    )
    fun queryUserById(userId: Int): LiveData<UserVO>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: UserVO)

    @Update
    fun upDataUser(user: UserVO)

    @Delete
    fun deleteUser(user: UserVO)

    @Query("delete from UserTable ")
    fun deleteAllUser()

}