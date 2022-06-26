package com.ByteDance.Gotlin.im.adapter;

import static com.ByteDance.Gotlin.im.util.Constants.BASE_URL;
import static com.ByteDance.Gotlin.im.util.Constants.DEFAULT_IMG;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.ByteDance.Gotlin.im.R;
import com.ByteDance.Gotlin.im.application.BaseApp;
import com.ByteDance.Gotlin.im.databinding.HMessageItemBinding;
import com.ByteDance.Gotlin.im.info.vo.MessageVO;
import com.ByteDance.Gotlin.im.info.vo.UserVO;
import com.ByteDance.Gotlin.im.util.Constants;
import com.ByteDance.Gotlin.im.util.Hutils.HLog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.LinkedList;

/**
 * @author: Hx
 * @date: 2022年06月13日 19:34
 */
public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {

    private static final float IMAGE_H_SCALE = 0.3f;
    private static final float IMAGE_W_SCALE = 0.3f;
    private final Context context;
    private final RequestOptions options = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.NONE)//不做磁盘缓存
            .skipMemoryCache(true);//不做内存缓存
    private final int max_width;
    private final int max_height;
    private LinkedList<MessageVO> list;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public ChatListAdapter(LinkedList<MessageVO> list) {
        this.list = list;
        context = BaseApp.Companion.getContext();
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        max_width = (int) (dm.widthPixels * IMAGE_W_SCALE);
        max_height = (int) (dm.heightPixels * IMAGE_H_SCALE);
        HLog.d("屏幕长度" + dm.heightPixels + "屏幕宽度" + dm.widthPixels);
        HLog.i("图片最大长度:" + max_height + "图片最大宽度" + max_width);
    }

    /**
     * 返回数据
     *
     * @return list
     */
    public LinkedList<MessageVO> getData() {
        return list;
    }

    /**
     * 设置数据
     *
     * @param list 聊天数据
     */
    public void setList(LinkedList<MessageVO> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ChatListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.h_message_item, parent, false);
        return new ChatListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListAdapter.ViewHolder holder, int position) {
        HMessageItemBinding binding = holder.binding;
        MessageVO message = list.get(position);
        UserVO userVO = message.getSender();
        String avatar = userVO.getAvatar();
        String content = message.getContent();
        String avatarImg = avatar == null ? String.valueOf(DEFAULT_IMG) : BASE_URL + avatar;
        String contentImg = content.contains("content") ? content : BASE_URL + content;

        if (message.getSelf()) {
            binding.right.setVisibility(View.VISIBLE);
            binding.left.setVisibility(View.GONE);
            binding.nameRight.setText(userVO.getNickName());
            loadPic(avatarImg, binding.headerRight.img);

            if (message.getType() == Constants.MESSAGE_TEXT) {
                binding.msgRight.setText(message.getContent());
            } else {
                binding.msgRight.setVisibility(View.GONE);
                binding.imgRight.setVisibility(View.VISIBLE);
                loadImg(contentImg, binding.imgRight);
            }
        } else {
            binding.left.setVisibility(View.VISIBLE);
            binding.right.setVisibility(View.GONE);
            binding.nameLeft.setText(userVO.getNickName());
            loadPic(avatarImg, binding.headerLeft.img);

            if (message.getType() == Constants.MESSAGE_TEXT) {
                binding.msgLeft.setText(message.getContent());
            } else {
                binding.msgLeft.setVisibility(View.GONE);
                binding.imgLeft.setVisibility(View.VISIBLE);
                loadImg(contentImg, binding.imgLeft);
            }
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    /**
     * 装载图片
     *
     * @param path 图片路径
     * @param v    装入容器
     */
    private void loadPic(String path, ImageView v) {
        Glide.with(context)
                .load(path)
                .apply(options)
                .into(v);
    }

    /**
     * 自适应加载图片
     *
     * @param path 图片路径
     * @param v    目标
     */
    private void loadImg(String path, ImageView v) {
        CustomTarget<Drawable> target = new CustomTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                ViewGroup.LayoutParams params = v.getLayoutParams();
                int height = resource.getIntrinsicHeight();
                int width = resource.getIntrinsicWidth();
                HLog.d("图片高:" + height + "图片宽:" + width);

                if (width < max_width && height < max_height) {
                    params.height = height;
                    params.width = width;
                } else if (width >= height) {
                    params.width = max_width;
                    params.height = height * max_width / width;
                } else {
                    params.height = max_height;
                    params.width = width * max_height / height;
                }

                v.setLayoutParams(params);
                v.setImageDrawable(resource);
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {

            }
        };
        Glide.with(context).load(path).into(target);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        HMessageItemBinding binding;

        public ViewHolder(View v) {
            super(v);
            binding = HMessageItemBinding.bind(v);
        }
    }
}
