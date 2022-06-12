package com.ByteDance.Gotlin.im.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ByteDance.Gotlin.im.databinding.DItemLittleTitleBinding;
import com.ByteDance.Gotlin.im.databinding.DItemUserInfoStatueBinding;
import com.ByteDance.Gotlin.im.info.SearchUser;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Zhicong Deng
 * @Date 2022/6/12 15:43
 * @Email 1520483847@qq.com
 * @Description 复合多布局的RecycleView适配器
 */
public class SearchAdapter extends RecyclerView.Adapter {

    private final Context mContext;
    // 要展示的信息
    private final List<List<SearchUser>> mSearchUserInfoList;
    // 各组组名
    private final List<String> mGroupNameList;
    // 记录标题下标
    private final List<Integer> mTitleIndexList;
    // 当前所在组
    private int curGroupIndex = 0;
    // item总个数
    private int totalLen = 0;

    public static final int TYPE_TITLE = 0;
    public static final int TYPE_USER_INFO = 1;

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    public OnItemClickListener mOnItemClickListener;

    public void setSearchItemOnClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public SearchAdapter(Context context, List<List<SearchUser>> searchUserList, List<String> groupTitleList) {
        mContext = context;
        mSearchUserInfoList = searchUserList;
        mGroupNameList = groupTitleList;

        totalLen = mSearchUserInfoList.size();
        mTitleIndexList = new ArrayList<>(totalLen);
        int titlePositionIndex = 0;
        // 确定总item数量，titleItem出现的position
        for (int i = 0; i < mSearchUserInfoList.size(); i++) {
            mTitleIndexList.add(i, titlePositionIndex);
            int curGroupsize = mSearchUserInfoList.get(i).size();
            titlePositionIndex += 1 + curGroupsize;
            totalLen += curGroupsize;
        }

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        DItemLittleTitleBinding titleBinding = DItemLittleTitleBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);

        DItemUserInfoStatueBinding userInfoStatueBinding = DItemUserInfoStatueBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);

        if (viewType == TYPE_TITLE) {
            return new ItemTitleViewHolder(titleBinding);
        } else {
            return new ItemUserInfoViewHolder(userInfoStatueBinding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // TODO 待完善
        if (getItemViewType(position) == TYPE_TITLE) {
            ItemTitleViewHolder titleHolder = (ItemTitleViewHolder) holder;
            if (curGroupIndex < mGroupNameList.size()) {
                titleHolder.b.tvListTitle.setText(mGroupNameList.get(curGroupIndex));
            } else {
                titleHolder.b.tvListTitle.setText("未知分组");
            }
            curGroupIndex++;
        } else {
            // 一些数学推导,重定位position相对位置
            int curGroupLen;
            if (curGroupIndex == mSearchUserInfoList.size()) { // 到了最后一组
                curGroupLen = totalLen - mTitleIndexList.get(curGroupIndex - 1) - 1;
            } else {
                curGroupLen = mTitleIndexList.get(curGroupIndex) - mTitleIndexList.get(curGroupIndex - 1) - 1;
            }
            int curGroupTitleIndex = mTitleIndexList.get(curGroupIndex - 1);
            int relativePosition = curGroupLen - (curGroupLen - (position - curGroupTitleIndex)) - 1;
            // 前面判断完后curGroupIndex会+1,所以此处-1
            SearchUser user = mSearchUserInfoList.get(curGroupIndex - 1).get(relativePosition);
            ItemUserInfoViewHolder userInfoHolder = (ItemUserInfoViewHolder) holder;
            // TODO 头像加载
            userInfoHolder.b.tvUserName.setText(user.getUserName());
            userInfoHolder.b.tvUserDetail.setText(user.getUserMail());
            userInfoHolder.b.tvStatue.setText(user.getStatue());
        }
    }

    @Override
    public int getItemCount() {
        return totalLen;
    }

    @Override
    public int getItemViewType(int position) {
        for (int index : mTitleIndexList) {
            if (position == index) return TYPE_TITLE;
        }
        return TYPE_USER_INFO;
    }


    static class ItemTitleViewHolder extends RecyclerView.ViewHolder {
        DItemLittleTitleBinding b;

        public ItemTitleViewHolder(@NonNull DItemLittleTitleBinding b) {
            super(b.getRoot());
            this.b = b;
        }
    }

    static class ItemUserInfoViewHolder extends RecyclerView.ViewHolder {
        DItemUserInfoStatueBinding b;

        public ItemUserInfoViewHolder(@NonNull DItemUserInfoStatueBinding b) {
            super(b.getRoot());
            this.b = b;
        }
    }


}
