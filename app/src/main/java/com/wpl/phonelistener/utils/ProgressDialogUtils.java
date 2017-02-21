package com.wpl.phonelistener.utils;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by 培龙 on 2016/12/21.
 */

public class ProgressDialogUtils {
    private ProgressDialog progDialog;// 搜索时进度条
    private Context context;
    private String msg;

    public ProgressDialogUtils(Context context, String msg) {
        this.context = context;
        this.msg = msg;
    }

    /**
     * 显示进度条框
     */
    public void show() {
        progDialog = null;
        if (progDialog == null)
            progDialog = new ProgressDialog(context);
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(true);
        progDialog.setMessage("" + msg);
        progDialog.setCanceledOnTouchOutside(false);
        progDialog.show();
    }

    /**
     * 隐藏进度框
     */
    public void dismiss() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }
}
