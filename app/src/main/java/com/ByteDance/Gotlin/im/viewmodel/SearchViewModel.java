package com.ByteDance.Gotlin.im.viewmodel;

import androidx.lifecycle.ViewModel;

import com.ByteDance.Gotlin.im.Repository;
import com.ByteDance.Gotlin.im.info.VO.TestUser;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Zhicong Deng
 * @Date 2022/6/12 10:35
 * @Email 1520483847@qq.com
 * @Description 搜索界面相关的ViewModel
 */
public class SearchViewModel extends ViewModel {

    private final Repository repository = Repository.INSTANCE;

    private List<List<TestUser>> searchUserList;

    // TODO 本应通过网络请求返回LiveData数据，目前仅测试用
    public List<List<TestUser>> getUserGroupMoreThanOne(String mail) {
        TestUser u1 = new TestUser("null", "123456789@qq.com", "张三", "状态显示", "无消息");
        TestUser u2 = new TestUser("null", "123456789@qq.com", "李四", "状态显示", "无消息");
        TestUser u3 = new TestUser("null", "123456789@qq.com", "王五", "状态显示", "无消息");
        TestUser u4 = new TestUser("null", "123456789@qq.com", "赵六", "状态显示", "无消息");
        ArrayList<TestUser> G1 = new ArrayList<>();
        G1.add(u1);
        G1.add(u2);
        G1.add(u3);
        G1.add(u4);
        TestUser u5 = new TestUser("null", "123456789@qq.com", "小福贵", "厨子", "做饭");
        TestUser u6 = new TestUser("null", "123456789@qq.com", "小飞碟", "格格", "刺杀");
        TestUser u7 = new TestUser("null", "123456789@qq.com", "小李子", "公公", "捣乱");
        ArrayList<TestUser> G2 = new ArrayList<>();
        G2.add(u5);
        G2.add(u6);
        G2.add(u7);
        searchUserList = new ArrayList<>();
        searchUserList.add(G1);
        searchUserList.add(G2);
        return searchUserList;
    }

    public List<List<TestUser>> getOneUserGroup(String mail) {
        TestUser u5 = new TestUser("null", "123456789@qq.com", "小福贵", "厨子", "做饭");
        TestUser u6 = new TestUser("null", "123456789@qq.com", "小飞碟", "格格", "刺杀");
        TestUser u7 = new TestUser("null", "123456789@qq.com", "小李子", "公公", "捣乱");
        ArrayList<TestUser> G2 = new ArrayList<>();
        G2.add(u5);
        G2.add(u6);
        G2.add(u7);
        searchUserList = new ArrayList<>();
        searchUserList.add(G2);
        return searchUserList;
    }
}
