package com.ByteDance.Gotlin.im.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ByteDance.Gotlin.im.Repository;
import com.ByteDance.Gotlin.im.info.WSreceiveContent;
import com.ByteDance.Gotlin.im.info.vo.MessageVO;
import com.ByteDance.Gotlin.im.info.vo.SessionVO;
import com.ByteDance.Gotlin.im.info.vo.UserVO;

import java.util.ArrayList;

/**
 * @author: Hx
 * @date: 2022年06月11日 21:54
 */
public class ChatViewModel extends ViewModel {

    private final Repository repository = Repository.INSTANCE;
    private final MutableLiveData<ArrayList<MessageVO>> messages = new MutableLiveData<>();
    private final int sessionId;

    ChatViewModel(int sessionId) {
        this.sessionId = sessionId;
    }

    /**
     * 提供聊天列表刷新监听
     */
    public LiveData<ArrayList<MessageVO>> refreshMsgList() {
        return messages;
    }

    /**
     * 刷新聊天列表，显示更多聊天记录
     */
    public void refresh(ArrayList<MessageVO> oldData, WSreceiveContent content) {
        MessageVO messageVO = new MessageVO(content.getSession(), content.getSender(),
                content.getType(), content.getContent(), content.getSendTime(), content.getSelf());
        oldData.add(messageVO);
        ArrayList<MessageVO> newData = new ArrayList<>(oldData);
        messages.setValue(newData);
    }

    /**
     * 发送消息
     *
     * @param msg 消息
     */
    public void send(ArrayList<MessageVO> oldData, String msg) {
        //:TODO 打包发送消息
        //暂时测试用
        UserVO user = new UserVO(1, "", "me", "", true);
        SessionVO sessionVO = new SessionVO(sessionId, 0, "", "", 0, 0, 0);
        MessageVO message = new MessageVO(sessionVO, user, 0, msg, String.valueOf(System.currentTimeMillis()), true);
        oldData.add(message);
        ArrayList<MessageVO> newData = new ArrayList<>(oldData);
        messages.setValue(newData);
    }
}
