package com.ByteDance.Gotlin.im.view.activity;

import static com.ByteDance.Gotlin.im.util.Hutils.StrUtils.isMsgValid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ByteDance.Gotlin.im.application.BaseApp;
import com.ByteDance.Gotlin.im.databinding.DIncludeMyToolbarBinding;
import com.ByteDance.Gotlin.im.databinding.HActivityChatBinding;
import com.ByteDance.Gotlin.im.info.vo.MessageVO;
import com.ByteDance.Gotlin.im.info.vo.SessionVO;
import com.ByteDance.Gotlin.im.util.Constants;
import com.ByteDance.Gotlin.im.util.Hutils.DifferCallback;
import com.ByteDance.Gotlin.im.util.Hutils.HLog;
import com.ByteDance.Gotlin.im.util.Tutils.TPictureSelectorUtil.TGlideEngine;
import com.ByteDance.Gotlin.im.viewmodel.ChatViewModel;
import com.ByteDance.Gotlin.im.viewmodel.ChatViewModelFactory;
import com.bumptech.glide.Glide;
import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.SelectMimeType;
import com.luck.picture.lib.config.SelectModeConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.interfaces.OnResultCallbackListener;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * @author: Hx
 * @date: 2022年06月11日 19:31
 */
public class ChatActivity extends AppCompatActivity {

    private static SessionVO session;
    private HActivityChatBinding view;
    private DIncludeMyToolbarBinding toolbar;
    private EditText input;
    private TextView send;
    private ChatViewModel model;
    private ImageButton back;
    private RecyclerView chatList;
    private SwipeRefreshLayout refresh;
    private ImageButton image;

    public static void startChat(Context context, SessionVO session) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("session", session);
        context.startActivity(intent);
    }

    public static SessionVO getSession() {
        return session;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = HActivityChatBinding.inflate(getLayoutInflater());
        setContentView(view.getRoot());
        session = (SessionVO) getIntent().getSerializableExtra("session");

        bind();
        func();
        initUi();
    }

    /**
     * 初始化ui数据
     */
    private void initUi() {
        toolbar.title.setText(session.getName());
        model.refresh();
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
        image = view.image;
        model = new ViewModelProvider(this, new ChatViewModelFactory()).get(ChatViewModel.class);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.VERTICAL);
        manager.setSmoothScrollbarEnabled(true);
        chatList.setLayoutManager(manager);
        chatList.setAdapter(model.getAdapter());
    }

    /**
     * 初始化功能
     */
    private void func() {
        //消息更新监听
        model.updateMsgList().observe(this, messages -> {

            LinkedList<MessageVO> old = model.getAdapter().getData();
            model.getAdapter().setList(messages);
            //刷新消息列表
            DiffUtil.DiffResult diffResult
                    = DiffUtil.calculateDiff(new DifferCallback(old, messages));
            diffResult.dispatchUpdatesTo(model.getAdapter());

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

        //发送消息
        send.setOnClickListener(view -> send());
        //返回
        back.setOnClickListener(view -> back());
        //打开图片选择器,发送照片
        image.setOnClickListener(view -> openImgSelector());
        //刷新
        refresh.setOnRefreshListener(() -> {
            model.refresh();
            refresh.setRefreshing(false);
        });

        //跳转到
        toolbar.imgMore.setOnClickListener(v -> {
            int chatType = session.getType();
            //跳转到群聊信息页面
            if (chatType == Constants.CHAT_GROUP) {
                //TODO 跳转群聊信息页面
            }
            //跳转到好友信息页面
            else if (chatType == Constants.CHAT_PRIVATE) {
                //TODO 跳转好友信息页面
            }
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
     * 发送消息
     */
    private void send() {
        model.sendText(input.getText().toString());
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

    /**
     * 启动照片选择器
     */
    private void openImgSelector() {
        PictureSelector.create(this)
                .openGallery(SelectMimeType.ofImage())
                .setImageEngine(TGlideEngine.createGlideEngine())
                .setSelectionMode(SelectModeConfig.SINGLE)
                .forResult(new OnResultCallbackListener<LocalMedia>() {
                    @Override
                    public void onResult(ArrayList<LocalMedia> result) {
                        String path = result.get(0).getPath();
                        //发送图片
                        model.sendImg(path);
                    }

                    @Override
                    public void onCancel() {
                        //ignore
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}


