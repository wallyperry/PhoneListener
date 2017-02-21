package com.wpl.phonelistener.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.wpl.phonelistener.broadcast.MyBroadcastReceiver;
import com.wpl.phonelistener.utils.PhoneUtils;

/**
 * 服务
 * Created by 培龙 on 2017/2/21.
 */

public class MyService extends Service {
    private MyBroadcastReceiver receiver;
    private Intent intent;

    @Override
    public void onCreate() {
        Log.e("MyService", "服务已创建");
        receiver = new MyBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(receiver, filter);

//        Notification notification = new Notification();
//        startForeground(1, notification);
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("MyService", "服务运行成功");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.e("MyService", "服务已销毁");
        unregisterReceiver(receiver);
        PhoneUtils tools = new PhoneUtils(this);
        intent = new Intent(com.wpl.phonelistener.service.MyService.this,
                com.wpl.phonelistener.service.MyService.class);
        if (!tools.isServiceRunning(MyService.class.getName())) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startService(intent);
        }
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
