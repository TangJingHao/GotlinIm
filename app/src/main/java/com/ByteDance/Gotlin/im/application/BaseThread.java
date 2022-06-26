package com.ByteDance.Gotlin.im.application;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.ByteDance.Gotlin.im.util.Constants;
import com.ByteDance.Gotlin.im.util.Tutils.TPhoneUtil;

/**
 * @Author 唐靖豪
 * @Date 2022/6/25 16:39
 * @Email 762795632@qq.com
 * @Description
 */
@RequiresApi(api = Build.VERSION_CODES.Q)
public class BaseThread implements Runnable{
    private int mode= -1;
    @Override
    public void run() {
        int phoneMode = TPhoneUtil.INSTANCE.getPhoneMode(BaseApp.Companion.getContext());
        if(mode==-1){
            mode=phoneMode;
        }else if(mode!=phoneMode){

        }
    }
}
