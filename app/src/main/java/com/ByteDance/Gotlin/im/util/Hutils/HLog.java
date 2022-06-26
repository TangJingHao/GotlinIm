package com.ByteDance.Gotlin.im.util.Hutils;

import android.util.Log;

import com.ByteDance.Gotlin.im.BuildConfig;

/**
 * @author: Hx
 * @date: 2022年06月25日 17:58
 */
public class HLog {

    static String tag;
    static String className;
    static String methodName;
    static int line;

    private HLog() {
    }

    public static boolean isDebuggable() {
        return BuildConfig.DEBUG;
    }

    private static<T> String createLog(T log) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("(").append(className).append(":").append(line).append(")");
        buffer.append(methodName);
        buffer.append(" >>>>> ").append(log);
        return buffer.toString();
    }

    private static void getTag(StackTraceElement[] sElements) {
        className = sElements[1].getFileName();
        methodName = " [" + sElements[1].getMethodName() + "]";
        line = sElements[1].getLineNumber();
        tag = className.replaceAll(".java", "");
    }

    public static<T> void e(T message) {
        if (!isDebuggable())
            return;

        // Throwable instance must be created before any methods
        getTag(new Throwable().getStackTrace());
        Log.e(tag, createLog(message));
    }


    public static<T> void i(T message) {
        if (!isDebuggable())
            return;

        getTag(new Throwable().getStackTrace());
        Log.i(tag, createLog(message));
    }

    public static<T> void d(T message) {
        if (!isDebuggable())
            return;

        getTag(new Throwable().getStackTrace());
        Log.d(tag, createLog(message));
    }

    public static<T> void v(T message) {
        if (!isDebuggable())
            return;

        getTag(new Throwable().getStackTrace());
        Log.v(tag, createLog(message));
    }

    public static<T> void w(T message) {
        if (!isDebuggable())
            return;

        getTag(new Throwable().getStackTrace());
        Log.w(tag, createLog(message));
    }

}
