package com.ByteDance.Gotlin.im.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ByteDance.Gotlin.im.databinding.TFragmentMyInfomationBinding

/**
 * @Author 唐靖豪
 * @Date 2022/6/12 14:25
 * @Email 762795632@qq.com
 * @Description
 * 我的
 */

class MyInformationFragment :Fragment() {
    private lateinit var mBinding:TFragmentMyInfomationBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding=TFragmentMyInfomationBinding.inflate(inflater,container,false)
        return mBinding.root
    }
}