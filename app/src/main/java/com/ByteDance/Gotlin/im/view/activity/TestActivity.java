package com.ByteDance.Gotlin.im.view.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.ByteDance.Gotlin.im.Repository;
import com.ByteDance.Gotlin.im.application.ThreadManager;
import com.ByteDance.Gotlin.im.databinding.DActivityTestBinding;
import com.ByteDance.Gotlin.im.entity.MessageEntity;
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

import java.sql.Date;
import java.util.List;
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
@Deprecated
@RequiresApi(api = Build.VERSION_CODES.Q)
public class TestActivity extends AppCompatActivity implements View.OnClickListener {

    private DActivityTestBinding b;
    private Context mContext;

    ConfirmPopupWindow confirmPopupWindow, confirmPopupWindow2;
    InputPopupWindow inputPopupWindow;
    SingleSelectPopupWindow singleSelectPopupWindow;

    private static final String TAG = "TestActivity";

    private static final Repository repository = Repository.INSTANCE;

    @SuppressLint("UnsafeOptInUsageError")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DActivityTestBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        mContext = this;

        b.testBar.imgChevronLeft.setVisibility(View.GONE);
        b.testBar.title.setText("测试页面");

        initPopupWindow();

        b.btnPopConfirm.setOnClickListener(this);
        b.btnPopConfirm2.setOnClickListener(this);
        b.btnPopInput.setOnClickListener(this);
        b.btnPopSelect.setOnClickListener(this);
        b.btnMain.setOnClickListener(this);

        b.send1.setOnClickListener(this);
        b.send2.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.equals(b.btnPopConfirm)) {
            DLogUtils.i(TAG, "展示第一个");
            confirmPopupWindow.show();
        } else if (view.equals(b.btnPopConfirm2)) {
            DLogUtils.i(TAG, "展示第二个");
            confirmPopupWindow2.show();
        } else if (view.equals(b.btnPopInput)) {
            inputPopupWindow.show();
        } else if (view.equals(b.btnPopSelect)) {
            singleSelectPopupWindow.show();
        } else if (view.equals(b.send1)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                String in1 = b.et1.getText().toString();
                if (in1.length() > 0) {
                    repository.postRequestFriend(Integer.parseInt(in1), "好友申请测试", "dzc测试备注");
                } else {
                    TPhoneUtil.INSTANCE.showToast(this, "uid为空");
                }
            }
        } else if (view.equals(b.send2)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                String in2 = b.et2.getText().toString();
                if (in2.length() > 0) {
                    repository.postRequestGroup(Integer.parseInt(in2), "加群申请测试", "dzc测试备注");
                } else {
                    TPhoneUtil.INSTANCE.showToast(this, "gid为空");
                }
            }
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
        confirmPopupWindow = new ConfirmPopupWindow(mContext, "测试", popupWindowListener);
        confirmPopupWindow2 = new ConfirmPopupWindow(mContext, "测试222222222222", popupWindowListener);
        // （可选）设置按钮文本
        confirmPopupWindow.setConfirmText("确认文本");
        confirmPopupWindow.setCancelText("取消测试文本");
        // （可选）设置警告类型颜色模式
        confirmPopupWindow.setWarnTextColorType();

        inputPopupWindow = new InputPopupWindow(mContext, "输入弹窗测试", popupWindowListener);

        singleSelectPopupWindow = new SingleSelectPopupWindow(mContext, "单选弹窗测试",
                "选项一", "选项二", popupWindowListener);
    }

}
