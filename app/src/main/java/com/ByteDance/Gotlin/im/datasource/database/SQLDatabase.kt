package com.ByteDance.Gotlin.im.datasource.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ByteDance.Gotlin.im.datasource.dao.BookDao
import com.ByteDance.Gotlin.im.entity.Book

/**
 * @Author Zhicong Deng
 * @Date  2022/6/14 22:07
 * @Email 1520483847@qq.com
 * @Description 学习测试用数据库
 */
@Database(entities = [Book::class], version = 1)
abstract class SQLDatabase : RoomDatabase() {
    abstract fun book(): BookDao
}
