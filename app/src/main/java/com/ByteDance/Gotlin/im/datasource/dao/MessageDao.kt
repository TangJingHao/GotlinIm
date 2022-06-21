package com.ByteDance.Gotlin.im.datasource.dao

import androidx.room.*
import com.ByteDance.Gotlin.im.entity.MessageEntity
import com.ByteDance.Gotlin.im.info.vo.MessageVO
import java.sql.Date

/**
 * @Description：
 * @Author：Suzy.Mo
 * @Date：2022/6/19 21:52
 */
@Dao
interface MessageDao {

    @Query("select * from MessageTable")
    fun queryAllMessages(): List<MessageEntity>

    /**
     * 根据会话id和发送者id查找消息
     */
    @Query(
        "select * from MessageTable " +
                "where (sessionId = :sessionId and sessionId = :userId)" +
                "order by sendTime desc"
    )
    fun queryMsgBySidAndUid(sessionId: Int, userId: Int): List<MessageEntity>

    /**
     * 根据会话id，发送者id以及时间范围内查找消息(降序)
     */
    @Query(
        "select * from MessageTable " +
                "where (sessionId = :sid and sessionId = :uid) " +
                "and (sendTime between :from and :to)" +
                "order by sendTime desc"
    )
    fun queryMsgByTime(sid: Int, uid: Int, from: Date, to: Date): List<MessageEntity>

    /**
     * 根据会话id，发送者id以及消息模糊查找
     */
    @Query(
        "select * from MessageTable " +
                "where (sessionId = :sid and sessionId = :uid) " +
                "and (content like '%' || :content || '%') " +
                "order by sendTime desc"
    )
    fun queryMsgByContext(sid: Int, uid: Int, content: String): List<MessageEntity>

    @Query(
        "select * from MessageTable " +
                "where (sessionId = :sid and sessionId = :uid) " +
                "and (sendTime between :from and :to)" +
                "and (content like '%' || :content || '%') " +
                "order by sendTime desc"
    )
    fun queryMessage(sid: Int, uid: Int, from: Date, to: Date, content: String): List<MessageEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMessage(message: MessageEntity): Long

    @Update
    fun upDataMessage(message: MessageEntity)

    @Delete
    fun deleteMessage(message: MessageEntity)
}