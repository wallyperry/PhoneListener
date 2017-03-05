package com.wpl.phonelistener.activity;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.ScrollView;
import android.widget.TextView;

import com.wpl.phonelistener.R;
import com.wpl.phonelistener.base.BaseActivity;
import com.wpl.phonelistener.broadcast.MyDeviceAdmin;
import com.wpl.phonelistener.service.MyService;
import com.wpl.phonelistener.utils.DialogUtils;
import com.wpl.phonelistener.utils.PhoneUtils;

import butterknife.Bind;

public class MainActivity extends BaseActivity {
    @Bind(R.id.main_msgTv)
    TextView msgTv;
    @Bind(R.id.main_sv)
    ScrollView sv;

    private PhoneUtils phoneUtils;
    private Intent intentService;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (!MainActivity.this.isFinishing()) {
                msgTv.append(new PhoneUtils(MainActivity.this).getTime() + "> " + msg.obj + "\n");
                sv.scrollTo(0, msgTv.getMeasuredHeight() - sv.getHeight());
            }
        }
    };

    @Override
    protected int initLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        phoneUtils = new PhoneUtils(this);
        intentService = new Intent(this, MyService.class);

        Message msg = new Message();
        msg.obj = "正在初始化数据，请稍后...";
        handler.sendMessage(msg);

        Message msg1 = new Message();
        msg1.obj = "服务启动中...";
        handler.sendMessage(msg1);
        startService(intentService);

        new Thread(() -> {
            while (!MainActivity.this.isFinishing()) {
                try {
                    Thread.sleep(10000);
                    Message msg2 = new Message();
                    if (phoneUtils.isServiceRunning(MyService.class.getName())) {
                        msg2.obj = "服务正常运行中...";
                    } else {
                        msg2.obj = "服务已停止";
                    }
                    handler.sendMessage(msg2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 申请设备管理器权限
     */
    private void initDevice() {
        Message msg3 = new Message();
        msg3.obj = "正在检测设备管理器信息";
        handler.sendMessage(msg3);
        // 申请权限
        ComponentName componentName = new ComponentName(
                MainActivity.this, MyDeviceAdmin.class);
        // 设备安全管理服务 2.2之前的版本是没有对外暴露的 只能通过反射技术获取
        DevicePolicyManager devicePolicyManager =
                (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        // 判断该组件是否有系统管理员的权限
        boolean isAdminActive = devicePolicyManager.isAdminActive(componentName);
        if (!isAdminActive) {
            Message msg4 = new Message();
            msg4.obj = "设备管理器未激活...";
            handler.sendMessage(msg4);
            DialogUtils.showConfirmDialog(MainActivity.this,
                    "设备管理器", "为了避免数据被清除或轻易被卸载\n您必须激活设备管理器", "取消", "好的",
                    new DialogUtils.DialogCallBack() {
                        @Override
                        public void onComplete() {
                            Message msg5 = new Message();
                            msg5.obj = "正在跳转至设备管理器激活页面";
                            handler.sendMessage(msg5);
                            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
                            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "测试");
                            startActivityForResult(intent, 20);
                        }

                        @Override
                        public void onCancel() {
                            Message msg6 = new Message();
                            msg6.obj = "激活设备管理器操作已取消";
                            handler.sendMessage(msg6);
                        }
                    });
        } else {
            Message msg7 = new Message();
            msg7.obj = "设备管理器已激活";
            handler.sendMessage(msg7);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(this::initDevice, 2000);
    }
}
