package com.ByteDance.Gotlin.im.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ByteDance.Gotlin.im.R;
import com.ByteDance.Gotlin.im.databinding.HMessageItemBinding;
import com.ByteDance.Gotlin.im.info.vo.MessageVO;
import com.ByteDance.Gotlin.im.info.vo.UserVO;

import java.util.ArrayList;

/**
 * @author: Hx
 * @date: 2022年06月13日 19:34
 */
public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {

    private ArrayList<MessageVO> list;

    public ChatListAdapter(ArrayList<MessageVO> list) {
        this.list = list;
    }

    /**
     * 返回数据
     *
     * @return list
     */
    public ArrayList<MessageVO> getData() {
        return list;
    }

    /**
     * 设置数据
     *
     * @param list
     */
    public void setList(ArrayList<MessageVO> list) {
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
        if (message.getSelf()) {
            binding.right.setVisibility(View.VISIBLE);
            binding.left.setVisibility(View.GONE);
            binding.msgRight.setText(message.getContent());
            binding.nameRight.setText(userVO.getNickName());
        } else {
            binding.left.setVisibility(View.VISIBLE);
            binding.right.setVisibility(View.GONE);
            binding.msgLeft.setText(message.getContent());
            binding.nameLeft.setText(userVO.getNickName());
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
