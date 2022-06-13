package com.ByteDance.Gotlin.im.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ByteDance.Gotlin.im.R;
import com.ByteDance.Gotlin.im.databinding.DItemLittleTitleBinding;
import com.ByteDance.Gotlin.im.databinding.DItemUserInfoMessageBinding;
import com.ByteDance.Gotlin.im.databinding.DItemUserInfoSimpleBinding;
import com.ByteDance.Gotlin.im.databinding.DItemUserInfoStatueBinding;
import com.ByteDance.Gotlin.im.info.TestUser;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Zhicong Deng
 * @Date 2022/6/12 15:43
 * @Email 1520483847@qq.com
 * @Description 复合多布局的RecycleView适配器
 * 适用于标题+用户选项的展示
 * <p>
 * 【注】目前还未做集合类型适配
 */
public class TabWithTitleAdapter<E> extends RecyclerView.Adapter {

    private final Context mContext;
    // 要展示的信息
    private final List<List<E>> mSearchUserInfoList;
    // 各组组名
    private final List<String> mGroupNameList;
    // 记录标题下标
    private final List<Integer> mTitleIndexList;
    // 当前所在组
    private int curGroupIndex = 0;
    // item总个数
    private int totalLen = 0;

    // 标题
    private static final int TYPE_TITLE = 0;
    // 头像、用户名、邮箱信息、状态
    public static final int TYPE_USER_INFO_STATUE = 1;
    // 头像、用户名
    public static final int TYPE_USER_INFO_SIMPLE = 2;
    // 头像、用户名、信息、发送时间
    public static final int TYPE_USER_MESSAGE = 3;
    // 当前状态（只能是1/2/3）
    public int mTabType;

    private static int DEFAULT_IMG = R.drawable.d_img_useravatar1;

    // 点击tab的事件
    public interface OnItemClickListener {
        void onItemClick(View v, int groupPosition, int relativePosition);
    }

    // 点击更多的事件，此处一般用于好友申请接收状态
    public interface OnMoreClickListener {
        void onMoreClick(View v, int groupPosition, int relativePosition);
    }

    public OnItemClickListener mItemOnClickListener;
    public OnMoreClickListener mOnMoreClickListener;

    public void setMoreOnClickListener(OnItemClickListener listener) {
        this.mItemOnClickListener = listener;
    }

    public void setMoreOnClickListener(OnMoreClickListener listener) {
        this.mOnMoreClickListener = listener;
    }

