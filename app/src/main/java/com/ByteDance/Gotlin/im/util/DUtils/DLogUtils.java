package com.ByteDance.Gotlin.im.util.DUtils;

import android.util.Log;

/**
 * Created by Zhicong Deng
 * on 2022/6/11 21:14
 * https://github.com/LumosDZC
 *
 * 个人用LogUtils，将过滤输出等级设为NOTHING则关闭 log
 */
public class DLogUtils {

    public static final int VERBOSE = 1;
    public static final int DEBUG = 2;
    public static final int INFO = 3;
    public static final int WARN = 4;
    public static final int ERROR = 5;
    public static final int NOTHING = 6;
    public static int level = INFO;  //过滤输出等级

    public static void v(String TAG, String content) {
        if (level <= VERBOSE) {
            Log.v("【" + TAG + "】", ">>>>>>" + content);
        }
    }

    public static void d(String TAG, String content) {
        if (level <= DEBUG) {
            Log.d("【" + TAG + "】", ">>>>>>" + content);
        }
    }

    public static void i(String TAG, String method, String content) {
        if (level <= INFO) {
            Log.i("【" + TAG + "】", "[" + method + "] >>> " + content);
        }
    }

    public static void i(String TAG, String content) {
        if (level <= INFO) {
            Log.i("【" + TAG + "】", ">>>>>>" + content);
        }
    }

    public static void w(String TAG, String content) {
        if (level <= WARN) {
            Log.w("【" + TAG + "】", ">>>>>>" + content);
        }
    }

    public static void e(String TAG, String content) {
        if (level <= ERROR) {
            Log.e("【" + TAG + "】", ">>>>>>" + content);
        }
    }
}
