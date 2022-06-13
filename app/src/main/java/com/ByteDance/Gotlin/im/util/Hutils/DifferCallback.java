package com.ByteDance.Gotlin.im.util.Hutils;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.ByteDance.Gotlin.im.info.Message;

import java.util.ArrayList;

/**
 * @author: Hx
 * @date: 2022年06月13日 19:45
 */
public class DifferCallback extends DiffUtil.Callback {

    private ArrayList<Message> oldData;
    private ArrayList<Message> newData;

    public DifferCallback(ArrayList<Message> oldData, ArrayList<Message> newData) {
        this.oldData = oldData;
        this.newData = newData;
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }

    @Override
    public int getOldListSize() {
        return oldData == null ? 0 : oldData.size();
    }

    @Override
    public int getNewListSize() {
        return newData == null ? 0 : newData.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
//        Message o = oldData.get(oldItemPosition);
//        Message n = newData.get(newItemPosition);
//        return o.time == n.time;
        return true;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Message o = oldData.get(oldItemPosition);
        Message n = newData.get(newItemPosition);
        if (o.from != n.from) return false;
        if (o.type != n.type) return false;
        if (o.time != n.time) return false;
        return o.content.equals(n.content);
    }
}
