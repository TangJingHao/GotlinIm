package com.ByteDance.Gotlin.im.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ByteDance.Gotlin.im.databinding.TFragmentAddressBookBinding

/**
 * @Author 唐靖豪
 * @Date 2022/6/12 14:34
 * @Email 762795632@qq.com
 * @Description
 * 通讯录
 */

class AddressBookFragment:Fragment(){
    private lateinit var mBinding:TFragmentAddressBookBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding=TFragmentAddressBookBinding.inflate(inflater,container,false)
        return mBinding.root
    }

    override fun onStart() {
        super.onStart()

    }
}