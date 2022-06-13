package com.ByteDance.Gotlin.im.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.ByteDance.Gotlin.im.R;
import com.ByteDance.Gotlin.im.adapter.TabWithTitleAdapter;
import com.ByteDance.Gotlin.im.databinding.DActivityFriendApplicationBinding;
import com.ByteDance.Gotlin.im.databinding.DActivityMyGroupBinding;
import com.ByteDance.Gotlin.im.info.TestUser;
import com.ByteDance.Gotlin.im.util.DUtils.DLogUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyGroupActivity extends AppCompatActivity {

    private static final String TAG = "MyGroupActivity";

    private DActivityMyGroupBinding b;

    private TabWithTitleAdapter adapter;
    private List<List<TestUser>> mDateList;
    private String[] mDateTitleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DActivityMyGroupBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        initDate();

        b.myToolbar.title.setText("我的群聊");
        b.myToolbar.imgChevronLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        adapter = new TabWithTitleAdapter(
                MyGroupActivity.this,
                mDateList,
                Arrays.asList(mDateTitleList),
                TabWithTitleAdapter.TYPE_USER_INFO_SIMPLE);
        b.rvLayout.setAdapter(adapter);
        b.rvLayout.setLayoutManager(new LinearLayoutManager(this));
        b.rvLayout.setAdapter(adapter);
        adapter.setMoreOnClickListener(new TabWithTitleAdapter.OnMoreClickListener() {
            @Override
            public void onMoreClick(View v, int groupPosition, int relativePosition) {
                DLogUtils.i(TAG, "do something");
            }
        });
        if (mDateList.size() != 0 && mDateTitleList.length != 0)
            adapter.notifyDataSetChanged();

    }

    private void initDate() {
        mDateTitleList = new String[]{"A", "B"};
        getUserGroupMoreThanOne("");
    }

    public List<List<TestUser>> getUserGroupMoreThanOne(String mail) {
        TestUser u1 = new TestUser("null", "123456789@qq.com", "张三", "状态显示", "无消息");
        TestUser u2 = new TestUser("null", "123456789@qq.com", "李四", "状态显示", "无消息");
        TestUser u3 = new TestUser("null", "123456789@qq.com", "王五", "状态显示", "无消息");
        TestUser u4 = new TestUser("null", "123456789@qq.com", "赵六", "状态显示", "无消息");
        ArrayList<TestUser> G1 = new ArrayList<>();
        G1.add(u1);
        G1.add(u2);
        G1.add(u3);
        G1.add(u4);
        TestUser u5 = new TestUser("null", "123456789@qq.com", "小福贵", "厨子", "做饭");
        TestUser u6 = new TestUser("null", "123456789@qq.com", "小飞碟", "格格", "刺杀");
        TestUser u7 = new TestUser("null", "123456789@qq.com", "小李子", "公公", "捣乱");
        ArrayList<TestUser> G2 = new ArrayList<>();
        G2.add(u5);
        G2.add(u6);
        G2.add(u7);
        mDateList = new ArrayList<>();
        mDateList.add(G1);
        mDateList.add(G2);
        return mDateList;
    }
}