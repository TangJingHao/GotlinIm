package com.ByteDance.Gotlin.im.util.DUtils;

import com.ByteDance.Gotlin.im.info.vo.UserVO;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * @Author Zhicong Deng
 * @Date 2022/6/16 19:16
 * @Email 1520483847@qq.com
 * @Description
 */
public class DSortUtils {

    /**
     * 获得适合于适配器的参数
     * @param dataList
     * @return
     */
    public static List<List<UserVO>> sort(List<UserVO> dataList, List<String> title){
        boolean[] map = new boolean[27];
        Collections.sort(dataList, new Comparator<UserVO>() {
            @Override
            public int compare(UserVO o1, UserVO o2) {
                try {
                    int index = o1.getNickName().toLowerCase(Locale.ROOT).toString().charAt(0) - 'a';
                    if (index >= 0 && index < 26) {
                        if (!map[index]) map[index] = true;
                    } else {
                        if (!map[26]) map[26] = true;
                    }

                    String s1 = TComparatorUtil.getPingYin(o1.getNickName());
                    String s2 = TComparatorUtil.getPingYin(o2.getNickName());
                    String str1 = new String(s1.getBytes("GB2312"), "ISO-8859-1");
                    String str2 = new String(s2.getBytes("GB2312"), "ISO-8859-1");
                    return str1.compareTo(str2);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });

        for (int i = 0; i < map.length; i++) {
            if (map[i]) {
                title.add(numToLetter(i));
            }
        }

        return getListList(dataList);
    }

    /**
     * 把排序好的List<UserVO>按照首字母划分为List<List<UserVO>> 方便作为适配器的参数
     * @param dataList
     * @return
     */
    public static List<List<UserVO>> getListList(List<UserVO> dataList) {
        boolean[] map = new boolean[27];
        ArrayList<List<UserVO>> res = new ArrayList<>(dataList.size());
        ArrayList<UserVO> elseList = new ArrayList<>(); // 用于存储其他字符的
        int curIndex = -1;
        for (UserVO user : dataList) {
            int index = user.getNickName().toLowerCase(Locale.ROOT).charAt(0) - 'a';
            if (index >= 0 && index < 26) {
                if (!map[index]) {
                    map[index] = true;
                    ArrayList<UserVO> userList = new ArrayList<>();
                    userList.add(user);
                    res.add(userList);
                    curIndex++;
                } else {
                    res.get(curIndex).add(user);
                }
            } else {
                if (!map[26]) {
                    map[26] = true;
                    elseList.add(user);
                } else {
                    elseList.add(user);
                }
            }
        }
        if (elseList.size() != 0)
            res.add(elseList);

        return res;
    }

    public static String numToLetter(int num) {
        if (num >= 0 && num < 26) {
            return String.valueOf((char) (num + 'A'));
        } else {
            return "#";
        }
    }


}
