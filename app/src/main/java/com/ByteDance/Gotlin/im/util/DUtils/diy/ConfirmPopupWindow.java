package com.ByteDance.Gotlin.im.util.DUtils.diy;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import androidx.annotation.RequiresApi;

import com.ByteDance.Gotlin.im.R;
import com.ByteDance.Gotlin.im.application.BaseApp;
import com.ByteDance.Gotlin.im.databinding.DPopupWindowConfirmBinding;
import com.ByteDance.Gotlin.im.util.DUtils.AttrColorUtils;
import com.ByteDance.Gotlin.im.util.DUtils.DLogUtils;

/**
 * @Author Zhicong Deng
 * @Date 2022/6/15 14:58
 * @Email 1520483847@qq.com
 * @Description 请求确认类型弹窗
 */
public class ConfirmPopupWindow extends BasePopupWindow {
    @SuppressLint("StaticFieldLeak")
    private static DPopupWindowConfirmBinding b;
    WindowManager wm;
    WindowManager.LayoutParams para;


    public ConfirmPopupWindow(Activity activity, String title, PopupWindowListener listener) {
        super(activity, getBinding(activity).getRoot(), listener);
        setTitleText(title);
        wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        para = new WindowManager.LayoutParams();
    }

    public static DPopupWindowConfirmBinding getBinding(Activity activity) {
        if (b == null) {
            b = DPopupWindowConfirmBinding.inflate(LayoutInflater.from(activity));
        }
        return b;
    }

    /**
     * 将确认文字类型改为警告模式
     */
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void setWarnTextColorType() {
        b.tvSelectConfirm.setTextColor(AttrColorUtils.getValueOfColorAttr(activitySRF.get(), R.attr.text_error));
        b.tvSelectCancel.setTextColor(AttrColorUtils.getValueOfColorAttr(activitySRF.get(), R.attr.text_strong));
    }

    @Override
    public void show() {
        if (activitySRF != null && activitySRF.get() != null && !activitySRF.get().isFinishing()) {
            if (isShowing()) {
                dismiss();
            } else {
                backgroundAlpha(0.8f);
                showAtLocation(b.getRoot(), Gravity.CENTER, 0, 0);
            }
        } else {
            new Handler(activitySRF.get().getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (null != activitySRF && null != activitySRF.get() && !activitySRF.get().isFinishing()) {
                        dismiss();
                    }
                }
            }, 3000);
        }
    }

    @Override
    void setPopupWindowListener() {
        if (mListener != null) {
            b.tvSelectConfirm.setOnClickListener(view -> {
                mListener.onConfirm("CONFIRM");
                dismiss();
            });
            b.tvSelectCancel.setOnClickListener(view -> {
                mListener.onCancel();
                dismiss();
            });
            setOnDismissListener(() -> {
                backgroundAlpha(1f);
                mListener.onDismiss();
            });
        }
    }

    @Override
    void setTitleText(String text) {
        b.tvConfirmMsg.setText(text);
    }


    @Override
    public void setConfirmText(String confirmText) {
        b.tvSelectConfirm.setText(confirmText);
    }

    @Override
    public void setCancelText(String cancelText) {
        b.tvSelectCancel.setText(cancelText);
    }


}


