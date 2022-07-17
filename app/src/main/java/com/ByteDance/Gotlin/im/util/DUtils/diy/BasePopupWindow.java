package com.ByteDance.Gotlin.im.util.DUtils.diy;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import androidx.viewbinding.ViewBinding;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

/**
 * @Author Zhicong Deng
 * @Date 2022/6/15 16:05
 * @Email 1520483847@qq.com
 * @Description https://blog.csdn.net/u011835956/article/details/110958989
 */
abstract class BasePopupWindow extends PopupWindow {

    public WeakReference<Activity> activitySRF = null;


    public PopupWindowListener mListener;

    public abstract void show();

    abstract void setPopupWindowListener();

    abstract void setTitleText(String text);

    abstract void setConfirmText(String text);

    abstract void setCancelText(String text);

    public BasePopupWindow(Activity activity, View root, PopupWindowListener mListener) {
        super(root, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        activitySRF = new WeakReference<Activity>(activity);
        this.mListener = mListener;

        setBackgroundDrawable(new BitmapDrawable());
        setFocusable(true);
        setOutsideTouchable(true);

        setPopupWindowListener();
    }

    public void backgroundAlpha(float f) {//透明函数
        WindowManager.LayoutParams lp = (activitySRF.get()).getWindow().getAttributes();
        lp.alpha = f;
        activitySRF.get().getWindow().setAttributes(lp);
    }

}
