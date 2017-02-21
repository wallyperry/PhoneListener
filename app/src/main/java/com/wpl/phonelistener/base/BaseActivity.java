package com.wpl.phonelistener.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;


import com.jaeger.library.StatusBarUtil;

import butterknife.ButterKnife;

/**
 * baseActivity
 * Created by 培龙 on 2017/2/13.
 */

public abstract class BaseActivity extends AppCompatActivity {
    protected Context context;
    protected Toast toast;
    protected Bundle savedInstanceState;

    protected abstract int initLayoutId();

    protected abstract void initView();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        this.savedInstanceState = savedInstanceState;
        initLayoutId();
        super.onCreate(savedInstanceState);
        setContentView(initLayoutId());
        context = this;
        ButterKnife.bind(this);
        initStatusBar();
        initView();
    }

    private void initStatusBar() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0 全透明状态栏
//            View decorView = getWindow().getDecorView();
//            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
//            decorView.setSystemUiVisibility(option);
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4 全透明状态栏
//            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
//            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
//        }

        StatusBarUtil.hideFakeStatusBarView(this);
    }

    /**
     * 防止Toast重复显示
     *
     * @param msg 消息
     */
    public void ToastShow(String msg) {
        if (toast == null) {
            toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
            toast.setDuration(Toast.LENGTH_SHORT);
        }
        toast.show();
    }

    public void LogE(String s) {
        Log.e(getClass().getName(), s);
    }

    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
    }
}
