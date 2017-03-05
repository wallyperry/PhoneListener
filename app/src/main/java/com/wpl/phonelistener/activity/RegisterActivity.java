package com.wpl.phonelistener.activity;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wpl.phonelistener.R;
import com.wpl.phonelistener.base.BaseActivity;
import com.wpl.phonelistener.mvp.model.DataInjectImpl;
import com.wpl.phonelistener.mvp.presenter.M_Presenter;
import com.wpl.phonelistener.mvp.view.M_View;
import com.wpl.phonelistener.utils.BmobUtils;
import com.wpl.phonelistener.utils.PhoneUtils;
import com.wpl.phonelistener.utils.SPUtils;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;

/**
 * 信息录入界面
 * Created by 培龙 on 2017/2/20.
 */
public class RegisterActivity extends BaseActivity implements M_View.DataInject {
    @Bind(R.id.register_inputPhone)
    EditText inputPhone;
    @Bind(R.id.register_inputId)
    EditText inputId;
    @Bind(R.id.register_info)
    EditText inputInfo;
    @Bind(R.id.register_msgLL)
    LinearLayout msgLL;
    @Bind(R.id.register_msg)
    TextView msg;
    @Bind(R.id.register_pb)
    ProgressBar progressBar;

    private SPUtils spUtils;
    private PhoneUtils phoneUtils;

    @Override
    protected int initLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initView() {
        spUtils = new SPUtils(this, "loginStatus");
        phoneUtils = new PhoneUtils(this);
        msgLL.getBackground().setAlpha(150);
        inputPhone.setText(phoneUtils.getPhoneNumber());
        inputPhone.setSelection(inputPhone.getText().length());//Let the input method at the end of the text
        inputPhone.requestFocus();
    }

    @OnClick({R.id.register_injection})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_injection:   //注入按钮
                injectionData();
                break;
            default:
                break;
        }
    }

    private void injectionData() {
        msgLL.setVisibility(View.GONE);
        msg.setText("");
        String inPhone = inputPhone.getText().toString().trim();
        String inId = inputId.getText().toString().trim();
        String inInfo = inputInfo.getText().toString().trim();
        if (inPhone.length() != 11) {
            ToastShow("请输入本机电话号码");
        } else if (inInfo.length() < 1) {
            ToastShow("请输入备注信息");
        } else if (inId.length() < 1) {
            ToastShow("请输入控制端账号");
        } else {
            progressBar.setVisibility(View.VISIBLE);
            M_Presenter.DataInject injectPresenter = new DataInjectImpl(this);
            injectPresenter.dataInject(this, phoneUtils.getBrand(), inPhone, inId, inInfo);
        }
    }

    @Override
    public void injectSuccess(String objId) {
        progressBar.setVisibility(View.GONE);
        switch (objId) {
            case "notHave":
                msgLL.setVisibility(View.VISIBLE);
                msg.setText("控制账户不存在");
                break;
            case "have":
                msgLL.setVisibility(View.VISIBLE);
                msg.setText("该手机号已激活，请联系管理员");
                break;
            default:
                spUtils.putBoolean("isLogin", true);
                spUtils.putString("objId", objId);
                msgLL.setVisibility(View.VISIBLE);
                msg.setText("恭喜，激活成功啦！\n正在跳转中...");
                new Handler().postDelayed(() -> {
                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                    finish();
                }, 1500);
                break;
        }
    }

    @Override
    public void injectError(BmobException e) {
        progressBar.setVisibility(View.GONE);
        msgLL.setVisibility(View.VISIBLE);
        msg.setText(BmobUtils.errorMsg(e.getErrorCode()));
    }
}
