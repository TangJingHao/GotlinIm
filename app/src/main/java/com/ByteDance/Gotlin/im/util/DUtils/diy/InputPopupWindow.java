package com.ByteDance.Gotlin.im.util.DUtils.diy;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import androidx.annotation.RequiresApi;

import com.ByteDance.Gotlin.im.application.BaseApp;
import com.ByteDance.Gotlin.im.databinding.DPopupWindowInputBinding;
import com.ByteDance.Gotlin.im.util.Tutils.TPhoneUtil;

import java.util.Objects;

/**
 * @Author Zhicong Deng
 * @Date 2022/6/15 14:59
 * @Email 1520483847@qq.com
 * @Description 输入框类型弹窗
 */
public class InputPopupWindow extends BasePopupWindow {
    @SuppressLint("StaticFieldLeak")
    private static DPopupWindowInputBinding b;

    public InputPopupWindow(Activity activity, String title, PopupWindowListener listener) {
        super(activity, getBinding(activity).getRoot(), listener);
        setTitleText(title);
    }

    public static DPopupWindowInputBinding getBinding(Activity activity) {
        if (b == null) {
            b = DPopupWindowInputBinding.inflate(LayoutInflater.from(activity));
        }
        return b;
    }

    @Override
    public void show() {
        if (isShowing()) {
            dismiss();
        } else {
            backgroundAlpha(0.8f);
            showAtLocation(b.getRoot(), Gravity.CENTER, 0, 0);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    void setPopupWindowListener() {
        if (mListener != null) {
            b.tvSelectConfirm.setOnClickListener(view -> {
                if (b.etPopInput.getText().length() == 0) {
                    TPhoneUtil.INSTANCE.showToast(Objects.requireNonNull(activitySRF.get()), "请输入内容");
                } else {
                    mListener.onConfirm(b.etPopInput.getText().toString());
                    dismiss();
                }
            });
            b.tvSelectCancel.setOnClickListener(view -> {
                mListener.onCancel();
                dismiss();
            });
            setOnDismissListener(() -> {
                backgroundAlpha(1f);
                b.etPopInput.clearComposingText();
                mListener.onDismiss();
            });
        }
    }

    @Override
    void setTitleText(String text) {
        b.tvPopTitle.setText(text);
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
