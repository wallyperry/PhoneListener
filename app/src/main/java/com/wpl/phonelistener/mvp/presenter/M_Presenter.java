package com.wpl.phonelistener.mvp.presenter;

import android.content.Context;

/**
 * Created by 培龙 on 2017/2/20.
 */

public interface M_Presenter {

    /**
     * 注入资料
     */
    interface DataInject {
        void dataInject(String phone, String belongTo);
    }

    /**
     * 获取位置信息
     */
    interface CurrentLocation {
        void getLocation(Context context);
    }
}