    /**
     * adapter构造方法
     *
     * @param context        context
     * @param searchUserList 需要展示的用户信息集合
     * @param groupTitleList 对应用户信息的各组标题
     * @param tabType        展示类型，如 TYPE_USER_INFO_STATUE
     */
    public TabWithTitleAdapter(Context context,
                               List<List<E>> searchUserList,
                               List<String> groupTitleList,
                               int tabType) {
        mContext = context;
        mSearchUserInfoList = searchUserList;
        mGroupNameList = groupTitleList;
        mTabType = tabType;

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
        switch (viewType) {
            case TYPE_TITLE: {
                DItemLittleTitleBinding titleBinding = DItemLittleTitleBinding
                        .inflate(LayoutInflater.from(parent.getContext()), parent, false);
                return new TitleViewHolder(titleBinding);
            }
            case TYPE_USER_INFO_STATUE: {
                DItemUserInfoStatueBinding statueBinding = DItemUserInfoStatueBinding
                        .inflate(LayoutInflater.from(parent.getContext()), parent, false);
                return new UserStatueInfoViewHolder(statueBinding);
            }
            case TYPE_USER_INFO_SIMPLE: {
                DItemUserInfoSimpleBinding simpleBinding = DItemUserInfoSimpleBinding
                        .inflate(LayoutInflater.from(parent.getContext()), parent, false);
                return new SimpleUserInfoViewHolder(simpleBinding);
            }
            default: {  // TYPE_USER_MESSAGE
                DItemUserInfoMessageBinding messageBinding = DItemUserInfoMessageBinding
                        .inflate(LayoutInflater.from(parent.getContext()), parent, false);
                return new UserMessageViewHolder(messageBinding);
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_TITLE) {
            TitleViewHolder titleHolder = (TitleViewHolder) holder;
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

            // 适配不同的tab,当前组编号为curGroupIndex - 1，相对组内位置为relativePosition
            // TODO 泛型适配不同的User类，需要修改的靓仔请找到自己的case修改
            switch (mTabType) {
                case TYPE_USER_INFO_STATUE: {
                    // TODO 在此处处理泛型类的转换
                    TestUser user = (TestUser) mSearchUserInfoList.get(curGroupIndex - 1).get(relativePosition);
                    UserStatueInfoViewHolder userHolder = (UserStatueInfoViewHolder) holder;

                    // TODO 网络头像加载，目前仅加载默认头像
                    Glide.with(mContext)
                            .load(DEFAULT_IMG)
                            .apply(RequestOptions.bitmapTransform(new RoundedCorners(4)))
                            .into(userHolder.b.imgUserPic);//四周都是圆角的圆角矩形图片。
                    userHolder.b.tvUserName.setText(user.getUserName());
                    userHolder.b.tvUserMail.setText(user.getUserMail());
                    userHolder.b.tvStatue.setText(user.getStatue());
                    userHolder.b.rLayout.setOnClickListener(view ->
                            mItemOnClickListener.onItemClick(view,
                            curGroupIndex - 1, relativePosition));
                    userHolder.b.tvStatue.setOnClickListener(view ->
                            mOnMoreClickListener.onMoreClick(view,
                            curGroupIndex - 1, relativePosition));
                    break;
                }
                case TYPE_USER_INFO_SIMPLE: {
                    // TODO 在此处处理泛型类的转换
                    TestUser user = (TestUser) mSearchUserInfoList.get(curGroupIndex - 1).get(relativePosition);
                    SimpleUserInfoViewHolder simpleUserHolder = (SimpleUserInfoViewHolder) holder;

                    // TODO 网络头像加载，目前仅加载默认头像
                    Glide.with(mContext)
                            .load(DEFAULT_IMG)
                            .apply(RequestOptions.bitmapTransform(new RoundedCorners(4)))
                            .into(simpleUserHolder.b.imgUserPic);
                    simpleUserHolder.b.tvUserName.setText(user.getUserName());
                    simpleUserHolder.b.rLayout.setOnClickListener(view ->
                            mItemOnClickListener.onItemClick(view,
                            curGroupIndex - 1, relativePosition));
                    break;
                }
                case TYPE_USER_MESSAGE: {
                    // TODO 在此处处理泛型类的转换
                    TestUser user = (TestUser) mSearchUserInfoList.get(curGroupIndex - 1).get(relativePosition);
                    UserMessageViewHolder MessageHolder = (UserMessageViewHolder) holder;

                    // TODO 网络头像加载，目前仅加载默认头像
                    Glide.with(mContext)
                            .load(DEFAULT_IMG)
                            .apply(RequestOptions.bitmapTransform(new RoundedCorners(4)))
                            .into(MessageHolder.b.imgUserPic);
                    MessageHolder.b.tvUserName.setText(user.getUserName());
                    MessageHolder.b.tvUserMsg.setText(user.getMsg());
                    MessageHolder.b.tvTime.setText("当前时间");
                    MessageHolder.b.rLayout.setOnClickListener(view ->
                            mItemOnClickListener.onItemClick(view,
                            curGroupIndex - 1, relativePosition));
                    break;
                }
                default:
                    break;
            }

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
        return mTabType;
    }

    static class TitleViewHolder extends RecyclerView.ViewHolder {
        DItemLittleTitleBinding b;

        public TitleViewHolder(@NonNull DItemLittleTitleBinding b) {
            super(b.getRoot());
            this.b = b;
        }
    }

    static class UserStatueInfoViewHolder extends RecyclerView.ViewHolder {
        DItemUserInfoStatueBinding b;

        public UserStatueInfoViewHolder(@NonNull DItemUserInfoStatueBinding b) {
            super(b.getRoot());
            this.b = b;
        }
    }

    static class SimpleUserInfoViewHolder extends RecyclerView.ViewHolder {
        DItemUserInfoSimpleBinding b;

        public SimpleUserInfoViewHolder(@NonNull DItemUserInfoSimpleBinding b) {
            super(b.getRoot());
            this.b = b;
        }
    }

    static class UserMessageViewHolder extends RecyclerView.ViewHolder {
        DItemUserInfoMessageBinding b;

        public UserMessageViewHolder(@NonNull DItemUserInfoMessageBinding b) {
            super(b.getRoot());
            this.b = b;
        }
    }

}
