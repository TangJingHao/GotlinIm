package com.ByteDance.Gotlin.im.util.DUtils.diy;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.ByteDance.Gotlin.im.databinding.DPopupWindowSingleSelectBinding;

/**
 * @Author Zhicong Deng
 * @Date 2022/6/15 15:00
 * @Email 1520483847@qq.com
 * @Description 单选类型弹窗, 目前仅支持双选
 */
public class SingleSelectPopupWindow extends BasePopupWindow {
    public int getSelectIndex() {
        return selectIndex;
    }

    public void setSelectIndex(int selectIndex) {
        this.selectIndex = selectIndex;
    }

    @SuppressLint("StaticFieldLeak")
    private static DPopupWindowSingleSelectBinding b;

    private int selectIndex = 0;

    String[] options = new String[]{"选项0", "选项1"};

    public SingleSelectPopupWindow(Activity activity, String title, String option0, String option1,
                                   PopupWindowListener listener) {
        super(activity, getBinding(activity).getRoot(), listener);

        options[0] = option0;
        options[1] = option1;

        setTitleText(title);
        b.options1.setText(options[0]);
        b.options2.setText(options[1]);

        b.rgSelectGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            if (i == b.options1.getId()) {
                selectIndex = 0;
            } else if (i == b.options2.getId()) {
                selectIndex = 1;
            }
        });
    }

    public static DPopupWindowSingleSelectBinding getBinding(Activity activity) {
        if (b == null) {
            b = DPopupWindowSingleSelectBinding.inflate(LayoutInflater.from(activity));
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

    @Override
    void setPopupWindowListener() {
        if (mListener != null) {
            b.tvSelectConfirm.setOnClickListener(view -> {
                mListener.onConfirm(options[selectIndex]);
                dismiss();
            });
            b.tvSelectCancel.setOnClickListener(view -> {
                mListener.onCancel();
                dismiss();
                dismiss();
            });
            setOnDismissListener(() -> {
                backgroundAlpha(1f);
                mListener.onDismiss();
                dismiss();
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

    public void setOptions(int index) {
        switch (index % 2) {
            case 0: {
                b.rgSelectGroup.check(b.options1.getId());
            }
            case 1: {
                b.rgSelectGroup.check(b.options2.getId());
            }
        }
    }
}
