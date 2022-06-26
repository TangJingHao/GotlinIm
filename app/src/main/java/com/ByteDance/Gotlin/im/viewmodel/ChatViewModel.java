package com.ByteDance.Gotlin.im.viewmodel;

import static com.ByteDance.Gotlin.im.util.Constants.MESSAGE_IMG;
import static com.ByteDance.Gotlin.im.util.Constants.MESSAGE_TEXT;
import static com.ByteDance.Gotlin.im.util.Constants.SEND_MESSAGE;
import static com.ByteDance.Gotlin.im.util.Constants.WS_SEND_MESSAGE;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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
import com.ByteDance.Gotlin.im.util.Hutils.HLog;
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
@RequiresApi(api = Build.VERSION_CODES.Q)
public class ChatViewModel extends ViewModel {

    private final static int LIST_BOTTOM = -1;
    private final static int LIST_TOP = 0;
    private final MutableLiveData<LinkedList<MessageVO>> messages;
    private final SessionVO session;
    private final ChatListAdapter adapter;
    private final Repository re = Repository.INSTANCE;
    private final WebSocket webSocket;
    private final UserVO user;
    private int page = 0;

    ChatViewModel(SessionVO session) {
        this.session = session;
        SocketListener listener = new SocketListener();
        //初始化webSocket
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
                re.getUserAvatar(),
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
                    if (list == null) return;
                    if (list.size() > 0) {
                        MessageVO[] messageVOS = new MessageVO[list.size()];
                        int count = list.size() - 1;
                        for (MessageVO h : list) {
                            messageVOS[count--] = h;
                        }
                        addMsg(messageVOS, LIST_TOP);
                    }
                }
            }
        });
    }

    /**
     * 接收text消息
     */
    private void receivedText(WebSocketReceiveChatMsg msg) {
        HLog.i(msg.getWsContent().toString());
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
                re.getUserId(), MESSAGE_TEXT, msg));
        ThreadManager.getDefFixThreadPool().execute(() -> webSocket.send(gson.toJson(sendChatMsg)));
        MessageVO[] messageVOS = {ws2Message(sendChatMsg, true)};
        addMsg(messageVOS, LIST_BOTTOM);
    }

    /**
     * 接收IMG消息
     */
    private void receivedImg(WebSocketReceiveChatMsg msg) {
        //TODO:接收图片
    }

    /**
     * 发送IMG消息
     *
     * @param path 图片路径
     */
    public void sendImg(String path) {
        //TODO:发送图片
        HLog.i(path);
        WebSocketSendChatMsg sendChatMsg = new WebSocketSendChatMsg(
                SEND_MESSAGE, new WSsendContent(session.getSessionId(),
                user.getUserId(), MESSAGE_IMG, path));

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
                c.getContent(), "xxxx-xx-xx xx:xx:xx", self);
    }

    /**
     * webSocket监听类
     */
    class SocketListener extends WebSocketListener {
        Gson gson = new Gson();

        @Override
        public void onOpen(@NonNull WebSocket webSocket, @NonNull Response response) {
            HLog.i("[开启链接]");
        }

        @Override
        public void onMessage(@NonNull WebSocket webSocket, @NonNull String text) {
            HLog.i("[回调] " + text);
            WebSocketReceiveChatMsg msg = gson.fromJson(text, WebSocketReceiveChatMsg.class);
            ThreadManager.getDefFixThreadPool().execute(() -> {
                int type = msg.getWsContent().getType();
                if (type == MESSAGE_TEXT) {
                    receivedText(msg);
                } else if (type == MESSAGE_IMG) {
                    //TODO:处理图片信息

                }
            });
        }

        @Override
        public void onMessage(@NonNull WebSocket webSocket, @NonNull ByteString bytes) {
            HLog.i("[回调] " + bytes);
        }

        @Override
        public void onClosing(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
            HLog.i("[链接关闭中] " + reason);
        }

        @Override
        public void onClosed(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
            HLog.i("[链接已关闭] " + reason);
        }

        @Override
        public void onFailure(@NonNull WebSocket webSocket, @NonNull Throwable t, Response response) {
            HLog.i("[链接关闭]" + (response != null ? response.toString() : null));
        }
    }
}
