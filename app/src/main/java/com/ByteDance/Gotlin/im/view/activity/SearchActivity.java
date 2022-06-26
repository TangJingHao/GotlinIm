package com.ByteDance.Gotlin.im.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.ByteDance.Gotlin.im.R;
import com.ByteDance.Gotlin.im.databinding.DActivitySearchBinding;
import com.ByteDance.Gotlin.im.util.DUtils.AttrColorUtils;
import com.ByteDance.Gotlin.im.view.fragment.SearchFragment;
import com.ByteDance.Gotlin.im.view.fragment.SearchHistoryMsgFragment;

import net.lucode.hackware.magicindicator.FragmentContainerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ClipPagerTitleView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zhicong Deng
 * on 2022/6/11 11:17
 * https://github.com/LumosDZC
 * <p>
 * 从查找新好友/查找新群聊/消息搜索跳转来，从Intent中获取类别参数判断
 */
public class SearchActivity extends AppCompatActivity {

    private DActivitySearchBinding b;

    private final List<Fragment> mFragments = new ArrayList<>();
    private final FragmentContainerHelper mFragmentContainerHelper = new FragmentContainerHelper();

    private String[] mTitles;
    // 消息搜索用
    private int sessionId;

    // 搜索类型（好友群聊）intent用
    private static final String SEARCH_TYPE = "search_type";
    private static final String SESSION_ID = "session_id";
    private static final int SEARCH_TYPE_FRIEND = 0;
    private static final int SEARCH_TYPE_GROUP_CHAT = 1;
    private static final int SEARCH_TYPE_MESSAGE = 2;
    private int curSearchType;

    // 启动fragment的参数
    private static final int SEARCH_MAILBOX = 0;
    private static final int SEARCH_NICKNAME = 1;
    private static final int MY_APPLICATION = 2;

    private static final int SEARCH_GROUP_CHAT_ID = 3;
    private static final int SEARCH_GROUP_CHAT_NICKNAME = 4;
    private static final int MY_GROUP_CHAR_APPLICATION = 5;

    private static final int SEARCH_HISTORY_MESSAGE = 6; // 废弃

    public static void startMsgSearch(Context context, int sessionId) {
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtra(SEARCH_TYPE, SEARCH_TYPE_MESSAGE);
        intent.putExtra(SESSION_ID, sessionId);
        context.startActivity(intent);
    }

    public static void startSearchNewFriendSearch(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtra(SEARCH_TYPE, SEARCH_TYPE_FRIEND);
        context.startActivity(intent);
    }

    public static void startSearchNewGroupSearch(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtra(SEARCH_TYPE, SEARCH_TYPE_GROUP_CHAT);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        initData();
        initView();
    }

    protected void initData() {
        // 从Intent获取搜索类型（好友/群聊）,KEY 为 SEARCH_TYPE
        curSearchType = getIntent().getIntExtra(SEARCH_TYPE, 0);

        if (curSearchType == SEARCH_TYPE_FRIEND) {
            mTitles = this.getResources().getStringArray(R.array.indicator_search_new_friend_title);
        } else if (curSearchType == SEARCH_TYPE_GROUP_CHAT) {
            mTitles = this.getResources().getStringArray(R.array.indicator_search_new_group_chat_title);
        } else if (curSearchType == SEARCH_TYPE_MESSAGE) {
            sessionId = getIntent().getIntExtra(SESSION_ID, 0);
            mTitles = this.getResources().getStringArray(R.array.indicator_search_history_message_title);
        }
    }

    protected void initView() {
        if (curSearchType == SEARCH_TYPE_FRIEND) {
            b.myToolbar.title.setText(R.string.title_search_new_friend);
        } else if (curSearchType == SEARCH_TYPE_GROUP_CHAT) {
            b.myToolbar.title.setText(R.string.title_search_new_group_chat);
        } else if (curSearchType == SEARCH_TYPE_MESSAGE) {
            b.myToolbar.title.setText(R.string.title_search_history_message);
        }

        b.myToolbar.imgChevronLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });
        initFragments(curSearchType);
        initNewFriendsMagicIndicator();
        mFragmentContainerHelper.handlePageSelected(0, false);
        switchPages(0);
    }

    private void switchPages(int index) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment;
        for (int i = 0, j = mFragments.size(); i < j; i++) {
            if (i == index) {
                continue;
            }
            fragment = mFragments.get(i);
            if (fragment.isAdded()) {
                fragmentTransaction.hide(fragment);
            }
        }
        fragment = mFragments.get(index);
        if (fragment.isAdded()) {
            fragmentTransaction.show(fragment);
        } else {
            fragmentTransaction.add(b.newFriendsFragmentContainer.getId(), fragment);
        }
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void initFragments(int curSearchType) {
        switch (curSearchType) {
            case SEARCH_TYPE_FRIEND: {
                // 邮箱搜索界面
                mFragments.add(SearchFragment.newInstance(SEARCH_MAILBOX, 0));
                // 昵称搜索界面
                mFragments.add(SearchFragment.newInstance(SEARCH_NICKNAME, 0));
                // 我的申请界面
                mFragments.add(SearchFragment.newInstance(MY_APPLICATION, 0));
                break;
            }
            case SEARCH_TYPE_GROUP_CHAT: {
                // 群号搜索界面
                mFragments.add(SearchFragment.newInstance(SEARCH_GROUP_CHAT_ID, 0));
                // 群昵称搜索界面
                mFragments.add(SearchFragment.newInstance(SEARCH_GROUP_CHAT_NICKNAME, 0));
                // 我的群聊申请界面
                mFragments.add(SearchFragment.newInstance(MY_GROUP_CHAR_APPLICATION, 0));
                break;
            }
            case SEARCH_TYPE_MESSAGE: {
                // 搜索消息记录界面
                mFragments.add(SearchHistoryMsgFragment.newInstance(sessionId));
                break;
            }
            default:
                break;
        }
    }

    private void initNewFriendsMagicIndicator() {

        int TEXT_LINK = AttrColorUtils.getValueOfColorAttr(this, R.attr.text_link);
        int BG_WEAK = AttrColorUtils.getValueOfColorAttr(this, R.attr.bg_weak);
        int TEXT_MEDIUM = AttrColorUtils.getValueOfColorAttr(this, R.attr.text_medium);

        b.newFriendsIndicator.setBackgroundColor(BG_WEAK);

        // 创建适配器
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mTitles == null ? 0 : mTitles.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, int index) {
                ClipPagerTitleView clipPagerTitleView = new ClipPagerTitleView(context);
                clipPagerTitleView.setText(mTitles[index]);
                clipPagerTitleView.setTextColor(TEXT_MEDIUM);
                clipPagerTitleView.setTextSize(getResources().getDimension(R.dimen.im_text_medium));
                clipPagerTitleView.setClipColor(TEXT_LINK);
                clipPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 指示器点击事件
                        mFragmentContainerHelper.handlePageSelected(index);
                        switchPages(index);
                    }
                });
                return clipPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator linePagerIndicator = new LinePagerIndicator(context);
                linePagerIndicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                linePagerIndicator.setColors(TEXT_LINK);
                return linePagerIndicator;
            }
        });
        // 绑定指示器和适配器
        b.newFriendsIndicator.setNavigator(commonNavigator);
        mFragmentContainerHelper.attachMagicIndicator(b.newFriendsIndicator);
    }

}
