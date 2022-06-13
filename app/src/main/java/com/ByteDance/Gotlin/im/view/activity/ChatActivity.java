package com.ByteDance.Gotlin.im.view.activity;

import static com.ByteDance.Gotlin.im.info.Message.TYPE_TEXT;

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

import com.ByteDance.Gotlin.im.adapter.ChatListAdapter;
import com.ByteDance.Gotlin.im.databinding.DIncludeMyToolbarBinding;
import com.ByteDance.Gotlin.im.databinding.HActivityChatBinding;
import com.ByteDance.Gotlin.im.info.Message;
import com.ByteDance.Gotlin.im.util.Hutils.DifferCallback;
import com.ByteDance.Gotlin.im.viewmodel.ChatViewModel;

import java.util.ArrayList;

/**
 * @author: Hx
 * @date: 2022年06月11日 19:31
 */
public class ChatActivity extends AppCompatActivity {

    //聊天类型[1单人/2群聊]
    private static final int TYPE_SINGLE = 1;
    private static final int TYPE_GROUPS = 2;

    private static final String TAG = "TAG_ChatActivity";
    private HActivityChatBinding view;
    private DIncludeMyToolbarBinding toolbar;
    private EditText input;
    private TextView send;
    private ChatViewModel model;
    private ImageButton back;
    private RecyclerView chatList;
    private SwipeRefreshLayout refresh;
    private ArrayList<Message> list = new ArrayList<>();
    private ChatListAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = HActivityChatBinding.inflate(getLayoutInflater());
        setContentView(view.getRoot());
        init();
    }

    private void init() {
        toolbar = view.toolbar;
        toolbar.imgMore.setVisibility(View.VISIBLE);
        input = view.input;
        send = view.send;
        back = toolbar.imgChevronLeft;
        model = new ViewModelProvider(this).get(ChatViewModel.class);
        chatList = view.chatList;
        refresh = view.refresh;

        //刷新监听
        model.refreshMsgList().observe(this, new Observer<ArrayList<Message>>() {
            @Override
            public void onChanged(ArrayList<Message> messages) {
                ArrayList<Message> list = adapter.getData();
                adapter.setList(messages);
                DiffUtil.DiffResult diffResult
                        = DiffUtil.calculateDiff(new DifferCallback(list, messages));
                diffResult.dispatchUpdatesTo(adapter);
                chatList.scrollToPosition(0);
                refresh.setRefreshing(false);
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

        for (int i = 0; i < 10; i++) {
            list.add(new Message(String.valueOf(10 - i),
                    TYPE_TEXT, i % 2, System.currentTimeMillis()));
        }

        adapter = new ChatListAdapter(list);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.VERTICAL);
        manager.setSmoothScrollbarEnabled(true);
        chatList.setLayoutManager(manager);
        chatList.setAdapter(adapter);

        //刷新
        refresh.setOnRefreshListener(() -> {
            model.refresh(adapter.getData());
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
        model.send(input.getText().toString());
        input.clearFocus();
        input.setText("");
        hideKeyboard();
    }

    /**
     * 返回
     */
    private void back() {
        //TODO:back
    }

}
