package com.ByteDance.Gotlin.im.datasource.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ByteDance.Gotlin.im.info.vo.GroupVO

/**
 * @Description：
 * @Author：Suzy.Mo
 * @Date：2022/6/19 20:41
 */
@Dao
interface GroupDao {

    @Query("select * from GroupTable")
    fun queryAllGroups(): LiveData<List<GroupVO>>

    @Query(
        "select * from GroupTable " +
                "where groupId = :gid"
    )
    fun queryGroupById(gid: Int): LiveData<GroupVO>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGroup(group: GroupVO)

    @Update
    fun upDataGroup(group: GroupVO)

    @Delete
    fun deleteGroup(group: GroupVO)

    @Query("delete from GroupTable ")
    fun deleteAllGroup()

}