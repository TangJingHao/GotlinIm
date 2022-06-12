package com.ByteDance.Gotlin.im.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ByteDance.Gotlin.im.databinding.TFragmentMessageBinding

/**
 * @Author 唐靖豪
 * @Date 2022/6/12 14:33
 * @Email 762795632@qq.com
 * @Description
 * 消息
 */

class MessageFragment:Fragment() {
    private lateinit var mBinding:TFragmentMessageBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding=TFragmentMessageBinding.inflate(inflater,container,false)
        return mBinding.root
    }
}