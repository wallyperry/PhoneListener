package com.wpl.phonelistener;

import android.app.Application;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;


/**
 * Application
 * Created by 培龙 on 2017/2/20.
 */

public class MyApp extends Application {

    public static final String APPKEY = "d6fecacfa03937555cb24ea43881193f";

    @Override
    public void onCreate() {
        init();
        super.onCreate();
    }

    private void init() {

        BmobConfig config = new BmobConfig.Builder(this)
                .setApplicationId(APPKEY)
                .setConnectTimeout(60 * 5)
                .build();
        Bmob.initialize(config);
    }
}
