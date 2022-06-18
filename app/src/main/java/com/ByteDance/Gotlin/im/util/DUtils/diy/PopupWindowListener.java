package com.ByteDance.Gotlin.im.util.DUtils.diy;

/**
 * @Author Zhicong Deng
 * @Date 2022/6/19 0:27
 * @Email 1520483847@qq.com
 * @Description
 */
public interface PopupWindowListener {
    /**
     * @param input 输入或选择的选项
     */
    void onConfirm(String input);

    void onCancel();

    void onDismiss();
}
