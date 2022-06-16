package com.ByteDance.Gotlin.im.util.DUtils.diy;

import android.app.Activity;
import android.content.Context;
import android.view.WindowManager;
import android.widget.PopupWindow;

/**
 * @Author Zhicong Deng
 * @Date 2022/6/15 16:05
 * @Email 1520483847@qq.com
 * @Description https://blog.csdn.net/u011835956/article/details/110958989
 */
abstract class BasePopupWindow extends PopupWindow {

    public Context mContext;

    public PopupWindow popupWindow;

    abstract void show();

    abstract void setConfirmText(String text);

    abstract void setCancelText(String text);


    public void backgroundAlpha(float f) {//透明函数
        WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
        lp.alpha = f;
        ((Activity) mContext).getWindow().setAttributes(lp);
    }

}
