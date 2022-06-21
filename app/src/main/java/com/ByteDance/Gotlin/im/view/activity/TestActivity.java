package com.ByteDance.Gotlin.im.view.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.ByteDance.Gotlin.im.Repository;
import com.ByteDance.Gotlin.im.application.ThreadManager;
import com.ByteDance.Gotlin.im.databinding.DActivityTestBinding;
import com.ByteDance.Gotlin.im.info.WSsendContent;
import com.ByteDance.Gotlin.im.info.WebSocketReceiveChatMsg;
import com.ByteDance.Gotlin.im.info.WebSocketSendChatMsg;
import com.ByteDance.Gotlin.im.util.Constants;
import com.ByteDance.Gotlin.im.util.DUtils.DLogUtils;
import com.ByteDance.Gotlin.im.util.DUtils.diy.ConfirmPopupWindow;
import com.ByteDance.Gotlin.im.util.DUtils.diy.InputPopupWindow;
import com.ByteDance.Gotlin.im.util.DUtils.diy.PopupWindowListener;
import com.ByteDance.Gotlin.im.util.DUtils.diy.SingleSelectPopupWindow;
import com.ByteDance.Gotlin.im.util.Tutils.TPhoneUtil;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 * @Author Zhicong Deng
 * @Date 2022/6/12 14:13
 * @Email 1520483847@qq.com
 * @Description 测试用Activity
 */
public class TestActivity extends AppCompatActivity implements View.OnClickListener {

    private DActivityTestBinding b;
    private Context mContext;

    ConfirmPopupWindow confirmPopupWindow;
    InputPopupWindow inputPopupWindow;
    SingleSelectPopupWindow singleSelectPopupWindow;

    private static final String TAG = "TestActivity";

    private static final Repository repository = Repository.INSTANCE;

    private static final String BASE_WS_URL = "ws://chatspace.iceclean.top/space/ws/chat/";
    private static final String SEND_MESSAGE = "SEND_MESSAGE";
    private static final String USER_ONLINE = "USER_ONLINE";

    private static final String SEARCH_TYPE = "search_type";
    private static final int SEARCH_TYPE_FRIEND = 0;
    private static final int SEARCH_TYPE_GROUP_CHAT = 1;
    private static final int SEARCH_TYPE_MESSAGE = 2;

    WebSocket webSocket;

    Gson gson = new Gson();

    @SuppressLint("UnsafeOptInUsageError")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DActivityTestBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        mContext = this;

        b.testBar.imgChevronLeft.setVisibility(View.GONE);
        b.testBar.title.setText("测试页面");

        /*
         * websocket测试代码==========================================================================
         * */
        // 测试发送消息
        b.btnSend.setOnClickListener(new View.OnClickListener() {
            int count = 0;

            @Override
            public void onClick(View view) {
                // 注意线程
                ThreadManager.getDefFixThreadPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        DLogUtils.i("线程名" + Thread.currentThread().getName(),"onClick");
                        b.tvMe.setText("发送测试信息" + count);
                        WebSocketSendChatMsg sendChatMsg = new WebSocketSendChatMsg(
                                SEND_MESSAGE, new WSsendContent(6, 1, 0,
                                "发送测试信息" + count++));
                        webSocket.send(gson.toJson(sendChatMsg));
                    }
                });
            }
        });

        b.btnConnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EchoWebSocketListener listener = new EchoWebSocketListener();
                webSocket = Repository.INSTANCE.getWebSocketAndConnect(listener);
            }
        });

        b.btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webSocket.cancel();
            }
        });

        initPopupWindow();

        b.btnPopConfirm.setOnClickListener(this);
        b.btnPopInput.setOnClickListener(this);
        b.btnPopSelect.setOnClickListener(this);
        b.btnMain.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.equals(b.btnPopConfirm)) {
            confirmPopupWindow.show();
        } else if (view.equals(b.btnPopInput)) {
            inputPopupWindow.show();
        } else if (view.equals(b.btnPopSelect)) {
            singleSelectPopupWindow.show();
        } else if (view.equals(b.btnMain)) {
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    private void initPopupWindow() {
        PopupWindowListener popupWindowListener = new PopupWindowListener() {
            @Override
            public void onConfirm(String input) {
                TPhoneUtil.INSTANCE.showToast(TestActivity.this, "onConfirm:" + input);
            }

            @Override
            public void onCancel() {
                TPhoneUtil.INSTANCE.showToast(TestActivity.this, "onCancel:取消");
            }

            @Override
            public void onDismiss() {
                TPhoneUtil.INSTANCE.showToast(TestActivity.this, "onDismiss:关闭弹窗");
            }
        };

        // 新建弹窗
        confirmPopupWindow = new ConfirmPopupWindow(this, "测试", popupWindowListener);
        // （可选）设置按钮文本
        confirmPopupWindow.setConfirmText("确认文本");
        confirmPopupWindow.setCancelText("取消测试文本");
        // （可选）设置警告类型颜色模式
        confirmPopupWindow.setWarnTextColorType();

        inputPopupWindow = new InputPopupWindow(mContext, "输入弹窗测试", popupWindowListener);

        singleSelectPopupWindow = new SingleSelectPopupWindow(mContext, "单选弹窗测试",
                "选项一", "选项二", popupWindowListener);
    }

    /**
     * WebSocket链接测试
     */
    private void connect() {
        DLogUtils.i(TAG, "创建wedSocket");
        EchoWebSocketListener listener = new EchoWebSocketListener();
        Request request = new Request.Builder()
                .url(BASE_WS_URL + repository.getUserId())
                .build();
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(3, TimeUnit.SECONDS)
                .build();
        webSocket = client.newWebSocket(request, listener);
        client.dispatcher().executorService().shutdown();
        DLogUtils.i(TAG, "创建wedSocket完成");
    }

    class EchoWebSocketListener extends WebSocketListener {

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            DLogUtils.i(TAG, "链接开启");
            WebSocketSendChatMsg sendChatMsg = new WebSocketSendChatMsg(
                    SEND_MESSAGE, new WSsendContent(6, 1, 0, "开始聊天吧"));
            boolean b = webSocket.send(gson.toJson(sendChatMsg));
        }

        // 回调,展示消息
        @Override
        public void onMessage(WebSocket webSocket, String text) {
            WebSocketReceiveChatMsg msg = gson.fromJson(text, WebSocketReceiveChatMsg.class);
            DLogUtils.i(TAG, "回调" + text);
            b.tvOther.setText(msg.getWsContent().getSendTime() + " " + msg.getWsContent().getContent());
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
            DLogUtils.i(TAG, "链接失败/发送失败");
        }
    }
}
