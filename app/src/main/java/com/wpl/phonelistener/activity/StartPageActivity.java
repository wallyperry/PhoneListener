package com.wpl.phonelistener.activity;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.TextView;

import com.wpl.phonelistener.R;
import com.wpl.phonelistener.base.BaseActivity;
import com.wpl.phonelistener.utils.DialogUtils;
import com.wpl.phonelistener.utils.PermissionsChecker;
import com.wpl.phonelistener.utils.SPUtils;

import butterknife.Bind;

/**
 * 启动页面
 * Created by 培龙 on 2017/2/20.
 */

public class StartPageActivity extends BaseActivity {
    @Bind(R.id.start_text)
    TextView textView;

    private SPUtils spUtils;
    private boolean isRequireCheck; //是否需要系统权限检测
    static final String[] PERMISSIONS = new String[]{   //危险权限（运行时权限）
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_SMS,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_CONTACTS
    };
    private PermissionsChecker mPermissionsChecker; //检查权限
    private static final int PERMISSION_REQUEST_CODE = 0;   //系统权限返回码
    private static final String PACKAGE_URL_SCHEME = "package:";

    @Override
    protected int initLayoutId() {
        return R.layout.activity_start;
    }

    @Override
    protected void initView() {
        textView.setText("正在初始化...");
        spUtils = new SPUtils(this, "loginStatus");
        initPermissions();
    }

    private void initPermissions() {
        textView.append("\n检查权限...");
        mPermissionsChecker = new PermissionsChecker(this);
        isRequireCheck = true;
    }

    private void initUser() {
        textView.append("\n正在初始化用户信息...");
        if (spUtils.getBoolean("isLogin", false)) {
            textView.append("\n用户已注入...");
            textView.append("\n正在跳转中...");
            try {
                startActivity(new Intent(StartPageActivity.this, MainActivity.class));
                finish();
            } catch (RuntimeException e) {
                textView.append("\n跳转出错...");
                textView.append("\n即将退出...");
                new Handler().postDelayed(this::finish, 3000);
                e.printStackTrace();
            }

        } else {
            textView.append("\n用户未注入...");
            textView.append("\n跳转至信息录入页面...");
            new Handler().postDelayed(() -> {
                startActivity(new Intent(this, RegisterActivity.class));
                finish();
            }, 2000);
        }
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isRequireCheck) {
            //权限没有授权，进入授权界面
            if (mPermissionsChecker.judgePermissions(PERMISSIONS)) {
                Log.e("MainActivity", "没有权限");
                textView.append("\n无权限");
                ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_REQUEST_CODE);
            } else {
                Log.e("MainActivity", "有权限");
                textView.append("\n权限验证通过...");
                DialogUtils.showConfirmDialog(this, "免责声明", getResources().getString(R.string.agreement),
                        "退出", "同意", new DialogUtils.DialogCallBack() {
                            @Override
                            public void onComplete() {
                                initUser();
                            }

                            @Override
                            public void onCancel() {
                                finish();
                                System.exit(0);
                            }
                        });
            }
        } else {
            isRequireCheck = true;
        }
    }

    /**
     * 用户权限处理，
     * 如果全部获取，则直接过
     * 如果权限缺失，则提示Dialog
     *
     * @param requestCode  请求码
     * @param permissions  权限
     * @param grantResults 结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE && hasAllPermissionsGranted(grantResults)) {
            isRequireCheck = true;
        } else {
            isRequireCheck = false;
            showPermissionDialog();
        }
    }

    /**
     * 提示对话框
     */
    private void showPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("帮助");
        builder.setMessage("当前应用缺少必要权限。请点击\"设置\"-打开所需权限。");
        // 拒绝, 退出应用
        builder.setNegativeButton("退出", (dialog, which) -> finish());

        builder.setPositiveButton("设置", (dialog, which) -> startAppSettings());
        builder.setCancelable(false);
        builder.show();
    }

    /**
     * 启动应用的设置
     */
    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse(PACKAGE_URL_SCHEME + getPackageName()));
        startActivity(intent);
    }

    /**
     * 含有全部权限
     *
     * @param grantResults
     * @return
     */
    private boolean hasAllPermissionsGranted(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }
}
