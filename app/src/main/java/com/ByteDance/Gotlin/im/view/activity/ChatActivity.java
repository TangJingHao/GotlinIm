package com.ByteDance.Gotlin.im.view.activity;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.ByteDance.Gotlin.im.util.Constants.SEND_MESSAGE;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ByteDance.Gotlin.im.Repository;
import com.ByteDance.Gotlin.im.adapter.ChatListAdapter;
import com.ByteDance.Gotlin.im.databinding.DIncludeMyToolbarBinding;
import com.ByteDance.Gotlin.im.databinding.HActivityChatBinding;
import com.ByteDance.Gotlin.im.info.WSsendContent;
import com.ByteDance.Gotlin.im.info.WebSocketReceiveChatMsg;
import com.ByteDance.Gotlin.im.info.WebSocketSendChatMsg;
import com.ByteDance.Gotlin.im.info.vo.MessageVO;
import com.ByteDance.Gotlin.im.util.DUtils.DLogUtils;
import com.ByteDance.Gotlin.im.util.Hutils.DifferCallback;
import com.ByteDance.Gotlin.im.viewmodel.ChatViewModel;
import com.ByteDance.Gotlin.im.viewmodel.ChatViewModelFactory;
import com.google.gson.Gson;

import java.util.ArrayList;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 * @author: Hx
 * @date: 2022年06月11日 19:31
 */
public class ChatActivity extends AppCompatActivity {

    //聊天类型[1单人/2群聊]
    private static final int TYPE_SINGLE = 1;
    private static final int TYPE_GROUPS = 2;

    private static final String TAG = "TAG_ChatActivity";
    private static int sessionId;
    WebSocket webSocket;
    Gson gson = new Gson();
    private HActivityChatBinding view;
    private DIncludeMyToolbarBinding toolbar;
    private EditText input;
    private TextView send;
    private ChatViewModel model;
    private ImageButton back;
    private RecyclerView chatList;
    private SwipeRefreshLayout refresh;
    private ArrayList<MessageVO> list;
    private ChatListAdapter adapter;
    private String sessinoName;

    public static void startChat(Context context, int sessionId, String sessinoName) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("sessionId", sessionId);
        intent.putExtra("sessinoName", sessinoName);
        context.startActivity(intent);
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
    }

    public static int getSessionId() {
        return sessionId;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = HActivityChatBinding.inflate(getLayoutInflater());
        setContentView(view.getRoot());
        sessionId = getIntent().getIntExtra("sessionId", 0);
        sessinoName = getIntent().getStringExtra("sessinoName");

        bind();
        func();
        initUiData();
        connect();
    }

    /**
     * 初始化ui数据
     */
    private void initUiData() {
        toolbar.title.setText(sessinoName);
    }

    /**
     * 绑定ui
     */
    private void bind() {
        toolbar = view.toolbar;
        toolbar.imgMore.setVisibility(View.VISIBLE);
        input = view.input;
        send = view.send;
        back = toolbar.imgChevronLeft;
        chatList = view.chatList;
        refresh = view.refresh;
        model = new ViewModelProvider(this, new ChatViewModelFactory()).get(ChatViewModel.class);
        list = new ArrayList<>();
        adapter = new ChatListAdapter(list);

    }

    private void connect() {
        SocketListener listener = new SocketListener();
        webSocket = Repository.INSTANCE.getWebSocketAndConnect(listener);
    }

    /**
     * 初始化功能
     */
    private void func() {
        //刷新监听
        model.refreshMsgList().observe(this, new Observer<ArrayList<MessageVO>>() {
            @Override
            public void onChanged(ArrayList<MessageVO> messages) {
                ArrayList<MessageVO> list = adapter.getData();
                adapter.setList(messages);
                DiffUtil.DiffResult diffResult
                        = DiffUtil.calculateDiff(new DifferCallback(list, messages));
                diffResult.dispatchUpdatesTo(adapter);
//                chatList.scrollToPosition(0);
//                refresh.setRefreshing(false);
            }
        });

        //文本监测
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                send.setEnabled(isMsgValid(s.toString()));
            }
        });
        //回车监测
        input.setOnEditorActionListener((v, actionId, event) -> {
            send();
            return true;
        });

        send.setOnClickListener(view -> send());
        back.setOnClickListener(view -> back());

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.VERTICAL);
        manager.setSmoothScrollbarEnabled(true);
        chatList.setLayoutManager(manager);
        chatList.setAdapter(adapter);

        //刷新
        refresh.setOnRefreshListener(() -> {
//            model.refresh(adapter.getData());
            refresh.setRefreshing(false);
        });
    }

    /**
     * 关闭软键盘
     */
    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        View v = getWindow().peekDecorView();
        if (null != v) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    /**
     * 检查输入是否合法
     *
     * @param msg 消息
     * @return true/false
     */
    private boolean isMsgValid(String msg) {
        return null != msg && !TextUtils.isEmpty(msg) && msg.trim().length() > 0;
    }

    /**
     * 发送消息
     */
    private void send() {
        new Thread(() -> {
            WebSocketSendChatMsg sendChatMsg = new WebSocketSendChatMsg(
                    SEND_MESSAGE, new WSsendContent(6, 1, 0,
                    input.getText().toString()));
            webSocket.send(gson.toJson(sendChatMsg));
        }).start();

        model.send(adapter.getData(), input.getText().toString());
        input.clearFocus();
        input.setText("");
        hideKeyboard();
    }

    /**
     * 返回
     */
    private void back() {
        //TODO:返回时数据处理
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webSocket.cancel();
    }

    class SocketListener extends WebSocketListener {

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            DLogUtils.i(TAG, "链接开启");
            WebSocketSendChatMsg sendChatMsg = new WebSocketSendChatMsg(
                    SEND_MESSAGE, new WSsendContent(6, 1, 0, "开始聊天吧"));
            webSocket.send(gson.toJson(sendChatMsg));
        }

        // 回调,展示消息
        @Override
        public void onMessage(WebSocket webSocket, String text) {
            WebSocketReceiveChatMsg msg = gson.fromJson(text, WebSocketReceiveChatMsg.class);
            DLogUtils.i(TAG, "回调" + text);
            model.refresh(adapter.getData(), msg.getWsContent());
        }

        // 回调
        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            DLogUtils.i(TAG, "回调" + bytes);
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            DLogUtils.i(TAG, "链接关闭中");
        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            DLogUtils.i(TAG, "链接已关闭");
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
//            WebSocket webSocket1 = webSocket;
            DLogUtils.i(TAG, "链接失败/发送失败");
        }
    }
}


