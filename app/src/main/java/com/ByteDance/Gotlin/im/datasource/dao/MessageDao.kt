package com.ByteDance.Gotlin.im.datasource.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ByteDance.Gotlin.im.entity.MessageEntity
import kotlinx.coroutines.flow.Flow
import java.sql.Date


/**
 * @Description：
 * @Author：Suzy.Mo
 * @Date：2022/6/19 21:52
 */
@Dao
interface MessageDao {

    @Query("select * from MessageTable order by sendTime desc")
    fun queryAllMessages(): LiveData<List<MessageEntity>>

    /**
     * 根据会话id查找消息
     */
    @Query(
        "select * from MessageTable where (sessionId = :sessionId)" +
                "order by sendTime desc"
    )
    fun queryMsgBySid(sessionId: Int): LiveData<List<MessageEntity>>


    @Query(
        "select * from MessageTable " +
                "where (sessionId = :sid) " +
                "and (sendTime between :from and :to)" +
                "and (content like '%' || :content || '%') " +
                "order by sendTime desc " +
                "limit :limit"
    )
    fun queryMessage(
        sid: Int,
        from: Date,
        to: Date,
        content: String,
        limit: Int // 一次获取的消息数量
    ): LiveData<List<MessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMessage(message: MessageEntity)

    @Update
    fun upDataMessage(message: MessageEntity)

    @Delete
    fun deleteMessage(message: MessageEntity)
}