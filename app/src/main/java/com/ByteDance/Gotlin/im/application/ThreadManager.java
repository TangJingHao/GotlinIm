package com.ByteDance.Gotlin.im.application;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author Zhicong Deng
 * @Date 2022/6/19 19:22
 * @Email 1520483847@qq.com
 * @Description 线程管理
 */
public class ThreadManager {

    //获取内核数量
    private final static int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();

    private final static ThreadPoolExecutor mDefFixThreadPool; // 长期维护的聊天线程
    private final static ThreadPoolExecutor mDefCacheThreadPool; // 非核心线程，闲杂用
    private final static ExecutorService mDefSingleThreadPool;

    static {
        mDefFixThreadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(NUMBER_OF_CORES * 2);
        mDefCacheThreadPool = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        mDefSingleThreadPool = Executors.newSingleThreadExecutor();
    }

    public static ThreadPoolExecutor getDefFixThreadPool() {
        return mDefFixThreadPool;
    }

    public static ThreadPoolExecutor getDefCacheThreadPool() {
        return mDefCacheThreadPool;
    }

    public static ExecutorService getDefSingleThreadPool() {
        return mDefSingleThreadPool;
    }
}
