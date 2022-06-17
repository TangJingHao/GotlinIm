package com.ByteDance.Gotlin.im.util.Hutils;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.ByteDance.Gotlin.im.info.vo.MessageVO;

import java.util.ArrayList;

/**
 * @author: Hx
 * @date: 2022年06月13日 19:45
 */
public class DifferCallback extends DiffUtil.Callback {

    private ArrayList<MessageVO> oldData;
    private ArrayList<MessageVO> newData;

    public DifferCallback(ArrayList<MessageVO> oldData, ArrayList<MessageVO> newData) {
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
        return true;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        MessageVO o = oldData.get(oldItemPosition);
        MessageVO n = newData.get(newItemPosition);
        if (!o.getSession().equals(n.getSession())) return false;
        if (!o.getSender().equals(n.getSender())) return false;
        if (o.getType() != n.getType()) return false;
        if (!o.getContent().equals(n.getContent())) return false;
        if (!o.getSendTime().equals(n.getSendTime())) ;
        return o.getSelf() == n.getSelf();
    }
}
