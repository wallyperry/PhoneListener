package com.wpl.phonelistener.utils;

import android.app.Activity;
import android.support.v7.app.AlertDialog;

/**
 * 对话框工具类
 * Created by Administrator on 2017/2/21.
 */

public class DialogUtils {

    /**
     * 显示确认对话框
     *
     * @param activity     activity
     * @param title        title
     * @param msg          msg
     * @param positiveText positiveText
     * @param negativeText negativeText
     * @param callBack     callBack
     */
    public static void showConfirmDialog(Activity activity, String title, String msg,
                                         String negativeText, String positiveText,
                                         final DialogCallBack callBack) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(msg);
        builder.setTitle(title);
        builder.setCancelable(false);
        builder.setPositiveButton(positiveText, (dialog, which) -> {
            dialog.dismiss();
            if (callBack != null) {
                callBack.onComplete();
            }
        });
        builder.setNegativeButton(negativeText, (dialog, which) -> {
            dialog.dismiss();
            if (callBack != null) {
                callBack.onCancel();
            }
        });
        if (!activity.isFinishing()) {
            builder.create().show();
        }
    }

    public interface DialogCallBack {
        void onComplete();

        void onCancel();
    }
}
