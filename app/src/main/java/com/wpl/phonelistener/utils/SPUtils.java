package com.wpl.phonelistener.utils;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * SharedPreferences的工具类
 * <p>
 * Created by Wpl on 2017/1/3.
 * Contact number 18244267955 and Email pl.w@outlook.com
 */
public class SPUtils {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public SPUtils(Context context, String fileName) {
        preferences = context.getSharedPreferences(fileName, MODE_PRIVATE);
        editor = preferences.edit();
    }

    /**
     * SP写入key对应的数据
     *
     * @param key   键
     * @param value 值
     */
    public void putString(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public void putBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    public void putInt(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }

    /**
     * 清空SP的所有数据
     */
    public void clear() {
        editor.clear();
        editor.commit();
    }

    /**
     * 删除SP里指定key对应的数据项
     *
     * @param key 键
     */
    public void remove(String key) {
        editor.remove(key);
        editor.commit();
    }

    /**
     * 获取SP指定key的值
     *
     * @param key      键
     * @param defValue 默认值
     * @return 值
     */
    public String getString(String key, String defValue) {
        return preferences.getString(key, defValue);
    }

    public boolean getBoolean(String key, boolean defValue) {
        return preferences.getBoolean(key, defValue);
    }

    public int getInt(String key, int defValue) {
        return preferences.getInt(key, defValue);
    }
}
