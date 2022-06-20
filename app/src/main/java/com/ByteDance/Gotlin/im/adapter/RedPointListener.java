package com.ByteDance.Gotlin.im.adapter;

import android.view.View;

import cn.bingoogolapple.badgeview.BGABadgeView;
import cn.bingoogolapple.badgeview.BGABadgeable;

/**
 * @Author Zhicong Deng
 * @Date 2022/6/20 17:09
 * @Email 1520483847@qq.com
 * @Description 拖动小红点时的接口
 */
public interface RedPointListener {
    void onDragDismiss(BGABadgeable badgeable, int position);

    void onClick(View view, int position, BGABadgeView badge);
}
