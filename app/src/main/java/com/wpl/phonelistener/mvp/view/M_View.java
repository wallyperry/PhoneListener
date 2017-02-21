package com.wpl.phonelistener.mvp.view;

import java.util.Map;

import cn.bmob.v3.exception.BmobException;

/**
 * Created by 培龙 on 2017/2/20.
 */

public interface M_View {

    interface DataInject {
        void injectSuccess(String objId);

        void injectError(BmobException e);
    }

    /**
     * 获取当前位置信息
     */
    interface CurrentLocation {
        void locationSuccess(Map<String, String> map);

        void locationError(Map<String, String> map);

    }
}
