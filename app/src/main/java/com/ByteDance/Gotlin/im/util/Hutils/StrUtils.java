package com.ByteDance.Gotlin.im.util.Hutils;

import android.text.TextUtils;

/**
 * @author: Hx
 * @date: 2022年06月17日 16:15
 */
public class StrUtils {

    /**
     * Str不为空，除去空格，长度>0
     *
     * @param str
     * @return true/false
     */
    public static boolean isMsgValid(String str) {
        return null != str && !TextUtils.isEmpty(str) && str.trim().length() > 0;
    }
}
