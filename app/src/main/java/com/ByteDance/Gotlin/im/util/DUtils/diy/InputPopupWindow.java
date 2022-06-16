package com.ByteDance.Gotlin.im.util.DUtils.diy;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
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
    private DPopupWindowInputBinding b;

    private OnConfirmListener mOnConfirmListener;

    public interface OnConfirmListener {
        void onConfirm(String inputText);
    }

    public InputPopupWindow(Context context, String title) {
        mContext = context;
        b = DPopupWindowInputBinding.inflate(LayoutInflater.from(context));


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

        b.tvPopTitle.setText(title);
        b.tvSelectCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.etPopInput.clearComposingText();
                popupWindow.dismiss();
            }
        });
    }

    public void setOnConfirmListener(OnConfirmListener listener) {
        this.mOnConfirmListener = listener;
        if (mOnConfirmListener != null)
            b.tvSelectConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(b.etPopInput.getText().length() == 0){
                        TPhoneUtil.INSTANCE.showToast(mContext,"请输入内容");
                    }else{
                        mOnConfirmListener.onConfirm(b.etPopInput.getText().toString());
                        popupWindow.dismiss();
                    }
                }
            });
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
