package com.moudle.ijkplayer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by ke_li on 2016/10/21.
 */

public class ManagerUtils {


    /**
     * 创建确定取消弹窗
     *
     * @param context
     * @param title
     * @param positiveCallback
     * @param visible
     */
    public static void createTwoDialog(Context context, String title, String positive, String nagative, Dialog.OnClickListener positiveCallback, boolean visible) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(title);
        dialog.setPositiveButton(positive,positiveCallback);
        if (visible)
        dialog.setNegativeButton(nagative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.create().show();
    }

    public static void createTwoDialog(Context context, String title, Dialog.OnClickListener dialog, boolean visible) {
        createTwoDialog(context, title, "确定", "取消", dialog, visible);
    }



    /**
     * 判断WIFI是否打开
     *
     * @param context
     * @return
     */
    public static boolean isWifiEnabled(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkINfo = cm.getActiveNetworkInfo();
        if (networkINfo != null
                && networkINfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    public static final String  IS_USEWIFI = "IS_USEWIFI";

}
