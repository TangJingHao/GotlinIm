package com.ByteDance.Gotlin.im.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.ByteDance.Gotlin.im.R;
import com.ByteDance.Gotlin.im.adapter.TabWithTitleAdapter;
import com.ByteDance.Gotlin.im.databinding.DActivityFriendApplicationBinding;
import com.ByteDance.Gotlin.im.info.TestUser;
import com.ByteDance.Gotlin.im.util.DUtils.DLogUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author Zhicong Deng
 * @Date 2022/6/13 15:51
 * @Email 1520483847@qq.com
 * @Description 好友申请界面
 */
public class FriendApplicationActivity extends AppCompatActivity {

    private static final String TAG = "FriendApplicationActivity";

    private DActivityFriendApplicationBinding b;
    private TabWithTitleAdapter adapter;
    private List<List<TestUser>> mDateList;
    private String[] mDateTitleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DActivityFriendApplicationBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        initDate();
        int num = mDateList.size();
        b.myToolbar.title.setText("好友申请（" + num + "）");
        b.myToolbar.imgChevronLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        adapter = new TabWithTitleAdapter(
                FriendApplicationActivity.this,
                mDateList,
                Arrays.asList(mDateTitleList),
                TabWithTitleAdapter.TYPE_USER_INFO_STATUE);
        b.rvLayout.setAdapter(adapter);
        b.rvLayout.setLayoutManager(new LinearLayoutManager(this));
        b.rvLayout.setAdapter(adapter);
        adapter.setMoreOnClickListener(new TabWithTitleAdapter.OnMoreClickListener() {
            @Override
            public void onMoreClick(View v, int groupPosition, int relativePosition) {
                DLogUtils.i(TAG,"do something");
            }
        });
        if (mDateList.size() != 0 && mDateTitleList.length != 0)
            adapter.notifyDataSetChanged();
    }

    private void initDate() {
        mDateTitleList = getResources().getStringArray(R.array.rvLayout_friend_application_little_title);
        getOneUserGroup("");
    }

    public List<List<TestUser>> getOneUserGroup(String mail) {
        TestUser u5 = new TestUser("null", "123456789@qq.com", "小福贵", "厨子", "做饭");
        TestUser u6 = new TestUser("null", "123456789@qq.com", "小飞碟", "格格", "刺杀");
        TestUser u7 = new TestUser("null", "123456789@qq.com", "小李子", "公公", "捣乱");
        ArrayList<TestUser> G2 = new ArrayList<>();
        G2.add(u5);
        G2.add(u6);
        G2.add(u7);
        mDateList = new ArrayList<>();
        mDateList.add(G2);
        return mDateList;
    }


}