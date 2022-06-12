package com.ByteDance.Gotlin.im.view.custom

import android.view.MenuItem
import androidx.core.view.forEachIndexed
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * @Author 唐靖豪
 * @Date 2022/6/12 15:27
 * @Email 762795632@qq.com
 * @Description
 * 主界面的viewpager2选择器
 */

class MainBnvVp2Mediator(private val bnv: BottomNavigationView,
                         private val vp2: ViewPager2,
                         private val config: ((BottomNavigationView,ViewPager2) -> Unit)? = null
    ) {
    //BottomNavigationView中item与id的对应关系
    private val map = mutableMapOf<MenuItem,Int>()

    init {
        bnv.menu.forEachIndexed { index, item ->
            map[item] = index
        }
    }

    /**
     * 绑定
     */
    fun attach(){
        config?.invoke(bnv,vp2)
        //viewpager页面滑动监听: 动态绑定BottomNavigationView的selectedItemId属性
        vp2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                //绑定position位置的BottomNavigationView菜单Id
                bnv.selectedItemId = bnv.menu.getItem(position).itemId
            }
        })
        //BottomNavigationView的菜单点击事件动态改变viewpager切换的页面
        bnv.setOnItemSelectedListener{item->
            vp2.currentItem = map[item] ?: error("Bnv的item的ID${item.itemId}没有对应的viewpager2的元素")
            true
        }
    }
}