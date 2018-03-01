package com.flyco.tablayout.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.TextView;

/**
 * Created by ke_li on 2018/1/10.
 */

public class UiUtils {
    public static void getTextPx(Context context, float point, TextView textView) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int widthPixels = displayMetrics.widthPixels;
        float px = widthPixels * point / 240;
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, px);
    }
}
