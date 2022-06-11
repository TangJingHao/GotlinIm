package com.ByteDance.Gotlin.im.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.ByteDance.Gotlin.im.databinding.DFragmentSearchNewBinding;

public class SearchNewFragment extends Fragment {

    private DFragmentSearchNewBinding b;

    private static final String SEARCH_PARAM = "search_param";

    private int searchParam;

    // 启动fragment的参数
    private static final int SEARCH_MAILBOX = 0;
    private static final int SEARCH_NICKNAME = 1;
    private static final int MY_APPLICATION = 2;
    private static final int SEARCH_GROUP_CHAT_ID = 3;
    private static final int SEARCH_GROUP_CHAT_NICKNAME = 4;
    private static final int MY_GROUP_CHAR_APPLICATION = 5;

    public SearchNewFragment() {
        // Required empty public constructor
    }

    /**
     * 工厂模式创建Fragment实例类
     * @param searchParam 当前页面指示器选项卡
     * @return 实例化Fragment.
     */
    public static SearchNewFragment newInstance(int searchParam) {
        SearchNewFragment fragment = new SearchNewFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        b = DFragmentSearchNewBinding.inflate(inflater,container,false);
        // TODO 处理不同条件下的搜索资料
        switch (searchParam){
            case SEARCH_MAILBOX:{
                break;
            }
            case SEARCH_NICKNAME:{

                break;
            }
            case MY_APPLICATION:{
                b.searchBox.fLayout.setVisibility(View.GONE);
                break;
            }
            case SEARCH_GROUP_CHAT_ID:{

                break;
            }case SEARCH_GROUP_CHAT_NICKNAME:{

                break;
            }
            case MY_GROUP_CHAR_APPLICATION:{
                b.searchBox.fLayout.setVisibility(View.GONE);
                break;
            }
            default:
                break;
        }
        return b.getRoot();
    }
}