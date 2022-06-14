package com.ByteDance.Gotlin.im.util.DUtils;

import android.content.Context;

import com.ByteDance.Gotlin.im.R;
import com.google.android.material.badge.BadgeDrawable;

/**
 * @Author Zhicong Deng
 * @Date 2022/6/14 14:44
 * @Email 1520483847@qq.com
 * @Description 小红点相关辅助类
 */
public class RedPointHelper {

    /**
     * 生成默认小红点BadgeDrawable
     */
    public static BadgeDrawable getRedPointBadge(Context context, int num) {
        BadgeDrawable redPoint = BadgeDrawable.create(context);
        // 定位
        redPoint.setBadgeGravity(BadgeDrawable.TOP_END);
        redPoint.setHorizontalOffset(16);
        redPoint.setVerticalOffset(10);
        // 设置数字
        if (num > 0)
            redPoint.setNumber(num);
        // badge最多显示字符，默认99+ 是3个字符（带'+'号）
        redPoint.setMaxCharacterCount(3);
        // 背景颜色
        redPoint.setBackgroundColor(AttrColorUtils
                .getValueOfColorAttr(context, R.attr.text_error));
        // 设置可视
        redPoint.setVisible(true);
        return redPoint;
    }
}
