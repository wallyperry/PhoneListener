package com.wpl.phonelistener.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by 培龙 on 2017/2/21.
 */

public class ClientUser extends BmobObject {
    private String phone;
    private boolean isFeedback;
    private _User belongTo;
    private String phoneInfo;

    public String getPhoneInfo() {
        return phoneInfo;
    }

    public void setPhoneInfo(String phoneInfo) {
        this.phoneInfo = phoneInfo;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isFeedback() {
        return isFeedback;
    }

    public void setFeedback(boolean feedback) {
        isFeedback = feedback;
    }

    public _User getBelongTo() {
        return belongTo;
    }

    public void setBelongTo(_User belongTo) {
        this.belongTo = belongTo;
    }
}
