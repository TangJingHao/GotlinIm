package com.ByteDance.Gotlin.im.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ByteDance.Gotlin.im.R;
import com.ByteDance.Gotlin.im.adapter.TabWithTitleAdapter;
import com.ByteDance.Gotlin.im.databinding.DFragmentSearchBinding;
import com.ByteDance.Gotlin.im.info.TestUser;
import com.ByteDance.Gotlin.im.util.DUtils.AttrColorUtils;
import com.ByteDance.Gotlin.im.util.DUtils.DLogUtils;
import com.ByteDance.Gotlin.im.viewmodel.SearchViewModel;

import java.util.Arrays;
import java.util.List;

public class SearchFragment extends Fragment {

    private static final String TAG = "SearchFragment";
    private DFragmentSearchBinding b;

    private SearchViewModel searchViewModel;
    private List<List<TestUser>> searchUserList = null;

    private static final String SEARCH_PARAM = "search_param";

    private int searchParam;

    // 启动fragment的参数
    private static final int SEARCH_MAILBOX = 0;
    private static final int SEARCH_NICKNAME = 1;
    private static final int MY_APPLICATION = 2;
    private static final int SEARCH_GROUP_CHAT_ID = 3;
    private static final int SEARCH_GROUP_CHAT_NICKNAME = 4;
    private static final int MY_GROUP_CHAR_APPLICATION = 5;
    private static final int SEARCH_HISTORY_MESSAGE = 6;

    /**
     * 工厂模式创建Fragment实例类
     *
     * @param searchParam 当前页面指示器选项卡
     * @return 实例化Fragment.
     */
    public static SearchFragment newInstance(int searchParam) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putInt(SEARCH_PARAM, searchParam);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            searchParam = getArguments().getInt(SEARCH_PARAM);
        }
        searchViewModel = new ViewModelProvider(getActivity()).get(SearchViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        b = DFragmentSearchBinding.inflate(inflater, container, false);
        // 测试用
        initDataTest();
        initViewAndEvent();

        return b.getRoot();
    }

    /**
     * 测试用数据
     */
    private void initDataTest() {
        searchUserList = searchViewModel.searchNewFriendByMail("123456789@qq.com");
    }

    /**
     * 初始化不同搜索类型下的界面和交互时事件
     */
    private void initViewAndEvent() {
        String[] littleTitleArray;
        switch (searchParam) {
            case SEARCH_MAILBOX: {
                DLogUtils.i(TAG, "SEARCH_MAILBOX");
                littleTitleArray = getResources().getStringArray(R.array.rvLayout_search_result_little_title);
                break;
            }
            case SEARCH_NICKNAME: {
                DLogUtils.i(TAG, "SEARCH_NICKNAME");
                littleTitleArray = getResources().getStringArray(R.array.rvLayout_search_result_little_title);
                break;
            }
            case MY_APPLICATION: {
                DLogUtils.i(TAG, "MY_APPLICATION");
                littleTitleArray = getResources().getStringArray(R.array.rvLayout_my_application_little_title);
                b.searchBar.fLayout.setVisibility(View.GONE);
                break;
            }
            case SEARCH_GROUP_CHAT_ID: {
                DLogUtils.i(TAG, "SEARCH_GROUP_CHAT_ID");
                littleTitleArray = getResources().getStringArray(R.array.rvLayout_search_result_little_title);
                break;
            }
            case SEARCH_GROUP_CHAT_NICKNAME: {
                DLogUtils.i(TAG, "SEARCH_GROUP_CHAT_NICKNAME");
                littleTitleArray = getResources().getStringArray(R.array.rvLayout_search_result_little_title);
                break;
            }
            case MY_GROUP_CHAR_APPLICATION: {
                DLogUtils.i(TAG, "MY_GROUP_CHAR_APPLICATION");
                littleTitleArray = getResources().getStringArray(R.array.rvLayout_my_application_little_title);
                b.searchBar.fLayout.setVisibility(View.GONE);
                break;
            }
            case SEARCH_HISTORY_MESSAGE: {
                DLogUtils.i(TAG, "SEARCH_HISTORY_MESSAGE");
                littleTitleArray = getResources().getStringArray(R.array.rvLayout_search_result_little_title);
                b.timeBar.lLayout.setVisibility(View.VISIBLE);
                b.timeBar.tvTimeFrom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // TODO 拉起时间选择器，选择初始时间
                    }
                });
                b.timeBar.tvTimeTo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // TODO 拉起时间选择器，选择结束时间
                    }
                });
                break;
            }
            default:
                littleTitleArray = getResources().getStringArray(R.array.rvLayout_search_result_little_title);
                break;
        }


        // 多布局复合recycle
        TabWithTitleAdapter adapter = new TabWithTitleAdapter(
                getActivity(),
                searchUserList,
                Arrays.asList(littleTitleArray),
                TabWithTitleAdapter.TYPE_USER_MESSAGE);
        b.rvLayout.setLayoutManager(new LinearLayoutManager(getActivity()));
        b.rvLayout.setAdapter(adapter);
        if (searchUserList.size() != 0 && littleTitleArray.length != 0)
            adapter.notifyDataSetChanged();


        b.srLayout.setColorSchemeColors(AttrColorUtils
                .getValueOfColorAttr(getActivity(), R.attr.accent_default));
        b.srLayout.setProgressBackgroundColorSchemeColor(AttrColorUtils
                .getValueOfColorAttr(getActivity(), R.attr.bg_weak));
        b.srLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
    }

    /**
     * 刷新数据，一般需要参数，这里测试用
     */
    private void refreshData() {
        // TODO 网络请求获取资料（viewmodel），此处还未考虑线程

        // TODO 通知Adapter更新

        // 停止刷新动画
        b.srLayout.setRefreshing(false);
    }


}