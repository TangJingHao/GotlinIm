package com.ByteDance.Gotlin.im.view.custom

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * @Author 唐靖豪
 * @Date 2022/6/12 15:29
 * @Email 762795632@qq.com
 * @Description
 * 主界面的viewpager2适配器
 */

class MainViewPagerAdapter(
    fragmentActivity: FragmentActivity,
    private val fragments: Map<Int, Fragment>
) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment =
        fragments[position] ?: error("请确保fragments数据源和viewpager2的index匹配设置")
}