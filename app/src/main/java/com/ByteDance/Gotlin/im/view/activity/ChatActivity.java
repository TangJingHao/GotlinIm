package com.ByteDance.Gotlin.im.view.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;


import com.ByteDance.Gotlin.im.databinding.DIncludeMyToolbarBinding;
import com.ByteDance.Gotlin.im.databinding.HActivityChatBinding;
import com.ByteDance.Gotlin.im.viewmodel.ChatViewModel;

/**
 * @author: Hx
 * @date: 2022年06月11日 19:31
 */
public class ChatActivity extends AppCompatActivity {

    static final String TAG = "TAG_ChatActivity";
    HActivityChatBinding view = null;
    DIncludeMyToolbarBinding toolbar;
    EditText input;
    TextView send;
    ChatViewModel model;

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
        model = new ViewModelProvider(this).get(ChatViewModel.class);

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
        input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                send();
                return true;
            }
        });

        send.setOnClickListener(view -> {
            send();
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
}
