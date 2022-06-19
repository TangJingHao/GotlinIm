package com.ByteDance.Gotlin.im.adapter;

import static com.ByteDance.Gotlin.im.util.Constants.BASE_URL;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ByteDance.Gotlin.im.R;
import com.ByteDance.Gotlin.im.application.BaseApp;
import com.ByteDance.Gotlin.im.databinding.HMessageItemBinding;
import com.ByteDance.Gotlin.im.info.vo.MessageVO;
import com.ByteDance.Gotlin.im.info.vo.UserVO;
import com.ByteDance.Gotlin.im.util.Constants;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.LinkedList;

/**
 * @author: Hx
 * @date: 2022年06月13日 19:34
 */
public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {

    private final Context context;
    private RequestOptions options = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.NONE)//不做磁盘缓存
            .skipMemoryCache(true);//不做内存缓存
    private LinkedList<MessageVO> list;

    public ChatListAdapter(LinkedList<MessageVO> list) {
        this.list = list;
        context = BaseApp.Companion.getContext();
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
     * @param list
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
        ;

        if (message.getSelf()) {
            binding.right.setVisibility(View.VISIBLE);
            binding.left.setVisibility(View.GONE);
            binding.msgRight.setText(message.getContent());
            binding.nameRight.setText(userVO.getNickName());
            Glide.with(context)
                    .load(avatar == null ? Constants.USER_DEFAULT_AVATAR : BASE_URL + avatar)
                    .apply(options)
                    .into(binding.headerRight.img);
        } else {
            binding.left.setVisibility(View.VISIBLE);
            binding.right.setVisibility(View.GONE);
            binding.msgLeft.setText(message.getContent());
            binding.nameLeft.setText(userVO.getNickName());
            Glide.with(context)
                    .load(avatar == null ? Constants.USER_DEFAULT_AVATAR : BASE_URL + avatar)
                    .apply(options)
                    .into(binding.headerLeft.img);
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        HMessageItemBinding binding;

        public ViewHolder(View v) {
            super(v);
            binding = HMessageItemBinding.bind(v);
        }
    }

}
