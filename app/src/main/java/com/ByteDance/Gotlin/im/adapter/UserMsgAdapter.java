package com.ByteDance.Gotlin.im.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ByteDance.Gotlin.im.databinding.DItemUserInfoMessageBinding;
import com.ByteDance.Gotlin.im.info.vo.TestUser;
import com.ByteDance.Gotlin.im.util.DUtils.RedPointHelper;
import com.google.android.material.badge.BadgeDrawable;

import java.util.List;

/**
 * @Author Zhicong Deng
 * @Date 2022/6/14 15:34
 * @Email 1520483847@qq.com
 * @Description 消息列表界面适配器
 */
public class UserMsgAdapter extends RecyclerView.Adapter<UserMsgAdapter.UserMsgHolder> {

    private final Context mContext;
    private final List<TestUser> mDataList;

    public UserMsgAdapter(Context mContext, List<TestUser> mDataList) {
        this.mContext = mContext;
        this.mDataList = mDataList;
    }

    //点击tab的事件
    public interface OnItemClickListener {
        void onItemClick(View v, int Position);
    }

    public OnItemClickListener mItemOnClickListener = null;

    public void setItemOnClickListener(OnItemClickListener listener) {
        this.mItemOnClickListener = listener;
    }

    @NonNull
    @Override
    public UserMsgHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DItemUserInfoMessageBinding b = DItemUserInfoMessageBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        BadgeDrawable redPointBadge = RedPointHelper.getRedPointBadge(mContext, 0);
        return new UserMsgHolder(mContext, b, redPointBadge);
    }

    @Override
    public void onBindViewHolder(@NonNull UserMsgHolder holder, int position) {
        holder.b.tvUserName.setText(mDataList.get(position).getUserName());
        holder.b.tvUserMsg.setText(mDataList.get(position).getMsg());
        holder.b.tvTime.setText("05:20");
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    static class UserMsgHolder extends RecyclerView.ViewHolder {
        DItemUserInfoMessageBinding b;
        BadgeDrawable redPoint;

        public UserMsgHolder(Context context, @NonNull DItemUserInfoMessageBinding b, BadgeDrawable redPoint) {
            super(b.getRoot());
            this.b = b;
            this.redPoint = redPoint;
        }
    }




}
