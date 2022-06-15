package com.ByteDance.Gotlin.im.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.room.Room;

import com.ByteDance.Gotlin.im.Repository;
import com.ByteDance.Gotlin.im.application.BaseApp;
import com.ByteDance.Gotlin.im.databinding.DActivityTestBinding;
import com.ByteDance.Gotlin.im.datasource.database.SQLDatabase;
import com.ByteDance.Gotlin.im.entity.Book;
import com.ByteDance.Gotlin.im.info.FriendListDataResponse;
import com.ByteDance.Gotlin.im.info.GroupListDataResponse;
import com.ByteDance.Gotlin.im.info.LoginDataResponse;
import com.ByteDance.Gotlin.im.info.SessionHistoryDataResponse;
import com.ByteDance.Gotlin.im.info.SessionListDataResponse;
import com.ByteDance.Gotlin.im.info.VO.TestUser;
import com.ByteDance.Gotlin.im.util.DUtils.DLogUtils;

import java.util.ArrayList;
import java.util.List;

import kotlin.Result;

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

    LiveData<Result<FriendListDataResponse>> friendList;
    LiveData<Result<GroupListDataResponse>> groupList;
    LiveData<Result<SessionListDataResponse>> sessionList;
    LiveData<Result<SessionHistoryDataResponse>> sessionHistoryList;

    @SuppressLint("UnsafeOptInUsageError")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DActivityTestBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        b.testBar.imgChevronLeft.setVisibility(View.GONE);
        b.testBar.title.setText("混乱的测试页面");

        Repository repository = Repository.INSTANCE;

        new Thread(new Runnable() {
            @Override
            public void run() {

                SQLDatabase db = Room.databaseBuilder(TestActivity.this, SQLDatabase.class, "IMDB").build();

//                db.book().insert(new Book(1,"西游记"));
//                db.book().insert(new Book(2,"水浒传"));
//                db.book().insert(new Book(3,"三国演义"));
//                db.book().insert(new Book(4,"红楼梦"));

                List<Book> books = db.book().qeuryAll();

                DLogUtils.i(TAG, books.get(0).getTitle());
                DLogUtils.i(TAG, books.get(1).getTitle());
                DLogUtils.i(TAG, books.get(2).getTitle());
                DLogUtils.i(TAG, books.get(3).getTitle());
            }
        }).start();




////
//        friendList = repository.getFriendList(1);
//        groupList = repository.getGroupList(1);
//        sessionList = repository.getSessionList(1);
//        sessionHistoryList = repository.getSessionHistoryList(1, 3, 0);
//
//
//        friendList.observe(this, new Observer<Result<FriendListDataResponse>>() {
//            @Override
//            public void onChanged(Result<FriendListDataResponse> result) {
//
//            }
//        });
//        groupList.observe(this, new Observer<Result<GroupListDataResponse>>() {
//            @Override
//            public void onChanged(Result<GroupListDataResponse> groupListDataResponseResult) {
//
//            }
//        });
//        sessionList.observe(this, new Observer<Result<SessionListDataResponse>>() {
//            @Override
//            public void onChanged(Result<SessionListDataResponse> sessionListDataResponseResult) {
//
//            }
//        });
//        sessionHistoryList.observe(this, new Observer<Result<SessionHistoryDataResponse>>() {
//            @Override
//            public void onChanged(Result<SessionHistoryDataResponse> sessionHistoryDataResponseResult) {
//
//            }
//        });
//
//
        b.btnAdd0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                friendList = repository.getFriendList(1);
//                Result<GroupListDataResponse> groupListDataResponseResult = repository.getGroupList(userId).getValue();
//                Result<SessionListDataResponse> sessionListDataResponseResult = repository.getSessionList(userId).getValue();
//                Result<SessionHistoryDataResponse> sessionHistoryDataResponseResult = repository.getSessionHistoryList(userId, 6, 0).getValue();
            }
        });

//        initViewAndEvent();

        //  测试用title
        ArrayList<String> testLittleTitle = new ArrayList<>();
        testLittleTitle.add("小红点测试");

    }

    private void initViewAndEvent() {
        //        List<TestUser> userMsgList = getOneUserGroupWithoutTitle();
//        UserMsgAdapter adapter = new UserMsgAdapter(TestActivity.this, userMsgList);
//        b.testRvLayout.setAdapter(adapter);
//        b.testRvLayout.setLayoutManager(new LinearLayoutManager(TestActivity.this));
//        adapter.notifyDataSetChanged();

        b.btnAdd0.setOnClickListener(new View.OnClickListener() {
            int i = 0;

            @Override
            public void onClick(View view) {
                b.itemMsg0.tvRedPoint.setText(String.valueOf(i++));
                if (i > 0)
                    b.itemMsg0.tvRedPoint.setVisibility(View.VISIBLE);
            }
        });

        b.btnAdd1.setOnClickListener(new View.OnClickListener() {
            int i = 0;

            @Override
            public void onClick(View view) {
                b.itemMsg1.tvRedPoint.setText(String.valueOf(i++));
                if (i > 0)
                    b.itemMsg1.tvRedPoint.setVisibility(View.VISIBLE);
            }
        });

        b.btnAdd2.setOnClickListener(new View.OnClickListener() {
            int i = 0;

            @Override
            public void onClick(View view) {
                b.itemMsg2.tvRedPoint.setText(String.valueOf(i++));
                if (i > 0)
                    b.itemMsg2.tvRedPoint.setVisibility(View.VISIBLE);
            }
        });

        b.btnClean.setOnClickListener(new View.OnClickListener() {
            int i = 0;

            @Override
            public void onClick(View view) {
                b.itemMsg0.tvRedPoint.setText(i++);
                if (i > 0)
                    b.itemMsg0.tvRedPoint.setVisibility(View.VISIBLE);
            }
        });


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

        b.btnFriendApplication.setOnClickListener(view ->
                startActivity(new Intent(TestActivity.this, FriendApplicationActivity.class)));

        b.btnMyGroup.setOnClickListener(view ->
                startActivity(new Intent(TestActivity.this, MyGroupActivity.class)));

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

    public List<TestUser> getOneUserGroupWithoutTitle() {
        List<List<TestUser>> dataList;
        TestUser u5 = new TestUser("null", "123456789@qq.com", "小福贵", "厨子", "做饭");
        TestUser u6 = new TestUser("null", "123456789@qq.com", "小飞碟", "格格", "刺杀");
        TestUser u7 = new TestUser("null", "123456789@qq.com", "小李子", "公公", "捣乱");
        ArrayList<TestUser> G2 = new ArrayList<>();
        G2.add(u5);
        G2.add(u6);
        G2.add(u7);
        return G2;
    }
}
