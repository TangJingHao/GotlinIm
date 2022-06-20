package com.ByteDance.Gotlin.im.adapter;

import static com.ByteDance.Gotlin.im.util.Constants.BASE_URL;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ByteDance.Gotlin.im.R;
import com.ByteDance.Gotlin.im.Repository;
import com.ByteDance.Gotlin.im.databinding.DItemUserInfoMessageBinding;
import com.ByteDance.Gotlin.im.info.MessageList;
import com.ByteDance.Gotlin.im.info.vo.SessionVO;
import com.ByteDance.Gotlin.im.info.vo.UserVO;
import com.ByteDance.Gotlin.im.util.DUtils.RedPointHelper;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.badge.BadgeDrawable;

import java.util.List;

/**
 * @Author Zhicong Deng
 * @Date 2022/6/14 15:34
 * @Email 1520483847@qq.com
 * @Description 消息列表界面适配器(已废弃)
 */
@Deprecated
public class UserMsgAdapter extends RecyclerView.Adapter<UserMsgAdapter.UserMsgHolder> {

    private final Context mContext;
    private final List<MessageList> mDataList;

    RoundedCorners roundedCorners = new RoundedCorners(8);//数字为圆角度数
    RequestOptions options = new RequestOptions()
            .transforms(new CenterCrop(), roundedCorners)
            .diskCacheStrategy(DiskCacheStrategy.NONE)//不做磁盘缓存
            .skipMemoryCache(true);//不做内存缓存

    private static int DEFAULT_IMG = R.drawable.d_img_useravatar1;

    public UserMsgAdapter(Context mContext, List<MessageList> mDataList) {
        this.mContext = mContext;
        this.mDataList = mDataList;
    }

    //点击tab的事件
    public interface OnItemClickListener {
        void onItemClick(View v, int position);
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
        SessionVO session = mDataList.get(position).getSession();
        UserVO sender = mDataList.get(position).getSender();
        String content = mDataList.get(position).getContent();

        String type = session.getType() == 1 ? "群聊会话" : "好友会话";

        Integer sessionId = session.getSessionId();
        String SessionName = session.getName();
        String sessionAvatar = session.getAvatar();
        String senderNickName = sender.getNickName();


        if (sender.getUserId() == Repository.INSTANCE.getUserId()) {
            senderNickName = "我";
        }

        Glide.with(mContext)
                .load(sessionAvatar == null ? DEFAULT_IMG : BASE_URL + sessionAvatar)
                .apply(options)
                .into(holder.b.bgaImgUserPic);

        holder.b.bgaTvSessionName.setText("[" + type + sessionId + "] " + SessionName);
        holder.b.bgaTvUserMsg.setText(senderNickName + ": " + content);

        holder.b.bgaTvTime.setText(mDataList.get(position).getSendTime());
        int i = position;
        if (mItemOnClickListener != null)
            holder.b.bgaRLayout.setOnClickListener(new View.OnClickListener() {
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
