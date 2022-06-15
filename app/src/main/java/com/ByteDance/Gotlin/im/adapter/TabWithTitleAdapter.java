package com.ByteDance.Gotlin.im.adapter;

import static com.ByteDance.Gotlin.im.util.Constants.BASE_URL;

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
import com.ByteDance.Gotlin.im.info.vo.GroupVO;
import com.ByteDance.Gotlin.im.info.vo.TestUser;
import com.ByteDance.Gotlin.im.info.vo.UserVO;
import com.ByteDance.Gotlin.im.util.DUtils.AttrColorUtils;
import com.bumptech.glide.Glide;

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
    private final List<List<E>> mDataInfoList;
    // 各组组名
    private final List<String> mTitleList;
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

    public OnItemClickListener mItemOnClickListener = null;
    public OnMoreClickListener mOnMoreClickListener = null;

    public void setItemOnClickListener(OnItemClickListener listener) {
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
     * @param groupTitleList 对应用户信息的各组标题（使用必须注意两参数是否大小一致）
     * @param tabType        展示类型，如 TYPE_USER_INFO_STATUE
     */
    public TabWithTitleAdapter(Context context,
                               List<List<E>> searchUserList,
                               List<String> groupTitleList,
                               int tabType) {
        mContext = context;
        mDataInfoList = searchUserList;
        mTitleList = groupTitleList;
        mTabType = tabType;

        totalLen = mDataInfoList.size();
        mTitleIndexList = new ArrayList<>(totalLen);
        int titlePositionIndex = 0;
        // 确定总item数量，titleItem出现的position
        for (int i = 0; i < mDataInfoList.size(); i++) {
            mTitleIndexList.add(i, titlePositionIndex);
            int curGroupsize = mDataInfoList.get(i).size();
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
                return new SimpleInfoViewHolder(simpleBinding);
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
            if (curGroupIndex < mTitleList.size()) {
                titleHolder.b.tvListTitle.setText(mTitleList.get(curGroupIndex));
            } else {
                titleHolder.b.tvListTitle.setText("未知分组");
            }
            curGroupIndex++;
        } else {
            // 一些数学推导,重定位position相对位置
            int curGroupLen;
            if (curGroupIndex == mDataInfoList.size()) { // 到了最后一组
                curGroupLen = totalLen - mTitleIndexList.get(curGroupIndex - 1) - 1;
            } else {
                curGroupLen = mTitleIndexList.get(curGroupIndex) - mTitleIndexList.get(curGroupIndex - 1) - 1;
            }
            int curGroupTitleIndex = mTitleIndexList.get(curGroupIndex - 1);
            int relativePosition = curGroupLen - (curGroupLen - (position - curGroupTitleIndex)) - 1;

            // 适配不同的tab,当前组编号为curGroupIndex - 1，相对组内位置为relativePosition
            E data = mDataInfoList.get(curGroupIndex - 1).get(relativePosition);

            // TODO 泛型适配不同的User类，需要修改的靓仔请找到自己的case修改
            switch (mTabType) {
                case TYPE_USER_INFO_STATUE: {
                    UserStatueInfoViewHolder userHolder = (UserStatueInfoViewHolder) holder;
                    if (data instanceof GroupVO) {
                        GroupVO item = (GroupVO) data;
                        Glide.with(mContext)
                                .load(BASE_URL + item.getAvatar())
                                .into(userHolder.b.imgUserPic);
                        userHolder.b.tvUserName.setText(item.getGroupName());
                        userHolder.b.tvUserMail.setText("gid：" + item.getGroupId());
                    } else if (data instanceof UserVO) {
                        UserVO item = (UserVO) data;
                        Glide.with(mContext)
                                .load(BASE_URL + item.getAvatar())
                                .into(userHolder.b.imgUserPic);
                        userHolder.b.tvUserName.setText(item.getUserName());
                        userHolder.b.tvUserMail.setText("uid：" + item.getUserId());
                        userHolder.b.tvStatue.setText(item.getOnline() ? "在线" : "离线");
                    }
                    if (mItemOnClickListener != null)
                        userHolder.b.rLayout.setOnClickListener(view ->
                                mItemOnClickListener.onItemClick(view,
                                        curGroupIndex - 1, relativePosition));
                    if (mOnMoreClickListener != null) {
                        userHolder.b.tvStatue.setOnClickListener(view ->
                                mOnMoreClickListener.onMoreClick(view,
                                        curGroupIndex - 1, relativePosition));
                        userHolder.b.tvStatue.setBackgroundColor(
                                AttrColorUtils.getValueOfColorAttr(mContext, R.attr.accent_default));
                        userHolder.b.tvStatue.setText("操作");
                        userHolder.b.tvStatue.setTextColor(AttrColorUtils
                                .getValueOfColorAttr(mContext, R.attr.fill_white));
                    }
                    break;
                }
                case TYPE_USER_INFO_SIMPLE: {
                    SimpleInfoViewHolder simpleHolder = (SimpleInfoViewHolder) holder;
                    if (data instanceof GroupVO) {
                        GroupVO item = (GroupVO) data;
                        Glide.with(mContext)
                                .load(BASE_URL + item.getAvatar())
                                .into(simpleHolder.b.imgUserPic);
                        simpleHolder.b.tvUserName.setText(item.getGroupName());
                    } else if (data instanceof UserVO) {
                        UserVO item = (UserVO) data;
                        Glide.with(mContext)
                                .load(item.getAvatar())
                                .into(simpleHolder.b.imgUserPic);
                        simpleHolder.b.tvUserName.setText(item.getUserName());
                    }
                    if (mItemOnClickListener != null)
                        simpleHolder.b.rLayout.setOnClickListener(view ->
                                mItemOnClickListener.onItemClick(view,
                                        curGroupIndex - 1, relativePosition));
                    break;
                }
                case TYPE_USER_MESSAGE: {
                    // TODO 在此处处理泛型类的转换
                    TestUser user = (TestUser) mDataInfoList.get(curGroupIndex - 1).get(relativePosition);
                    UserMessageViewHolder MessageHolder = (UserMessageViewHolder) holder;

                    // TODO 网络头像加载，目前仅加载默认头像
                    Glide.with(mContext)
                            .load(DEFAULT_IMG)
                            .into(MessageHolder.b.imgUserPic);
                    MessageHolder.b.tvUserName.setText(user.getUserName());
                    MessageHolder.b.tvUserMsg.setText(user.getMsg());
                    MessageHolder.b.tvTime.setText("当前时间");
                    if (mItemOnClickListener != null)
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

    static class SimpleInfoViewHolder extends RecyclerView.ViewHolder {
        DItemUserInfoSimpleBinding b;

        public SimpleInfoViewHolder(@NonNull DItemUserInfoSimpleBinding b) {
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
