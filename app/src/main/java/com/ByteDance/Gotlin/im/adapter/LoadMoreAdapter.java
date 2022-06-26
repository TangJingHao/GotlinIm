package com.ByteDance.Gotlin.im.adapter;

import static com.ByteDance.Gotlin.im.util.Constants.BASE_URL;
import static com.ByteDance.Gotlin.im.util.Constants.DEFAULT_IMG;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ByteDance.Gotlin.im.databinding.DItemFootBinding;
import com.ByteDance.Gotlin.im.databinding.DItemHistoryMessageBinding;
import com.ByteDance.Gotlin.im.entity.MessageEntity;
import com.ByteDance.Gotlin.im.util.DUtils.DLogUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Zhicong Deng
 * @Date 2022/6/23 18:33
 * @Email 1520483847@qq.com
 * @Description
 */
public class LoadMoreAdapter extends RecyclerView.Adapter {
    private static final String TAG = "UserHistoryMsgAdapter";
    private List<MessageEntity> mDataList = new ArrayList<>();
    private final Context mContext;

    //上拉加载更多布局
    public static final int TYPE_FOOT = 1;
    //主要布局
    public static final int TYPE_NORMAL = 2;
    //是否有隐藏
    private boolean hasMore = true;

    // 消息记录中被选中的高亮部分
    private String highLightPart = "";

    public LoadMoreAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<MessageEntity> mData) {
        this.mDataList = mData;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOT) {
            DItemFootBinding b = DItemFootBinding
                    .inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new FootViewHolder(b);
        } else {

            DItemHistoryMessageBinding b = DItemHistoryMessageBinding
                    .inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new HistoryMsgHolder(b);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FootViewHolder) {
            FootViewHolder footHolder = ((FootViewHolder) holder);
            if (hasMore) {
                footHolder.b.tvLoadMore.setText("正在加载更多...");
//                footHolder.b.tvLoadMore.setVisibility(View.VISIBLE);
            } else {
                footHolder.b.tvLoadMore.setText("没有更多数据了");
//                footHolder.b.tvLoadMore.setVisibility(View.GONE);
            }
        } else {
            HistoryMsgHolder msgHolder = ((HistoryMsgHolder) holder);
            msgHolder.b.tvSenderName.setText(mDataList.get(position).getSenderName());

            String highLightMasContent = changeMsgWithHighLight(mDataList.get(position).getContent());
            msgHolder.b.tvMsg.setText(Html.fromHtml(highLightMasContent));
            msgHolder.b.tvTime.setText(String.valueOf(mDataList.get(position).getSendTime()));

            String senderAvatar = mDataList.get(position).getSenderAvatar();
            Glide.with(mContext)
                    .load(senderAvatar == null ? DEFAULT_IMG : BASE_URL + senderAvatar)
                    .into(msgHolder.b.imgAvatar);

        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mDataList.size()) {
            return TYPE_FOOT;
        } else {
            return TYPE_NORMAL;
        }
    }

    //是否有更多数据
    public void hasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    // 设置查找的高亮数据
    public void setHighLightPart(String highLightPart) {
        this.highLightPart = highLightPart;
    }

    // 匹配到合适的文字并高光
    public String changeMsgWithHighLight(String content) {
        if (!highLightPart.equals("")) {
            return content.replaceAll(highLightPart, "<font color='#1e6fff'>" + highLightPart + "</font>");
        }
        return content;
    }

    static class HistoryMsgHolder extends RecyclerView.ViewHolder {
        DItemHistoryMessageBinding b;

        public HistoryMsgHolder(DItemHistoryMessageBinding b) {
            super(b.getRoot());
            this.b = b;
        }
    }

    static class FootViewHolder extends RecyclerView.ViewHolder {
        DItemFootBinding b;

        public FootViewHolder(DItemFootBinding b) {
            super(b.getRoot());
            this.b = b;
        }
    }
}




