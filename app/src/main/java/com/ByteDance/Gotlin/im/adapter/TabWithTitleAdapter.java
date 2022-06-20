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
import com.ByteDance.Gotlin.im.util.DUtils.DLogUtils;
import com.ByteDance.Gotlin.im.util.Tutils.TPhoneUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
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

    private final static String TAG = "TabWithTitleAdapter";

    private final Context mContext;

    public List<String> getmTitleList() {
        return mTitleList;
    }

    public List<Integer> getmTitleIndexList() {
        return mTitleIndexList;
    }

    // 要展示的信息
    private final List<List<E>> mDataInfoList;
    // 各组组名
    private final List<String> mTitleList;
    // 记录标题下标
    private final List<Integer> mTitleIndexList;
    // item总个数
    private int totalLen = 0;

    /*
    建立哈希表存储绝对布局与相对布局的对应关系
    前6位为组编号，若组编号为31（111111），则判定为标题
    后26位为相对编号
    * */
    // 后26位为1
    private final static int MASK_POSITION = 67108863;
    // 前6位为1
    private final static int MASK_GROUP = ~MASK_POSITION;
    // 判断标题值
    private final static int GROUP_TITLE = 31;
    // 哈希表
    private int[] map;

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

    private static int DEFAULT_IMG = R.drawable.ic_img_default;

    RoundedCorners roundedCorners = new RoundedCorners(8);//数字为圆角度数
    RequestOptions options = new RequestOptions()
            .transforms(new CenterCrop(), roundedCorners)
            .diskCacheStrategy(DiskCacheStrategy.NONE)//不做磁盘缓存
            .skipMemoryCache(true);//不做内存缓存

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
     * @param dataList       需要展示的用户信息集合
     * @param groupTitleList 对应用户信息的各组标题（使用必须注意两参数是否大小一致）
     * @param tabType        展示类型，如 TYPE_USER_INFO_STATUE
     */
    public TabWithTitleAdapter(Context context,
                               List<List<E>> dataList,
                               List<String> groupTitleList,
                               int tabType) {
        mContext = context;
        mDataInfoList = dataList;
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
        initMap();
    }

    /**
     * 存入初始化表
     */
    private void initMap() {
        map = new int[totalLen];
        int curGroupIndex = 0;
        for (int i = 0; i < totalLen; i++) {
            int group;
            int position;
            if (getItemViewType(i) == TYPE_TITLE) {
                group = GROUP_TITLE;
                position = curGroupIndex;
                curGroupIndex++;
            } else {
                int curGroupLen;    // 当前所在组长度
                if (curGroupIndex == mDataInfoList.size()) { // 到了最后一组
                    curGroupLen = totalLen - mTitleIndexList.get(curGroupIndex - 1) - 1;
                } else {
                    curGroupLen = mTitleIndexList.get(curGroupIndex) - mTitleIndexList.get(curGroupIndex - 1) - 1;
                }
                int curGroupTitleIndex = mTitleIndexList.get(curGroupIndex - 1);

                group = curGroupIndex - 1;
                position = curGroupLen - (curGroupLen - (i - curGroupTitleIndex)) - 1;
            }
            map[i] = group << 26 | position;
        }

        // 测试结果
//        for (int i = 0; i < map.length; i++) {
//            int group = getGroupFromMap(i);
//            int position = getRelativePositionFromMap(i);
//            if(group == GROUP_TITLE){
//                DLogUtils.i(TAG,"\n" + mTitleList.get(position));
//            }else{
//                DLogUtils.i(TAG,"\n G: " + group + "\t P: " + position);
//            }
//        }
    }

    /**
     * 根据绝对位置获取当前组别
     */
    private int getGroupFromMap(int position) {
        return (map[position] & MASK_GROUP) >> 26;
    }

    /**
     * 根据绝对位置获取当前组别下的相对位置
     */
    private int getRelativePositionFromMap(int position) {
        return map[position] & MASK_POSITION;
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
        int group = getGroupFromMap(position);
        int relativePosition = getRelativePositionFromMap(position);
        if (group == GROUP_TITLE) { // 是标题类型，此时relativePosition表示第几组
            TitleViewHolder titleHolder = (TitleViewHolder) holder;
            if (relativePosition < mTitleList.size()) {
                String title = mTitleList.get(relativePosition);
                if (title == "-") {
                    titleHolder.b.tvListTitle.setText("功能区");
                    titleHolder.b.fLayout.setBackgroundColor(AttrColorUtils.getValueOfColorAttr(mContext, R.attr.bg_default));
                } else
                    titleHolder.b.tvListTitle.setText(mTitleList.get(relativePosition));
            } else {
                titleHolder.b.tvListTitle.setText("未知分组");
            }
        } else {
            E data = mDataInfoList.get(group).get(relativePosition);
            // TODO 泛型适配不同的User类，需要修改的靓仔请找到自己的case修改
            switch (mTabType) {
                case TYPE_USER_INFO_STATUE: {
                    UserStatueInfoViewHolder userHolder = (UserStatueInfoViewHolder) holder;
                    if (data instanceof GroupVO) {
                        GroupVO item = (GroupVO) data;
                        Glide.with(mContext)
                                .load(item.getAvatar() == null ? DEFAULT_IMG : BASE_URL + item.getAvatar())
                                .into(userHolder.b.imgUserPic);
                        userHolder.b.tvUserName.setText(item.getGroupName());
                        userHolder.b.tvUserMail.setText("gid：" + item.getGroupId());
                    } else if (data instanceof UserVO) {
                        UserVO item = (UserVO) data;
                        Glide.with(mContext)
                                .load(item.getAvatar() == null ? DEFAULT_IMG : BASE_URL + item.getAvatar())
                                .into(userHolder.b.imgUserPic);
                        userHolder.b.tvUserName.setText(item.getUserName());
                        userHolder.b.tvUserMail.setText("uid：" + item.getUserId());
                        userHolder.b.tvStatue.setText(item.getOnline() ? "在线" : "离线");
                    }
                    if (mItemOnClickListener != null)
                        userHolder.b.rLayout.setOnClickListener(view ->
                                mItemOnClickListener.onItemClick(view, group, relativePosition));
                    if (mOnMoreClickListener != null) {
                        userHolder.b.tvStatue.setOnClickListener(view ->
                                mOnMoreClickListener.onMoreClick(view,
                                        group, relativePosition));
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
                                .load(item.getAvatar() == null ? DEFAULT_IMG : BASE_URL + item.getAvatar())
                                .apply(options)
                                .into(simpleHolder.b.imgUserPic);
                        simpleHolder.b.tvUserName.setText(item.getGroupName());
                    } else if (data instanceof UserVO) {
                        UserVO item = (UserVO) data;
                        Glide.with(mContext)
                                .load(item.getAvatar() == null ? DEFAULT_IMG : BASE_URL + item.getAvatar())
                                .apply(options)
                                .into(simpleHolder.b.imgUserPic);
                        simpleHolder.b.tvUserName.setText(item.getUserName());
                    }
                    if (mItemOnClickListener != null) {
                        simpleHolder.b.rLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mItemOnClickListener.onItemClick(view, group, relativePosition);
                            }
                        });
                    }
                    break;
                }
                case TYPE_USER_MESSAGE: {
//                    // TODO 在此处处理泛型类的转换
//                    UserVO item = (UserVO) mDataInfoList.get(group).get(relativePosition);
//                    UserMessageViewHolder MessageHolder = (UserMessageViewHolder) holder;
//
//                    // TODO 网络头像加载，目前仅加载默认头像
//                    Glide.with(mContext)
//                            .load(item.getAvatar() == null ? DEFAULT_IMG : BASE_URL + item.getAvatar())
//                            .into(MessageHolder.b.imgUserPic);
//                    MessageHolder.b.tvUserName.setText(item.getUserName());
//                    MessageHolder.b.tvUserMsg.setText(item.getMsg());
//                    MessageHolder.b.tvTime.setText("当前时间");
//                    if (mItemOnClickListener != null)
//                        MessageHolder.b.rLayout.setOnClickListener(view ->
//                                mItemOnClickListener.onItemClick(view, group, relativePosition));
//                    break;
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
