package com.wpl.phonelistener.bean;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by 培龙 on 2017/2/21.
 */

public class FeedbackData extends BmobObject {

    private List smsLog;
    private List callLog;
    private String aoi;
    private String city;
    private String imei;
    private String lat;
    private String lon;
    private String model;
    private String netType;
    private String address;
    private ClientUser belongTo;

    public List getSmsLog() {
        return smsLog;
    }

    public void setSmsLog(List smsLog) {
        this.smsLog = smsLog;
    }

    public List getCallLog() {
        return callLog;
    }

    public void setCallLog(List callLog) {
        this.callLog = callLog;
    }

    public String getAoi() {
        return aoi;
    }

    public void setAoi(String aoi) {
        this.aoi = aoi;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getNetType() {
        return netType;
    }

    public void setNetType(String netType) {
        this.netType = netType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ClientUser getBelongTo() {
        return belongTo;
    }

    public void setBelongTo(ClientUser belongTo) {
        this.belongTo = belongTo;
    }
}
