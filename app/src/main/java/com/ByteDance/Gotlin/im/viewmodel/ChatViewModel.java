package com.ByteDance.Gotlin.im.viewmodel;

import static com.ByteDance.Gotlin.im.info.Message.TYPE_TEXT;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ByteDance.Gotlin.im.Repository;
import com.ByteDance.Gotlin.im.info.Message;

import java.util.ArrayList;

/**
 * @author: Hx
 * @date: 2022年06月11日 21:54
 */
public class ChatViewModel extends ViewModel {

    private final Repository repository = Repository.INSTANCE;
    private final MutableLiveData<ArrayList<Message>> messages = new MutableLiveData<>();

    /**
     * 提供聊天列表刷新监听
     *
     * @return
     */
    public LiveData<ArrayList<Message>> refreshMsgList() {
        return messages;
    }

    /**
     * 刷新聊天列表，显示更多聊天记录
     */
    public void refresh(ArrayList<Message> oldData) {
        ArrayList<Message> newData = new ArrayList<>(oldData);
        for (int i = 0; i < 5; i++) {
            newData.add(0, new Message(String.valueOf(newData.size() + 1),
                    TYPE_TEXT, i % 2, System.currentTimeMillis()));
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
