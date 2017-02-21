package com.wpl.phonelistener.utils;

/**
 * Bmob返回的错误码信息
 * Created by 培龙 on 2017/2/18.
 */

public class BmobUtils {

    public static String errorMsg(int code) {
        String msg;
        switch (code) {
            case 9001:
                msg = "Application Id为空，请初始化.";
                break;
            case 9002:
                msg = "解析返回数据出错";
                break;
            case 9003:
                msg = "上传文件出错";
                break;
            case 9004:
                msg = "文件上传失败";
                break;
            case 9005:
                msg = "批量操作只支持最多50条";
                break;
            case 9006:
                msg = "objectId为空";
                break;
            case 9007:
                msg = "文件大小超过10M";
                break;
            case 9008:
                msg = "上传文件不存在";
                break;
            case 9009:
                msg = "没有缓存数据";
                break;
            case 9010:
                msg = "网络超时";
                break;
            case 9011:
                msg = "User类不支持批量操作";
                break;
            case 9012:
                msg = "上下文为空";
                break;
            case 9013:
                msg = "数据表名称格式不正确";
                break;
            case 9014:
                msg = "第三方账号授权失败";
                break;
            case 9015:
                msg = "其他错误";
                break;
            case 9016:
                msg = "无网络连接，请检查您的手机网络";
                break;
            case 9017:
                msg = "第三方登录错误";
                break;
            case 9018:
                msg = "参数不能为空";
                break;
            case 9019:
                msg = "格式不正确";
                break;
            default:
                msg = "未知错误";
                break;
        }
        return msg;
    }
}
