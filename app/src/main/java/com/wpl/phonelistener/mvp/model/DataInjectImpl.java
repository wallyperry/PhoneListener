package com.wpl.phonelistener.mvp.model;

import android.util.Log;

import com.wpl.phonelistener.bean.ClientUser;
import com.wpl.phonelistener.bean._User;
import com.wpl.phonelistener.mvp.presenter.M_Presenter;
import com.wpl.phonelistener.mvp.view.M_View;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by 培龙 on 2017/2/21.
 */

public class DataInjectImpl implements M_Presenter.DataInject {
    private M_View.DataInject view;

    public DataInjectImpl(M_View.DataInject view) {
        this.view = view;
    }

    @Override
    public void dataInject(String phone, String belongTo, String phoneInfo) {
        new Thread(() -> {
            BmobQuery<ClientUser> query = new BmobQuery<ClientUser>();
            query.addWhereEqualTo("phone", phone);
            query.count(ClientUser.class, new CountListener() {
                @Override
                public void done(Integer i, BmobException e) {
                    if (e == null) {
                        if (i == 0) {//进行注册
                            selectUserId(phone, belongTo, phoneInfo);
                        } else {
                            view.injectSuccess("have");
                        }
                    } else {
                        view.injectError(e);
                    }
                }
            });
        }).start();
    }

    /**
     * 查找该用户名的objectId
     *
     * @param phone    phone
     * @param belongTo belongTo
     */
    private void selectUserId(String phone, String belongTo, String phoneInfo) {
        BmobQuery<_User> query = new BmobQuery<_User>();
        query.addWhereEqualTo("username", belongTo);
        query.findObjects(new FindListener<_User>() {
            @Override
            public void done(List<_User> list, BmobException e) {
                if (e == null) {
                    if (list.size() > 0) {
                        addClientUser(phone, list.get(0).getObjectId(), phoneInfo);
                    } else {
                        view.injectSuccess("notHave");
                    }
                } else {
                    view.injectError(e);
                }
            }
        });
    }

    /**
     * 将用户信息添加到数据库
     *
     * @param phone    phone
     * @param objectId objectId
     */
    private void addClientUser(String phone, String objectId, String phoneInfo) {
        ClientUser clientUser = new ClientUser();
        _User user = new _User();
        user.setObjectId(objectId);
        clientUser.setPhone(phone);
        clientUser.setPhoneInfo(phoneInfo);
        clientUser.setFeedback(false);
        clientUser.setBelongTo(user);
        clientUser.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if (e == null) {
                    view.injectSuccess(objectId);
                } else {
                    view.injectError(e);
                }
            }
        });
    }
}
