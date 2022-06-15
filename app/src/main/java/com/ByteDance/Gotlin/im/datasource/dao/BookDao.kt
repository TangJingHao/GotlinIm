package com.ByteDance.Gotlin.im.datasource.dao

import androidx.room.*
import com.ByteDance.Gotlin.im.entity.Book

/**
 * @Author Zhicong Deng
 * @Date  2022/6/14 22:07
 * @Email 1520483847@qq.com
 * @Description 学习测试用操作接口
 */
@Dao
interface BookDao {

    @Query("select * from Book")
    fun qeuryAll(): List<Book>

    @Insert
    fun insert(vararg book: Book): List<Long>

    @Delete
    fun delete(book: Book): Int

    @Update
    fun update(book: Book): Int

}
