package com.ByteDance.Gotlin.im.util.Hutils;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.ByteDance.Gotlin.im.info.vo.MessageVO;

import java.lang.reflect.Field;
import java.util.LinkedList;

/**
 * @author: Hx
 * @date: 2022年06月13日 19:45
 */
public class DifferCallback extends DiffUtil.Callback {

    private LinkedList<MessageVO> oldData;
    private LinkedList<MessageVO> newData;

    public DifferCallback(LinkedList<MessageVO> oldData, LinkedList<MessageVO> newData) {
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
        Field[] fc1 = oldData.get(oldItemPosition).getClass().getDeclaredFields();
        Field[] fc2 = newData.get(newItemPosition).getClass().getDeclaredFields();

        for (int i = 0; i < fc1.length; i++) {
            try {
                fc1[i].setAccessible(true);
                fc2[i].setAccessible(true);
                if (!fc1[i].get(o).toString().equals(fc2[i].get(n).toString())) {
                    return false;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
