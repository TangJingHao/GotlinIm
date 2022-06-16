package com.ByteDance.Gotlin.im.util.DUtils.diy;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ByteDance.Gotlin.im.R;
import com.ByteDance.Gotlin.im.databinding.DPopupWindowSingleSelectBinding;
import com.ByteDance.Gotlin.im.util.Tutils.TPhoneUtil;

import java.util.List;

/**
 * @Author Zhicong Deng
 * @Date 2022/6/15 15:00
 * @Email 1520483847@qq.com
 * @Description 单选类型弹窗, 目前仅支持双选
 */
public class SingleSelectPopupWindow extends BasePopupWindow {

    private DPopupWindowSingleSelectBinding b;

    private OnConfirmListener mOnConfirmListener;

    private int selectIndex = 0;

    /**
     * 确认回调接口
     */
    public interface OnConfirmListener {
        void onConfirm(int index);
    }

    public SingleSelectPopupWindow(Context context, String title, String options1, String options2) {
        mContext = context;
        b = DPopupWindowSingleSelectBinding.inflate(LayoutInflater.from(context));
        b.tvPopTitle.setText(title);
        b.options1.setText(options1);
        b.options2.setText(options2);

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
        b.rgSelectGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == b.options1.getId()) {
                    selectIndex = 0;
                } else if (i == b.options2.getId()) {
                    selectIndex = 1;
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

    public void setOnConfirmListener(OnConfirmListener listener) {
        this.mOnConfirmListener = listener;
        if (mOnConfirmListener != null)
            b.tvSelectConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnConfirmListener.onConfirm(selectIndex);
                }
            });
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
