package com.wpl.phonelistener.activity;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wpl.phonelistener.R;
import com.wpl.phonelistener.base.BaseActivity;
import com.wpl.phonelistener.mvp.model.DataInjectImpl;
import com.wpl.phonelistener.mvp.presenter.M_Presenter;
import com.wpl.phonelistener.mvp.view.M_View;
import com.wpl.phonelistener.utils.BmobUtils;
import com.wpl.phonelistener.utils.ProgressDialogUtils;
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
    @Bind(R.id.register_msgLL)
    LinearLayout msgLL;
    @Bind(R.id.register_msg)
    TextView msg;

    private ProgressDialogUtils progressDialog;
    private SPUtils spUtils;

    @Override
    protected int initLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initView() {
        progressDialog = new ProgressDialogUtils(this, "请稍后...");
        spUtils = new SPUtils(this, "loginStatus");
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
        String inPhone = inputPhone.getText().toString().trim();
        String inId = inputId.getText().toString().trim();
        if (inPhone.length() != 11) {
            ToastShow("请输入11位手机号码");
        } else if (inId.length() < 1) {
            ToastShow("请输入控制端ID");
        } else {
            progressDialog.show();
            M_Presenter.DataInject injectPresenter = new DataInjectImpl(this);
            injectPresenter.dataInject(inPhone, inId);
        }
    }

    @Override
    public void injectSuccess(String objId) {
        progressDialog.dismiss();
        switch (objId) {
            case "notHave":
                ToastShow("控制账户不存在");
                break;
            case "have":
                ToastShow("该手机号已注入，请联系管理员");
                break;
            default:
                spUtils.putBoolean("isLogin", true);
                spUtils.putString("objId", objId);
                ToastShow("注入成功");
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                finish();
                break;
        }
    }

    @Override
    public void injectError(BmobException e) {
        progressDialog.dismiss();
        ToastShow(BmobUtils.errorMsg(e.getErrorCode()));
    }
}
