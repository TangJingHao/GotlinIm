package com.ByteDance.Gotlin.im.view.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.ByteDance.Gotlin.im.R;
import com.ByteDance.Gotlin.im.databinding.DActivitySearchBinding;
import com.ByteDance.Gotlin.im.view.fragment.SearchFragment;

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

    private List<Fragment> mFragments = new ArrayList<>();
    private FragmentContainerHelper mFragmentContainerHelper = new FragmentContainerHelper();

    private String[] mTitles;
    // 搜索类型（好友群聊）
    private static final String SEARCH_TYPE = "search_type";
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
    private static final int SEARCH_HISTORY_MESSAGE = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        initData();
        initView();
    }

    protected void initData() {
        // TODO 从Intent获取搜索类型（好友/群聊）,KEY 为 SEARCH_TYPE
        curSearchType = getIntent().getIntExtra(SEARCH_TYPE, 0);
        if (curSearchType == SEARCH_TYPE_FRIEND) {
            mTitles = this.getResources().getStringArray(R.array.search_new_friend_title);
        } else if (curSearchType == SEARCH_TYPE_GROUP_CHAT) {
            mTitles = this.getResources().getStringArray(R.array.search_new_group_chat_title);
        } else if(curSearchType == SEARCH_TYPE_MESSAGE){
            mTitles = this.getResources().getStringArray(R.array.search_history_message_title);
        }
    }

    protected void initView() {
        if (curSearchType == SEARCH_TYPE_FRIEND) {
            b.myToolbar.title.setText(R.string.title_search_new_friend);
        } else if (curSearchType == SEARCH_TYPE_GROUP_CHAT) {
            b.myToolbar.title.setText(R.string.title_search_new_group_chat);
        } else if(curSearchType == SEARCH_TYPE_MESSAGE){
            b.myToolbar.title.setText(R.string.title_search_history_message);
        }

        b.myToolbar.imgChevronLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
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
        switch (curSearchType){
            case SEARCH_TYPE_FRIEND:{
                // 邮箱搜索界面
                SearchFragment searchNewByMailboxFragment
                        = SearchFragment.newInstance(SEARCH_MAILBOX);
                // 昵称搜索界面
                SearchFragment searchNewByNickNameFragment
                        = SearchFragment.newInstance(SEARCH_NICKNAME);
                // 我的申请界面
                SearchFragment myApplicationFragment
                        = SearchFragment.newInstance(MY_APPLICATION);

                mFragments.add(searchNewByMailboxFragment);
                mFragments.add(searchNewByNickNameFragment);
                mFragments.add(myApplicationFragment);
                break;
            }
            case SEARCH_TYPE_GROUP_CHAT:{
                // 群号搜索界面
                SearchFragment searchNewByGroupIdFragment
                        = SearchFragment.newInstance(SEARCH_GROUP_CHAT_ID);
                // 群昵称搜索界面
                SearchFragment searchNewByGroupNickNameFragment
                        = SearchFragment.newInstance(SEARCH_GROUP_CHAT_NICKNAME);
                // 我的群聊申请界面
                SearchFragment myGroupCharApplicationFragment
                        = SearchFragment.newInstance(MY_GROUP_CHAR_APPLICATION);

                mFragments.add(searchNewByGroupIdFragment);
                mFragments.add(searchNewByGroupNickNameFragment);
                mFragments.add(myGroupCharApplicationFragment);
                break;
            }
            case SEARCH_TYPE_MESSAGE:{
                // 搜索消息记录界面
                SearchFragment searchHistoryMessageFragment
                        = SearchFragment.newInstance(SEARCH_HISTORY_MESSAGE);
                mFragments.add(searchHistoryMessageFragment);
                break;
            }
            default:
                break;
        }
    }

    private void initNewFriendsMagicIndicator() {

        int ACCENT_PRESS = getValueOfColorAttr(this, R.attr.accent_press);
        int BG_WEAK = getValueOfColorAttr(this, R.attr.bg_weak);
        int TEXT_MEDIUM = getValueOfColorAttr(this, R.attr.text_medium);

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
                clipPagerTitleView.setClipColor(ACCENT_PRESS);
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
                linePagerIndicator.setColors(ACCENT_PRESS);
                return linePagerIndicator;
            }
        });
        // 绑定指示器和适配器
        b.newFriendsIndicator.setNavigator(commonNavigator);
        mFragmentContainerHelper.attachMagicIndicator(b.newFriendsIndicator);
    }

    public static int getValueOfColorAttr(Context context, int attr) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(attr, typedValue, true);
        return Color.parseColor(String.valueOf(typedValue.coerceToString()));
    }
}
