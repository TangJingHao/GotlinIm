package com.ByteDance.Gotlin.im.view.activity;

import static com.ByteDance.Gotlin.im.info.Message.FRIENDS;
import static com.ByteDance.Gotlin.im.info.Message.TYPE_TEXT;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ByteDance.Gotlin.im.R;
import com.ByteDance.Gotlin.im.databinding.DIncludeMyToolbarBinding;
import com.ByteDance.Gotlin.im.databinding.HActivityChatBinding;
import com.ByteDance.Gotlin.im.databinding.HMessageItemBinding;
import com.ByteDance.Gotlin.im.info.Message;
import com.ByteDance.Gotlin.im.viewmodel.ChatViewModel;

import java.util.ArrayList;
import java.util.Random;

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

        String[] strings = {"轻轻敲碎沉睡的心灵", "慢慢睁开你地眼睛",
                "看看这世界是否依然", "忙碌的转个不停", "春风不解风情", "吹动少年的心"};

        ArrayList<Message> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(new Message(strings[new Random().nextInt(5)],
                    TYPE_TEXT, new Random().nextInt(1)));
        }
        ChatListAdapter adapter = new ChatListAdapter(list);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.VERTICAL);
        manager.setSmoothScrollbarEnabled(true);
        chatList.setLayoutManager(manager);
        chatList.setAdapter(adapter);

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

class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {

    ArrayList<Message> list;

    public ChatListAdapter(ArrayList<Message> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.h_message_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HMessageItemBinding binding = holder.binding;
        Message message = list.get(position);
        if (message.from == FRIENDS) {
            binding.left.setVisibility(View.VISIBLE);
            binding.msgLeft.setText(message.content);
        } else {
            binding.right.setVisibility(View.VISIBLE);
            binding.msgRight.setText(message.content);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        HMessageItemBinding binding;

        public ViewHolder(View v) {
            super(v);
            binding = HMessageItemBinding.bind(v);
        }
    }
}
