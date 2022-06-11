package com.ByteDance.Gotlin.im.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ByteDance.Gotlin.im.R;
import com.ByteDance.Gotlin.im.databinding.DActivityTestBinding;
import com.ByteDance.Gotlin.im.util.DUtils.DLogUtils;
import com.ByteDance.Gotlin.im.util.Tutils.TLogUtil;

/**
 * by邓志聪
 * 用于测试Activity启动
 */
public class TestActivity extends AppCompatActivity {

    private DActivityTestBinding b;
    private static final String TAG = "TestActivity";

    private static final String SEARCH_TYPE = "search_type";
    private static final int SEARCH_TYPE_FRIEND = 0;
    private static final int SEARCH_TYPE_GROUP_CHAT = 1;
    private static final int SEARCH_TYPE_MESSAGE = 2;

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
            DLogUtils.e(TAG,"btnNewFriend");
            startActivity(intent);

        });

        b.btnNewGroupChat.setOnClickListener(view -> {
            intent.putExtra(SEARCH_TYPE, SEARCH_TYPE_GROUP_CHAT);
            DLogUtils.e(TAG,"btnNewGroupChat");
            startActivity(intent);
        });

        b.btnHistoryMessage.setOnClickListener(view -> {
            intent.putExtra(SEARCH_TYPE, SEARCH_TYPE_MESSAGE);
            DLogUtils.e(TAG,"btnHistoryMessage");
            startActivity(intent);
        });


    }
}