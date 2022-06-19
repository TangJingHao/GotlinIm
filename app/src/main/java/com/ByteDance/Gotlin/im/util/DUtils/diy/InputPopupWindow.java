package com.ByteDance.Gotlin.im.util.DUtils.diy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.ByteDance.Gotlin.im.databinding.DPopupWindowInputBinding;
import com.ByteDance.Gotlin.im.util.Tutils.TPhoneUtil;

/**
 * @Author Zhicong Deng
 * @Date 2022/6/15 14:59
 * @Email 1520483847@qq.com
 * @Description 输入框类型弹窗
 */
public class InputPopupWindow extends BasePopupWindow {
    @SuppressLint("StaticFieldLeak")
    private static DPopupWindowInputBinding b;

    public InputPopupWindow(Context context, String title, PopupWindowListener listener) {
        super(
                context,
                new PopupWindow(
                        getBinding(context).getRoot(),
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT, true),
                listener);

        b.tvPopTitle.setText(title);
    }

    public static DPopupWindowInputBinding getBinding(Context context) {
        if (b == null) {
            b = DPopupWindowInputBinding.inflate(LayoutInflater.from(context));
        }
        return b;
    }

    @Override
    public void show() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        } else {
            backgroundAlpha(0.8f);
            mPopupWindow.showAtLocation(b.getRoot(), Gravity.CENTER, 0, 0);
        }
    }

    @Override
    void setPopupWindowListener() {
        if (mListener != null) {
            b.tvSelectConfirm.setOnClickListener(view -> {
                if (b.etPopInput.getText().length() == 0) {
                    TPhoneUtil.INSTANCE.showToast(mContext, "请输入内容");
                } else {
                    mListener.onConfirm(b.etPopInput.getText().toString());
                    mPopupWindow.dismiss();
                }
            });
            b.tvSelectCancel.setOnClickListener(view -> {
                mListener.onCancel();
                mPopupWindow.dismiss();
            });
            mPopupWindow.setOnDismissListener(() -> {
                backgroundAlpha(1f);
                b.etPopInput.clearComposingText();
                mListener.onDismiss();
            });
        }
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
