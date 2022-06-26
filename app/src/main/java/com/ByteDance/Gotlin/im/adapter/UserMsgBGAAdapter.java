package com.ByteDance.Gotlin.im.adapter;

import static com.ByteDance.Gotlin.im.util.Constants.BASE_URL;

import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.ByteDance.Gotlin.im.R;
import com.ByteDance.Gotlin.im.Repository;
import com.ByteDance.Gotlin.im.info.MessageList;
import com.ByteDance.Gotlin.im.info.vo.SessionVO;
import com.ByteDance.Gotlin.im.info.vo.UserVO;
import com.ByteDance.Gotlin.im.util.Hutils.HLog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import cn.bingoogolapple.androidcommon.adapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;
import cn.bingoogolapple.badgeview.BGABadgeRelativeLayout;
import cn.bingoogolapple.badgeview.BGABadgeTextView;
import cn.bingoogolapple.badgeview.BGABadgeView;
import cn.bingoogolapple.badgeview.BGABadgeable;
import cn.bingoogolapple.badgeview.BGADragDismissDelegate;

/**
 * @Author Zhicong Deng
 * @Date 2022/6/14 15:34
 * @Email 1520483847@qq.com
 * @Description 消息列表界面适配器
 */

public class UserMsgBGAAdapter extends BGARecyclerViewAdapter<MessageList> {

    public UserMsgBGAAdapter(RecyclerView recyclerView) {
        super(recyclerView);
    }

    private RedPointListener mRedPointInterface;

    public void setRedPonitInterface(RedPointListener redPointInterface) {
        this.mRedPointInterface = redPointInterface;
    }

    RoundedCorners roundedCorners = new RoundedCorners(8);// 数字为圆角度数
    RequestOptions options = new RequestOptions()
            .transforms(new CenterCrop(), roundedCorners)
            .diskCacheStrategy(DiskCacheStrategy.NONE)// 不做磁盘缓存
            .skipMemoryCache(true);// 不做内存缓存

    private static int DEFAULT_IMG = R.drawable.ic_img_default;

    @Override
    protected void fillData(BGAViewHolderHelper helper, int position, MessageList model) {
        SessionVO session = model.getSession();
        UserVO sender = model.getSender();
        String content = model.getContent();

        String type = session.getType() == 1 ? "群聊会话" : "好友会话";

        Integer sessionId = session.getSessionId();
        String SessionName = session.getName();
        String sessionAvatar = session.getAvatar();
        String senderNickName = sender.getNickName();
        int badgeNum = session.getBadgeNum();

        if (model.getSelf()) {
            senderNickName = "我";
        }

        helper.setText(R.id.bga_tv_session_name, "[" + type + sessionId + "] " + SessionName);
        helper.setText(R.id.bga_tv_time, model.getSendTime());
        helper.setText(R.id.bga_tv_user_msg, senderNickName + ": " + content);

        ImageView avatar = helper.getView(R.id.bga_img_user_pic);
        Glide.with(mContext)
                .load(sessionAvatar == null ? DEFAULT_IMG : BASE_URL + sessionAvatar)
                .apply(options)
                .into(avatar);


        // 小红点
        BGABadgeView RedBadge = helper.getView(R.id.bga_red_point);
        if (badgeNum == 0) {
            RedBadge.hiddenBadge();
        } else {
            RedBadge.showTextBadge(String.valueOf(session.getBadgeNum()));
        }
        RedBadge.setDragDismissDelegage(new BGADragDismissDelegate() {
            @Override
            public void onDismiss(BGABadgeable badgeable) {
                if (mRedPointInterface != null) {
                    mRedPointInterface.onDragDismiss(badgeable, position);
                }
            }
        });

        // item点击
        BGABadgeRelativeLayout layout = (BGABadgeRelativeLayout) helper.getConvertView();
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRedPointInterface.onClick(view, position, RedBadge);
            }
        });

    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.d_item_user_info_message;
    }
}
