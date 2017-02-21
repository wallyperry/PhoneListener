package com.wpl.phonelistener.mvp.model;

import android.content.Context;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.wpl.phonelistener.mvp.presenter.M_Presenter;
import com.wpl.phonelistener.mvp.view.M_View;

import java.util.HashMap;
import java.util.Map;

/**
 * 获取当前位置
 * Created by 培龙 on 2017/2/21.
 */

public class CurrentLocationImpl implements M_Presenter.CurrentLocation {
    private Map<String, String> map;

    private M_View.CurrentLocation view;
    private AMapLocationClient client;
    private AMapLocationListener listener;
    private AMapLocationClientOption option;

    public CurrentLocationImpl(M_View.CurrentLocation view) {
        this.view = view;
        map = new HashMap<>();
    }

    @Override
    public void getLocation(Context context) {
        listener = location -> {
            if (location != null) {
                if (location.getErrorCode() == 0) {//定位成功的回调
                    map.put("result", "success");
                    map.put("lat", "" + location.getLatitude());
                    map.put("lon", "" + location.getLongitude());
                    map.put("aoiName", "" + location.getAoiName());
                    map.put("address", "" + location.getAddress());
                    map.put("province", "" + location.getProvince());
                    map.put("city", "" + location.getCity());
                    map.put("district", "" + location.getDistrict());
                    map.put("street", "" + location.getStreet());
                    view.locationSuccess(map);
                } else {
                    map.put("result", "fail");
                    map.put("errorCode", "" + location.getErrorCode());
                    map.put("errorInfo", "" + location.getErrorInfo());
                    view.locationError(map);
                }
            }
        };
        client = new AMapLocationClient(context.getApplicationContext());
        client.setLocationListener(listener);
        option = new AMapLocationClientOption();
        option.setOnceLocation(true);
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        client.setLocationOption(option);
        client.startLocation();
    }
}
