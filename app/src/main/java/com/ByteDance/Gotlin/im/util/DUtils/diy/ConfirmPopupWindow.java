package com.ByteDance.Gotlin.im.util.DUtils.diy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.ByteDance.Gotlin.im.R;
import com.ByteDance.Gotlin.im.databinding.DPopupWindowConfirmBinding;
import com.ByteDance.Gotlin.im.util.DUtils.AttrColorUtils;

/**
 * @Author Zhicong Deng
 * @Date 2022/6/15 14:58
 * @Email 1520483847@qq.com
 * @Description 请求确认类型弹窗
 */
public class ConfirmPopupWindow extends BasePopupWindow {
    @SuppressLint("StaticFieldLeak")
    private static DPopupWindowConfirmBinding b;

    public ConfirmPopupWindow(Context context, String title, PopupWindowListener listener) {
        super(
                context,
                new PopupWindow(
                        getBinding(context).getRoot(),
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        true),
                listener
        );

        b.tvConfirmMsg.setText(title);
    }

    public static DPopupWindowConfirmBinding getBinding(Context context) {
        if (b == null) {
            b = DPopupWindowConfirmBinding.inflate(LayoutInflater.from(context));
        }
        return b;
    }

    /**
     * 将确认文字类型改为警告模式
     */
    public void setWarnTextColorType() {
        b.tvSelectConfirm.setTextColor(AttrColorUtils.getValueOfColorAttr(mContext, R.attr.text_error));
        b.tvSelectCancel.setTextColor(AttrColorUtils.getValueOfColorAttr(mContext, R.attr.text_strong));
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
                mListener.onConfirm("CONFIRM");
                mPopupWindow.dismiss();
            });
            b.tvSelectCancel.setOnClickListener(view -> {
                mListener.onCancel();
                mPopupWindow.dismiss();
            });
            mPopupWindow.setOnDismissListener(() -> {
                backgroundAlpha(1f);
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


