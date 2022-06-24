package com.ByteDance.Gotlin.im.viewmodel;

import static com.ByteDance.Gotlin.im.util.Constants.MESSAGE_TEXT;
import static com.ByteDance.Gotlin.im.util.Constants.SEND_MESSAGE;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ByteDance.Gotlin.im.Repository;
import com.ByteDance.Gotlin.im.adapter.ChatListAdapter;
import com.ByteDance.Gotlin.im.application.ThreadManager;
import com.ByteDance.Gotlin.im.info.SessionHistoryDataResponse;
import com.ByteDance.Gotlin.im.info.WSreceiveContent;
import com.ByteDance.Gotlin.im.info.WSsendContent;
import com.ByteDance.Gotlin.im.info.WebSocketReceiveChatMsg;
import com.ByteDance.Gotlin.im.info.WebSocketSendChatMsg;
import com.ByteDance.Gotlin.im.info.vo.MessageVO;
import com.ByteDance.Gotlin.im.info.vo.SessionVO;
import com.ByteDance.Gotlin.im.info.vo.UserVO;
import com.ByteDance.Gotlin.im.network.netImpl.NetWork;
import com.ByteDance.Gotlin.im.util.DUtils.DLogUtils;
import com.google.gson.Gson;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 * @author: Hx
 * @date: 2022年06月11日 21:54
 */
public class ChatViewModel extends ViewModel {

    private final static int LIST_BOTTOM = -1;
    private final static int LIST_TOP = 0;
    private final static String TAG = "ChatViewModel";
    private final MutableLiveData<LinkedList<MessageVO>> messages;
    private final SessionVO session;
    private final ChatListAdapter adapter;
    private final Repository re = Repository.INSTANCE;
    private WebSocket webSocket;
    private int page = 0;
    private UserVO user;

    ChatViewModel(SessionVO session) {
        this.session = session;
        SocketListener listener = new SocketListener();
//        //初始化webSocket
        webSocket = Repository.INSTANCE.getWebSocketAndConnect(listener);
        LinkedList<MessageVO> list = new LinkedList<>();
        adapter = new ChatListAdapter(list);
        messages = new MutableLiveData<>();
        //填充User
        user = new UserVO(
                re.getUserId(),
                re.getUserName(),
                re.getUserSex(),
                re.getUsernickName(),
                re.getUserEmail(),
                re.getUserAvatar() + "",
                true);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (webSocket != null)
            webSocket.cancel();
    }

    /**
     * 返回消息列表的适配器
     *
     * @return ChatListAdapter
     */
    public ChatListAdapter getAdapter() {
        return adapter;
    }

    /**
     * 提供聊天列表刷新监听
     */
    public LiveData<LinkedList<MessageVO>> updateMsgList() {
        return messages;
    }

    /**
     * 刷新聊天列表，显示更多聊天记录
     */
    public void refresh() {
        //从数据库加载聊天记录
        NetWork.INSTANCE.getSessionHistoryList(re.getUserId(), session.getSessionId(), page++, new Continuation<SessionHistoryDataResponse>() {
            @NonNull
            @Override
            public CoroutineContext getContext() {
                return EmptyCoroutineContext.INSTANCE;
            }

            @Override
            public void resumeWith(@NonNull Object o) {
                if (o instanceof SessionHistoryDataResponse) {
                    SessionHistoryDataResponse his = (SessionHistoryDataResponse) o;
                    List<MessageVO> list = his.getData().getHistoryList();
                    //将获得的数据转换为MessageVO，并向消息列表Top位置插入
                    MessageVO[] messageVOS = new MessageVO[list.size()];
                    int count = 0;
                    for (MessageVO h : list) {
                        messageVOS[count++] = new MessageVO(
                                h.getSession(), h.getSender(),
                                h.getType(), h.getContent(),
                                h.getSendTime(), h.getSelf());
                    }
                    addMsg(messageVOS, LIST_TOP);
                }
            }
        });
    }

    /**
     * 接收text消息
     */
    private void receivedText(WebSocketReceiveChatMsg msg) {
        WSreceiveContent c = msg.getWsContent();
        MessageVO message = new MessageVO(c.getSession(),
                c.getSender(), c.getType(), c.getContent(),
                c.getSendTime(), c.getSelf());
        MessageVO[] messageVOS = {message};
        addMsg(messageVOS, LIST_BOTTOM);
    }

    /**
     * 发送Text消息
     *
     * @param msg 消息
     */
    public void sendText(String msg) {
        //打包发送消息
        Gson gson = new Gson();
        WebSocketSendChatMsg sendChatMsg = new WebSocketSendChatMsg(
                SEND_MESSAGE, new WSsendContent(session.getSessionId(),
                re.getUserId(), 0, msg));
        webSocket.send(gson.toJson(sendChatMsg));
        MessageVO[] messageVOS = {ws2Message(sendChatMsg, true)};
        addMsg(messageVOS, LIST_BOTTOM);
    }

    /**
     * 往消息列表中添加一条记录
     */
    private void addMsg(MessageVO[] messageVOS, int pos) {
        LinkedList<MessageVO> oldData = messages.getValue();
        LinkedList<MessageVO> newData;
        if (oldData != null)
            newData = new LinkedList<>(oldData);
        else
            newData = new LinkedList<>();

        //向新数据中插入
        if (pos == LIST_BOTTOM) {
            Collections.addAll(newData, messageVOS);
        } else if (pos == LIST_TOP) {
            for (MessageVO msg : messageVOS) {
                newData.addFirst(msg);
            }
        }
        //设置新数据
        messages.postValue(newData);
    }

    /**
     * 将发送的数据转化为MessageVO,用于装进聊天界面
     */
    private MessageVO ws2Message(WebSocketSendChatMsg ws, boolean self) {

        WSsendContent c = ws.getWsContent();
        return new MessageVO(session, user, c.getType(),
                c.getContent(), "", self);
    }

    /**
     * webSocket监听类
     */
    class SocketListener extends WebSocketListener {
        Gson gson = new Gson();

        @Override
        public void onOpen(WebSocket webSocket, @NonNull Response response) {
            DLogUtils.i(TAG, "开启链接");
            WebSocketSendChatMsg sendChatMsg = new WebSocketSendChatMsg(
                    SEND_MESSAGE, new WSsendContent(session.getSessionId(), re.getUserId(),
                    0, "开始聊天吧"));
            webSocket.send(gson.toJson(sendChatMsg));
        }

        // 回调,展示消息
        @Override
        public void onMessage(@NonNull WebSocket webSocket, @NonNull String text) {
            DLogUtils.i(TAG, "回调" + text);
            WebSocketReceiveChatMsg msg = gson.fromJson(text, WebSocketReceiveChatMsg.class);
            if (msg.getWsContent().getType() == MESSAGE_TEXT) {

            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    receivedText(msg);
                }
            }).start();
        }

        // 回调
        @Override
        public void onMessage(@NonNull WebSocket webSocket, @NonNull ByteString bytes) {
            DLogUtils.i(TAG, "回调" + bytes);
        }

        @Override
        public void onClosing(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
            DLogUtils.i(TAG, "链接关闭中");
        }

        @Override
        public void onClosed(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
            DLogUtils.i(TAG, "链接已关闭");
        }

        @Override
        public void onFailure(@NonNull WebSocket webSocket, @NonNull Throwable t, Response response) {
            DLogUtils.i(TAG, "链接失败/发送失败" + t);
        }
    }
}
