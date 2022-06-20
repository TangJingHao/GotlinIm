package com.ByteDance.Gotlin.im.datasource.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ByteDance.Gotlin.im.datasource.dao.MessageDao
import com.ByteDance.Gotlin.im.datasource.dao.SessionDao
import com.ByteDance.Gotlin.im.datasource.dao.UserDao
import com.ByteDance.Gotlin.im.entity.Converters
import com.ByteDance.Gotlin.im.entity.MessageEntity
import com.ByteDance.Gotlin.im.entity.SessionEntity
import com.ByteDance.Gotlin.im.entity.UserEntity

/**
 * @Author Zhicong Deng
 * @Date  2022/6/14 22:07
 * @Email 1520483847@qq.com
 * @Description 学习测试用数据库
 */
@Database(entities = [UserEntity::class, SessionEntity::class, MessageEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class SQLDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun sessionDao(): SessionDao
    abstract fun messageDao(): MessageDao

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
