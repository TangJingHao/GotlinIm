package com.ByteDance.Gotlin.im.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @Author Zhicong Deng
 * @Date 2022/6/14 22:08
 * @Email 1520483847@qq.com
 * @Description 学习测试用数据类
 */
@Entity
data class Book(
    @PrimaryKey(autoGenerate = true)
    var number: Long = 0,
    var title: String
)
