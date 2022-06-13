package com.ByteDance.Gotlin.im.viewmodel;

import androidx.lifecycle.ViewModel;

import com.ByteDance.Gotlin.im.Repository;
import com.ByteDance.Gotlin.im.info.SearchUser;

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

    private List<List<SearchUser>> searchUserList;

    // TODO 本应通过网络请求返回LiveData数据，目前仅测试用
    public List<List<SearchUser>> searchNewFriendByMail(String mail) {
        SearchUser u1 = new SearchUser("null", "123456789@qq.com", "张三", "状态显示");
        SearchUser u2 = new SearchUser("null", "123456789@qq.com", "李四", "状态显示");
        SearchUser u3 = new SearchUser("null", "123456789@qq.com", "王五", "状态显示");
        SearchUser u4 = new SearchUser("null", "123456789@qq.com", "赵六", "状态显示");
        ArrayList<SearchUser> G1 = new ArrayList<>();
        G1.add(u1);
        G1.add(u2);
        G1.add(u3);
        G1.add(u4);
        SearchUser u5 = new SearchUser("null", "123456789@qq.com", "福贵", "厨子");
        SearchUser u6 = new SearchUser("null", "123456789@qq.com", "小飞碟", "格格");
        SearchUser u7 = new SearchUser("null", "123456789@qq.com", "小李子", "公公");
        ArrayList<SearchUser> G2 = new ArrayList<>();
        G2.add(u5);
        G2.add(u6);
        G2.add(u7);
        searchUserList = new ArrayList<>();
        searchUserList.add(G1);
        searchUserList.add(G2);
        return searchUserList;
    }
}
