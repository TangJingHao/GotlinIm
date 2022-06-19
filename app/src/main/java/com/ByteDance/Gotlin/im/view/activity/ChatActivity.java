package com.ByteDance.Gotlin.im.view.activity;

import static com.ByteDance.Gotlin.im.util.Hutils.StrUtils.isMsgValid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ByteDance.Gotlin.im.databinding.DIncludeMyToolbarBinding;
import com.ByteDance.Gotlin.im.databinding.HActivityChatBinding;
import com.ByteDance.Gotlin.im.info.vo.MessageVO;
import com.ByteDance.Gotlin.im.util.Hutils.DifferCallback;
import com.ByteDance.Gotlin.im.viewmodel.ChatViewModel;
import com.ByteDance.Gotlin.im.viewmodel.ChatViewModelFactory;

import java.lang.ref.WeakReference;
import java.util.LinkedList;


/**
 * @author: Hx
 * @date: 2022年06月11日 19:31
 */
public class ChatActivity extends AppCompatActivity {

    private static final String TAG = "ChatActivity";
    private static int sessionId;
    private HActivityChatBinding viewBinding;
    private DIncludeMyToolbarBinding toolbar;
    private EditText input;
    private TextView send;
    private ChatViewModel viewModel;
    private ImageButton back;
    private RecyclerView rvChatList;
    private SwipeRefreshLayout refresh;
    private String sessionName;

    private MyHandler myHandler;

    public static void startChat(Context context, int sessionId, String sessionName) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("sessionId", sessionId);
        intent.putExtra("sessionName", sessionName);
        context.startActivity(intent);
    }

    public static int getSessionId() {
        return sessionId;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = HActivityChatBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());

        sessionId = getIntent().getIntExtra("sessionId", 0);
        sessionName = getIntent().getStringExtra("sessionName");

        if (myHandler == null) {
            myHandler = new MyHandler(ChatActivity.this);
        }

        bind();
        func();
        initUi();
    }

    /**
     * 绑定ui
     */
    private void bind() {
        toolbar = viewBinding.toolbar;
        toolbar.imgMore.setVisibility(View.VISIBLE);
        input = viewBinding.input;
        send = viewBinding.send;
        back = toolbar.imgChevronLeft;
        rvChatList = viewBinding.chatList;
        refresh = viewBinding.refresh;
        viewModel = new ViewModelProvider(this, new ChatViewModelFactory()).get(ChatViewModel.class);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.VERTICAL);
        manager.setSmoothScrollbarEnabled(true);

        rvChatList.setLayoutManager(manager);
        rvChatList.setAdapter(viewModel.getAdapter());
    }

    /**
     * 初始化功能
     */
    private void func() {
        //消息更新监听
        viewModel.updateMsgList().observe(this, messages -> {

            LinkedList<MessageVO> old = viewModel.getAdapter().getData();
            viewModel.getAdapter().setList(messages);
            //刷新消息列表
            DiffUtil.DiffResult diffResult
                    = DiffUtil.calculateDiff(new DifferCallback(old, messages));
            diffResult.dispatchUpdatesTo(viewModel.getAdapter());

            if (refresh.isRefreshing())
                refresh.setRefreshing(false);
        });

        //输入文本监测
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

        //回车键监测
        input.setOnEditorActionListener((v, actionId, event) -> {
            send();
            return true;
        });

        send.setOnClickListener(view -> send());
        back.setOnClickListener(view -> back());

        //刷新
        refresh.setOnRefreshListener(() -> {
            viewModel.refresh();
            refresh.setRefreshing(false);
        });
    }

    /**
     * 初始化ui数据
     */
    private void initUi() {
        toolbar.title.setText(sessionName);
        viewModel.refresh();
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
     * 发送消息
     */
    private void send() {
        viewModel.send(input.getText().toString());
        //清理输入框
        input.clearFocus();
        input.setText("");
        hideKeyboard();
    }

    /**
     * 返回
     */
    private void back() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static class MyHandler extends Handler {
        private final WeakReference<ChatActivity> content;

        public MyHandler(ChatActivity activity) {
            super(Looper.getMainLooper());
            this.content = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            ChatActivity chatActivity = content.get();
            if (chatActivity != null) {
                switch (msg.what) {
                    // 主线程更新UI

                }
            }
        }
    }
}


