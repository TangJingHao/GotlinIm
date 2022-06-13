package com.ByteDance.Gotlin.im.util.Mutils

import android.content.Context
import android.content.Intent

/**
 * @Description：运用泛型实化优化Intent相关操作
 * @Author：Suzy.Mo
 * @Date：2022/6/11 22:27
 */

/**
 * @description：不带数据的Activity启动函数
 * @param:context
 */
inline fun<reified T> startActivity(context: Context){
    val intent = Intent(context,T::class.java)
    context.startActivity(intent)
}

/**
 * @description：带数据的Activity启动函数
 * usage：
 * startActivity<TestActivity>(ApplicationUtil.context){
 *      putExtra("param1","data")
 *      putExtra("param2",123)
 * }
 * @param:context
 * @param:block
 */
inline fun<reified T> startActivity(context: Context, block:Intent.()->Unit){
    val intent = Intent(context,T::class.java)
    intent.block()
    context.startActivity(intent)

}