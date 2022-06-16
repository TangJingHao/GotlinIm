package com.ByteDance.Gotlin.im.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ByteDance.Gotlin.im.databinding.DItemUserInfoMessageBinding;
import com.ByteDance.Gotlin.im.info.MessageList;
import com.ByteDance.Gotlin.im.info.vo.TestUser;
import com.ByteDance.Gotlin.im.util.DUtils.RedPointHelper;
import com.ByteDance.Gotlin.im.util.Tutils.TPhoneUtil;
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
    private final List<MessageList> mDataList;

    public UserMsgAdapter(Context mContext, List<MessageList> mDataList) {
        this.mContext = mContext;
        this.mDataList = mDataList;
    }

    // 点击tab的事件
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
        Integer sessionId = mDataList.get(position).getSession().getSessionId();
        String SessionName = mDataList.get(position).getSession().getName();
        String senderNickName = mDataList.get(position).getSender().getNickName();
        holder.b.tvUserName.setText(SessionName + sessionId);
        holder.b.tvUserMsg.setText(senderNickName + ": " + mDataList.get(position).getContent());
        holder.b.tvTime.setText(mDataList.get(position).getSendTime());
        int i = position;
        if (mItemOnClickListener != null)
            holder.b.rLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemOnClickListener.onItemClick(view, i);
                }
            });
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
