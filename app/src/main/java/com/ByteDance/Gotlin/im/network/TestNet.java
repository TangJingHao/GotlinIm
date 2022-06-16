package com.ByteDance.Gotlin.im.network;

import android.app.DownloadManager;
import android.os.Message;

import androidx.annotation.Nullable;

import com.ByteDance.Gotlin.im.util.Constants;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 * @Author Zhicong Deng
 * @Date 2022/6/15 20:04
 * @Email 1520483847@qq.com
 * @Description
 */
public class TestNet {

    private void webSocketConnect() {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(Constants.BASE_URL).build();
        mOkHttpClient.dispatcher().executorService().shutdown();

    }

    private WebSocket mWebSocket;

    public class EchoWebSocketListener extends WebSocketListener {

        @Override
        public void onOpen(WebSocket webSocket, Response response) {

            webSocket.send("hello world");
            webSocket.send("welcome");
            webSocket.send(ByteString.decodeHex("adef"));
            webSocket.close(1000, "再见");
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {

        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {

        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            webSocket.close(1000, null);

        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {

        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {

        }
    }

}


