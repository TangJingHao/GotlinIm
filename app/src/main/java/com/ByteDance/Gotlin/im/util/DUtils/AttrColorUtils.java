package com.ByteDance.Gotlin.im.util.DUtils;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;

/**
 * @Author Zhicong Deng
 * @Date 2022/6/12 11:54
 * @Email 1520483847@qq.com
 * @Description
 */
public class AttrColorUtils {
    public static int getValueOfColorAttr(Context context, int attr) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(attr, typedValue, true);
        return Color.parseColor(String.valueOf(typedValue.coerceToString()));
    }
}
