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


                /* 小红点使用Demo=============================================================================
         文章可查阅 https://mp.weixin.qq.com/s/rdAjBQ2DRCjiEKLc6EoChw
         */
        // 创建小红点BadgeDrawable
//        BadgeDrawable redPoint = RedPointHelper.getRedPointBadge(TestActivity.this, 0);

        // 添加到界面---------------------------------------------------------------------------------
//        b.itemSearch.imgUserPic.getViewTreeObserver().addOnGlobalLayoutListener(
//                new ViewTreeObserver.OnGlobalLayoutListener() {
//                    @SuppressLint("UnsafeOptInUsageError")
//                    @Override
//                    public void onGlobalLayout() {
//                        // 参数为：小红点，需要添加小红点的对象，被添加对象的父控件（一般是Fragment，用于限制大小）
//                        BadgeUtils.attachBadgeDrawable(redPoint, b.itemSearch.imgUserPic, b.itemSearch.flImg);
//                        b.itemSearch.imgUserPic.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                    }
//                });
//
//        // 更改小红点的数值----------------------------------------------------------------------------
//        b.itemSearch.rLayout.setOnClickListener(new View.OnClickListener() {
//            int i = 1;
//
//            @Override
//            public void onClick(View view) {
//                redPoint.setNumber(i++);
//            }
//        });
    }
}
