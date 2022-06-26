package com.ByteDance.Gotlin.im.datasource.database

import android.content.Context
import androidx.room.*
import com.ByteDance.Gotlin.im.datasource.dao.MessageDao
import com.ByteDance.Gotlin.im.datasource.dao.SessionDao
import com.ByteDance.Gotlin.im.datasource.dao.SessionUserDao
import com.ByteDance.Gotlin.im.datasource.dao.UserDao
import com.ByteDance.Gotlin.im.entity.MessageEntity
import com.ByteDance.Gotlin.im.entity.SessionUserEntity
import com.ByteDance.Gotlin.im.info.vo.SessionVO
import com.ByteDance.Gotlin.im.info.vo.UserVO
import java.sql.Date

/**
 * @Author Zhicong Deng
 * @Date  2022/6/14 22:07
 * @Email 1520483847@qq.com
 * @Description 学习测试用数据库
 */
@Database(
    entities = [UserVO::class, SessionVO::class, MessageEntity::class, SessionUserEntity::class],
    version = 3
)
@TypeConverters(Converters::class)
abstract class SQLDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun sessionDao(): SessionDao
    abstract fun messageDao(): MessageDao
    abstract fun suDao(): SessionUserDao

    companion object {

        private var instance: SQLDatabase? = null

        @Synchronized
        fun getDatabase(context: Context): SQLDatabase {
            instance?.let {
                return it
            }
            return Room.databaseBuilder(
                context.applicationContext,
                SQLDatabase::class.java, "app_database"
            )
                .fallbackToDestructiveMigration()
                .build().apply {
                    instance = this
                }
        }
    }
}

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }


    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}

