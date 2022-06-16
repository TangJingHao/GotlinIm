package com.ByteDance.Gotlin.im.util.DUtils.diy;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
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
    private DPopupWindowConfirmBinding b;

    private OnConfirmListener mOnConfirmListener;

    /**
     * 确认回调接口
     */
    public interface OnConfirmListener {
        void onConfirm();
    }

    public ConfirmPopupWindow(Context context, String title) {
        mContext = context;
        b = DPopupWindowConfirmBinding.inflate(LayoutInflater.from(context));
        b.tvConfirmMsg.setText(title);

        popupWindow = new PopupWindow(b.getRoot(),
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
    }

    public void setOnConfirmListener(OnConfirmListener listener) {
        this.mOnConfirmListener = listener;
        if (mOnConfirmListener != null)
            b.tvSelectConfirm.setOnClickListener(view ->
                    mOnConfirmListener.onConfirm());
    }

    public void setWarnTextColorType() {
        b.tvSelectConfirm.setTextColor(AttrColorUtils.getValueOfColorAttr(mContext, R.attr.text_error));
        b.tvSelectCancel.setTextColor(AttrColorUtils.getValueOfColorAttr(mContext, R.attr.text_strong));
    }

    @Override
    public void show() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        } else {
            backgroundAlpha(0.8f);
            popupWindow.showAtLocation(b.getRoot(), Gravity.CENTER, 0, 0);
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


