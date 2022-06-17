package com.ByteDance.Gotlin.im.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ByteDance.Gotlin.im.Repository;
import com.ByteDance.Gotlin.im.info.VO.MessageVO;

import java.util.ArrayList;

/**
 * @author: Hx
 * @date: 2022年06月11日 21:54
 */
public class ChatViewModel extends ViewModel {

    private final Repository repository = Repository.INSTANCE;
    private final MutableLiveData<ArrayList<MessageVO>> messages = new MutableLiveData<>();

    /**
     * 提供聊天列表刷新监听
     *
     */
    public LiveData<ArrayList<MessageVO>> refreshMsgList() {
        return messages;
    }

    /**
     * 刷新聊天列表，显示更多聊天记录
     */
    public void refresh(ArrayList<MessageVO> oldData) {
        ArrayList<MessageVO> newData = new ArrayList<>(oldData);
        for (int i = 0; i < 5; i++) {

        }
        messages.setValue(newData);
    }

    /**
     * 发送消息
     *
     * @param msg 消息
     */
    public void send(String msg) {
        //:TODO 打包发送消息
    }
}
