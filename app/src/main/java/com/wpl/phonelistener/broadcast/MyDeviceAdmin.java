package com.wpl.phonelistener.broadcast;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by 培龙 on 2017/2/21.
 */
public class MyDeviceAdmin extends DeviceAdminReceiver {

    @Override
    public CharSequence onDisableRequested(Context context, Intent intent) {
        return "警告！取消激活将导致数据丢失，非专业人员请不要点击确定！";
    }
}
