package com.wpl.phonelistener.utils;

import android.Manifest;
import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;

import java.io.InputStream;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by 培龙 on 2017/2/14.
 */

public class PhoneUtils {
    private Context context;
    private TelephonyManager tm;
    private Calendar calendar;

    public PhoneUtils(Context context) {
        this.context = context;
        tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        calendar = Calendar.getInstance();
    }

    /**
     * 获取是否有网络连接方法
     *
     * @return 是否有网
     */
    public boolean isConnected() {
        if (context == null)
            return false;
        ConnectivityManager mConnMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (mConnMgr == null)
            return false;
        NetworkInfo aActiveInfo = mConnMgr.getActiveNetworkInfo();
        // 获取活动网络连接信息
        boolean b = false;
        if (aActiveInfo != null) {
            b = true;
        } else {
            b = false;
        }
        return b;
    }

    /**
     * 判断某个服务是否正在运行的方法
     *
     * @param serviceName 是包名+服务的类名 或者 服务类名.class.getName();
     * @return true代表正在运行，false代表服务没有正在运行
     */
    public boolean isServiceRunning(String serviceName) {
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningServiceInfo> services = activityManager.getRunningServices(Integer.MAX_VALUE);

        for (ActivityManager.RunningServiceInfo runningServiceInfo : services) {
            if (runningServiceInfo.service.getClassName().equals(serviceName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取设备手机号码
     *
     * @return 手机号码
     */
    public String getPhoneNumber() {
        return "" + tm.getLine1Number();
    }

    /**
     * 获取设备串号
     *
     * @return 手机串号
     */
    public String getImei() {
        return "" + tm.getDeviceId();
    }

    /**
     * 获取设备品牌型号
     *
     * @return 手机品牌型号
     */
    public String getModel() {
        return "" + Build.BRAND + " " + Build.MODEL;
    }

    /**
     * 获取设备网络类型
     *
     * @return netType
     */
    public String getNetType() {
        String netType = "Offline";
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            if (networkInfo.getExtraInfo().toLowerCase().equals("cmnet")) {
                netType = "CMNET";
            } else {
                netType = "CMWAP";
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = "WIFI";
        }
        return "" + netType;
    }

    /**
     * 获取系统时间
     *
     * @return 年-月-日 时:分钟:秒
     */
    public String getTime() {
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int s = calendar.get(Calendar.SECOND);
        return "" + year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + s;
    }

    /**
     * 获取设备所有短信
     *
     * @return 短信
     */
    public List<Map<String, String>> getSmsInPhone() {
        List<Map<String, String>> list = new ArrayList<>();
        final String SMS_URI_INBOX = "content://sms/";// 所有信息
        try {
            ContentResolver cr = context.getContentResolver();
            String[] projection = new String[]{"_id", "address", "person", "body", "date", "type"};
            Uri uri = Uri.parse(SMS_URI_INBOX);
            Cursor cursor = cr.query(uri, projection, null, null, "date desc");
            assert cursor != null;
            while (cursor.moveToNext()) {
                Map<String, String> map = new HashMap<>();

                // -----------------------信息----------------
                int nameColumn = cursor.getColumnIndex("person");// 联系人姓名列表序号
                int phoneNumberColumn = cursor.getColumnIndex("address");// 手机号
                int smsbodyColumn = cursor.getColumnIndex("body");// 短信内容
                int dateColumn = cursor.getColumnIndex("date");// 日期
                int typeColumn = cursor.getColumnIndex("type");// 收发类型 1表示接受 2表示发送
                String nameId = cursor.getString(nameColumn);
                String phoneNumber = cursor.getString(phoneNumberColumn);
                String smsbody = cursor.getString(smsbodyColumn);
                String type = cursor.getString(typeColumn);
                Date d = new Date(Long.parseLong(cursor.getString(dateColumn)));
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd " + "\n" + "hh:mm:ss");
                String date = dateFormat.format(d);

                // --------------------------匹配联系人名字--------------------------
                Uri personUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, phoneNumber);
                Cursor localCursor = cr.query(personUri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup.PHOTO_ID, ContactsContract.PhoneLookup._ID}, null, null, null);
                if (localCursor != null) {
                    if (localCursor.getCount() != 0) {
                        localCursor.moveToFirst();
                        String name = localCursor.getString(localCursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
                        long photoid = localCursor.getLong(localCursor.getColumnIndex(ContactsContract.PhoneLookup.PHOTO_ID));
                        long contactid = localCursor.getLong(localCursor.getColumnIndex(ContactsContract.PhoneLookup._ID));
                        map.put("name", "" + name);
                        // 如果photoid 大于0 表示联系人有头像 ，如果没有给此人设置头像则给他一个默认的
                        if (photoid > 0) {
                            Uri uri1 = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactid);
                            InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(cr, uri1);
                        } else {
                        }
                    } else {
                        map.put("name", "" + phoneNumber);
                    }
                }
                localCursor.close();
                String t = "";
                if (type.equals("1")) {
                    t = "接收";
                }
                if (type.equals("2")) {
                    t = "发送";
                }
                map.put("type", "" + t);
                map.put("content", "" + smsbody);
                map.put("date", "" + date);
                list.add(map);
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
        return list;
    }


    /**
     * 获取设备所有通话记录
     *
     * @return 所有通话记录
     */
    public List<Map<String, String>> getCallDataList() {
        // 1.获得ContentResolver
        ContentResolver resolver = context.getContentResolver();
        // 2.利用ContentResolver的query方法查询通话记录数据库
        /**
         * @param uri 需要查询的URI，（这个URI是ContentProvider提供的）
         * @param projection 需要查询的字段
         * @param selection sql语句where之后的语句
         * @param selectionArgs ?占位符代表的数据
         * @param sortOrder 排序方式
         *
         */
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
        }
        Cursor cursor = resolver.query(CallLog.Calls.CONTENT_URI, // 查询通话记录的URI
                new String[]{CallLog.Calls.CACHED_NAME// 通话记录的联系人
                        , CallLog.Calls.NUMBER// 通话记录的电话号码
                        , CallLog.Calls.DATE// 通话记录的日期
                        , CallLog.Calls.DURATION// 通话时长
                        , CallLog.Calls.TYPE}// 通话类型
                , null, null, CallLog.Calls.DEFAULT_SORT_ORDER// 按照时间逆序排列，最近打的最先显示
        );
        // 3.通过Cursor获得数据
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        assert cursor != null;
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));
            String number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
            long dateLong = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE));
            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(dateLong));
            int duration = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.DURATION));
            int type = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE));
            String typeString = "";
            switch (type) {
                case CallLog.Calls.INCOMING_TYPE:
                    typeString = "接听";
                    break;
                case CallLog.Calls.OUTGOING_TYPE:
                    typeString = "拨出";
                    break;
                case CallLog.Calls.MISSED_TYPE:
                    typeString = "未接";
                    break;
                default:
                    break;
            }
            Map<String, String> map = new HashMap<String, String>();
            map.put("name", (name == null) ? "未备注" : name);
            map.put("number", number);
            map.put("date", date);
            map.put("duration", (duration / 60) + "分钟");
            map.put("type", typeString);
            list.add(map);
        }
        return list;
    }
}
