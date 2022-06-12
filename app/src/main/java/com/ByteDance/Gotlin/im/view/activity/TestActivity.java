package com.ByteDance.Gotlin.im.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.appcompat.app.AppCompatActivity;

import com.ByteDance.Gotlin.im.R;
import com.ByteDance.Gotlin.im.databinding.DActivityTestBinding;
import com.ByteDance.Gotlin.im.util.DUtils.AttrColorUtils;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.badge.BadgeUtils;

/**
 * @Author Zhicong Deng
 * @Date 2022/6/12 14:13
 * @Email 1520483847@qq.com
 * @Description 测试用Activity
 */
public class TestActivity extends AppCompatActivity {

    private DActivityTestBinding b;
    private static final String TAG = "TestActivity";

    private static final String SEARCH_TYPE = "search_type";
    private static final int SEARCH_TYPE_FRIEND = 0;
    private static final int SEARCH_TYPE_GROUP_CHAT = 1;
    private static final int SEARCH_TYPE_MESSAGE = 2;

    @SuppressLint("UnsafeOptInUsageError")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DActivityTestBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        b.testBar.imgChevronLeft.setVisibility(View.GONE);
        b.testBar.title.setText("测试页面");

        Intent intent = new Intent(TestActivity.this, SearchActivity.class);

        b.btnNewFriend.setOnClickListener(view -> {
            intent.putExtra(SEARCH_TYPE, SEARCH_TYPE_FRIEND);
            startActivity(intent);

        });

        b.btnNewGroupChat.setOnClickListener(view -> {
            intent.putExtra(SEARCH_TYPE, SEARCH_TYPE_GROUP_CHAT);
            startActivity(intent);
        });

        b.btnHistoryMessage.setOnClickListener(view -> {
            intent.putExtra(SEARCH_TYPE, SEARCH_TYPE_MESSAGE);
            startActivity(intent);
        });

        // 小红点使用Demo=============================================================================
        // 文章可查阅 https://mp.weixin.qq.com/s/rdAjBQ2DRCjiEKLc6EoChw
        // 创建小红点BadgeDrawable
        BadgeDrawable redPoint = BadgeDrawable.create(TestActivity.this);
        // 为小红点设置参数----------------------------------------------------------------------------
        // 定位
        redPoint.setBadgeGravity(BadgeDrawable.TOP_END);
        redPoint.setHorizontalOffset(16);
        redPoint.setVerticalOffset(10);
        // 设置数字
        redPoint.setNumber(9999);
        // badge最多显示字符，默认99+ 是3个字符（带'+'号）
        redPoint.setMaxCharacterCount(3);
        // 背景颜色
        redPoint.setBackgroundColor(AttrColorUtils
                .getValueOfColorAttr(TestActivity.this, R.attr.text_error));
        // 设置可视
        redPoint.setVisible(true);

        // 添加到界面---------------------------------------------------------------------------------
        b.itemSearch.imgUserPic.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        // 参数为：小红点，需要添加小红点的对象，被添加对象的父控件（一般是Fragment，用于限制大小）
                        BadgeUtils.attachBadgeDrawable(redPoint, b.itemSearch.imgUserPic, b.itemSearch.flImg);
                        b.itemSearch.imgUserPic.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });

        // 更改小红点的数值----------------------------------------------------------------------------
        b.itemSearch.rLayout.setOnClickListener(new View.OnClickListener() {
            int i = 1;
            @Override
            public void onClick(View view) {
                redPoint.setNumber(i++);
            }
        });

    }
}
